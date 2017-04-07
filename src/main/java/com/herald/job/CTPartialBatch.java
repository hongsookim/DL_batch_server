package com.herald.job;

import com.herald.api.pns.PnsApi;
import com.herald.entities.InputItem;
import com.herald.job.tasklet.DownloadTasklet;
import com.herald.job.tasklet.StatsTasklet;
import com.herald.properties.CommonProperties;
import com.herald.properties.PnsProperties;
import com.heraldg.util.MyPartitioner;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.PartitionStepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.skplanet.milab.catalog"})
public class CTPartialBatch {

    static final Logger logger = Logger.getLogger(CTPartialBatch.class);

    @Autowired
    JobBuilderFactory jobs;

    @Autowired
    StepBuilderFactory steps;

    @Autowired
    CommonProperties CatalogProperties;

    @Autowired
    PnsProperties pnsProperties;

    @Autowired
    PnsApi pnsAPi;

    @Bean
    public Job ctPartialJob(Step ctPartialFeatureExtractStep, Step ctPartialStatsStep) throws Exception{
        if (CatalogProperties.USE_LOCAL_RESOURCE) {
            return jobs.get("ctPartialJob")
            		.start(ctPartialFeatureExtractStep)
            		.next(ctPartialStatsStep).build();
        } else {
            return jobs.get("ctPartialJob")
            		.start(ctPartialDownloadStep())
            		.next(ctPartialFeatureExtractStep)
            		.next(ctPartialStatsStep).build();
        }
    }

    @Bean
    public Step ctPartialFeatureExtractStep(@Qualifier("tsvPartialBatchReader") ItemReader<InputItem> reader,
                                         @Qualifier("featureExtractWriter") ItemWriter<InputItem> writer) throws Exception {
        Step slaveStep = ctPartialStepSlave(reader, writer);
        PartitionStepBuilder partitionStepBuilder =
                steps.get("ctPartialFeatureExtractStep")
                .partitioner("ctPartialFeatureExtractStep.slave", new MyPartitioner())
                .step(slaveStep)
                .gridSize(CatalogProperties.getGridSize())
                .taskExecutor(new SimpleAsyncTaskExecutor());
        return partitionStepBuilder.build();
    }

    public Step ctPartialStepSlave(ItemReader<InputItem> reader, ItemWriter<InputItem> writer) throws Exception {
        return steps.get("ctPartialFeatureExtractStep.slave")
                .<InputItem, InputItem> chunk(50)
                .reader(reader)
                .writer(writer)
                .build();
    }

    public Step ctPartialDownloadStep() {
        DownloadTasklet tasklet = new DownloadTasklet();
        tasklet.setUrl(CatalogProperties.getPartialtsv(), CatalogProperties.getDownloadPath());

        return steps.get("downloadCTPartialTSV")
                .tasklet(tasklet)
                .build();

    }

    @Bean
    @Qualifier("ctPartialStatsStep")
    public Step ctPartialStatsStep() {
        StatsTasklet statsTasklet = new StatsTasklet(pnsAPi, "Category Partial", CatalogProperties.getPartialtsv());
        return steps.get("ctPartialStatsStep")
                .tasklet(statsTasklet)
                .build();

    }
}
