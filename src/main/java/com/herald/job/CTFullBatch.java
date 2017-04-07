package com.herald.job;

import com.herald.api.pns.PnsApi;
import com.herald.entities.InputItem;
import com.herald.job.tasklet.DownloadTasklet;
import com.herald.job.tasklet.StatsTasklet;
import com.herald.properties.CommonProperties;
import com.heraldg.util.MyPartitioner;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
public class CTFullBatch {

    static final Logger logger = Logger.getLogger(CTFullBatch.class);

    @Autowired
    JobBuilderFactory jobs;

    @Autowired
    StepBuilderFactory steps;

    @Autowired
    CommonProperties CatalogProperties;

    @Autowired
    PnsApi pnsAPi;

    @Bean
    public Job ctFullJob(Step ctFullStep, Step ctFullStatsStep) throws Exception{
        if (CatalogProperties.USE_LOCAL_RESOURCE) {
            return jobs.get("ctFullJob")
                    .incrementer(new RunIdIncrementer())
                    .start(ctFullStep)
                    .next(ctFullStatsStep)
                    .build();
        } else {
            return jobs.get("ctFullJob")
                    .incrementer(new RunIdIncrementer())
                    .start(ctFullDownloadStep())
                    .next(ctFullStep)
                    .next(ctFullStatsStep)
                    .build();
        }
    }

    @Bean
    public Step ctFullStep(@Qualifier("tsvFullBatchReader") ItemReader<InputItem> reader,
                           @Qualifier("featureExtractWriter") ItemWriter<InputItem> writer) throws Exception {
        String stepName = "ctFullStep";
        String slaveStepName = stepName + " .slave";
        Step slaveStep = ctFullStepSlave(slaveStepName, reader, writer);

        PartitionStepBuilder partitionStepBuilder =
                steps.get(stepName)
                .partitioner(slaveStepName, new MyPartitioner())
                .step(slaveStep)
                .gridSize(4)
                .taskExecutor(new SimpleAsyncTaskExecutor());
        return partitionStepBuilder.build();
    }

    public Step ctFullStepSlave(String slaveStepName, ItemReader<InputItem> reader, ItemWriter<InputItem> writer) throws Exception {
        return steps.get(slaveStepName)
                .<InputItem, InputItem> chunk(50)
                .reader(reader)
                .writer(writer)
                .build();
    }

    public Step ctFullDownloadStep() {
        DownloadTasklet tasklet = new DownloadTasklet();
        tasklet.setUrl(CatalogProperties.getFulltsv(), CatalogProperties.getDownloadPath());

        return steps.get("downloadCTFullTSV")
                .tasklet(tasklet)
                .build();

    }

    @Bean
    @Qualifier("ctFullStatsStep")
    public Step ctFullStatsStep() {
        StatsTasklet statsTasklet = new StatsTasklet(pnsAPi, "Category Full", CatalogProperties.getFullcsv());
        return steps.get("ctFullStatsStep")
                .tasklet(statsTasklet)
                .build();

    }
}
