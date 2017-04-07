package com.herald.application;

import com.heraldg.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

@SpringBootApplication
@ComponentScan(basePackages = {"com.skplanet.milab.catalog"})
public class BatchApplication {

    static final Logger logger = Logger.getLogger(BatchApplication.class);

    public static void main(String[] args)  throws BeansException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, InterruptedException {
        logger.debug("Summary BatchApplication main start");

        SpringApplicationBuilder builder = new SpringApplicationBuilder(new Object[0]);
        builder.sources(new Class[]{
                BatchApplication.class,
        });
        builder.web(false);
        ApplicationContext context = builder.run(args);

        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();

        String[] jobNames = args;
        if(StringUtil.isNotEmpty(jobNames)) {
            for(String jobName : jobNames) {
                logger.info("jobName:::[" + jobName + "]");
                if(StringUtil.isNotEmpty(jobName) && jobName.endsWith("Job")) {
                    JobExecution jobExecution = jobLauncher.run(context.getBean(jobName, Job.class), jobParameters);
                }
                logger.info("jobName:::[" + jobName + "] is finished.");
            }
        }

        logger.debug("Summary Batch Application main end");
    }
}
