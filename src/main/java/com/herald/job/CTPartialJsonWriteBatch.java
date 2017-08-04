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
@ComponentScan(basePackages = {"com.herald"})
public class CTPartialJsonWriteBatch extends CTPartialBatch {

    static final Logger logger = Logger.getLogger(CTPartialJsonWriteBatch.class);

    @Bean
    public Job ctPartialJsonWriteJob(Step ctPartialJsonWriteStep) throws Exception{
        if (CommonProperties.USE_LOCAL_RESOURCE) {
            return jobs.get("ctPartialJsonWriteBatchJob")
            		.start(ctPartialJsonWriteStep)
            		.build();
        } else {
            return jobs.get("ctPartialJsonWriteBatchJob")
            		.start(ctPartialDownloadStep())
            		.next(ctPartialJsonWriteStep)
            		.build();
        }
    }

    @Bean
    public Step ctPartialJsonWriteStep(@Qualifier("tsvPartialBatchReader") ItemReader<InputItem> reader,
                                         @Qualifier("tsvPartialBatchWriter") ItemWriter<InputItem> writer) throws Exception {
        Step slaveStep = ctPartialStepSlave(reader, writer);
        PartitionStepBuilder partitionStepBuilder =
                steps.get("processCTPartialTSV")
                .partitioner("ctPartialJsonWriteStep.slave", new MyPartitioner())
                .step(slaveStep)
                .gridSize(CommonProperties.getGridSize())
                .taskExecutor(new SimpleAsyncTaskExecutor());
        return partitionStepBuilder.build();
    }

    public Step ctPartialStepSlave(ItemReader<InputItem> reader, ItemWriter<InputItem> writer) throws Exception {
        return steps.get("ctPartialJsonWriteStep.slave")
                .<InputItem, InputItem> chunk(100)
                .reader(reader)
                .writer(writer)
                .build();
    }

}
