package com.herald.job;

import com.herald.entities.InputItem;
import com.heraldg.util.MyPartitioner;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.builder.PartitionStepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = {"com.skplanet.milab.catalog"})
public class CTFullJsonWriteBatch extends CTFullBatch {

    static final Logger logger = Logger.getLogger(CTFullJsonWriteBatch.class);

    @Bean
    public Job ctFullJsonWriteJob(Step ctFullJsonWriteStep) throws Exception{
        if (CatalogProperties.USE_LOCAL_RESOURCE) {
            return jobs.get("ctFullJsonWriteJob")
            		.start(ctFullJsonWriteStep)
            		.build();
        } else {
            return jobs.get("ctFullJsonWriteJob")
            		.start(ctFullDownloadStep())
            		.next(ctFullJsonWriteStep)
            		.build();
        }
    }

    @Bean
    public Step ctFullJsonWriteStep(@Qualifier("tsvFullBatchReader") ItemReader<InputItem> reader,
                                         @Qualifier("tsvFullBatchWriter") ItemWriter<InputItem> writer) throws Exception {
        String stepName = "ctFullJsonWriteStep";
        String slaveStepName = stepName + " .slave";

        Step slaveStep = ctFullStepSlave(slaveStepName, reader, writer);
        PartitionStepBuilder partitionStepBuilder =
                steps.get(stepName)
                .partitioner(slaveStepName, new MyPartitioner())
                .step(slaveStep)
                .gridSize(CatalogProperties.getGridSize())
                .taskExecutor(new SimpleAsyncTaskExecutor());
        return partitionStepBuilder.build();
    }
}
