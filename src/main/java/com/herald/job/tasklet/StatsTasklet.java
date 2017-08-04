package com.herald.job.tasklet;


import com.herald.api.pns.PnsApi;
import com.heraldg.util.Stats;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class StatsTasklet implements Tasklet {

    PnsApi pnsApi;
    String batchName = "Test Batch";
    String csvName;

    public StatsTasklet(PnsApi api, String batchName, String csvName) {
        this.pnsApi = api;
        this.batchName = batchName;
        this.csvName = csvName;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Stats.show();

        for (String key : Stats.getInstances().keySet()) {
            Stats stat = Stats.getInstances().get(key);
            // slack
            pnsApi.reportBySlack(" *"+key+"* ", csvName, stat.success.get(), stat.error.get(), stat.getDuration());
        }

        return RepeatStatus.FINISHED;
    }
}
