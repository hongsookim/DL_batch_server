package com.herald.test;

import com.herald.application.BatchApplication;
import com.herald.properties.CommonProperties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CatalogResultFileBatchJobTest {

    private JobExecution runJob(String jobName) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.sources(new Class[]{BatchApplication.class});
        builder.web(false);
        ApplicationContext context = builder.run(new String[] {});
        JobLauncher jobLauncher = (JobLauncher) context.getBean(JobLauncher.class);

        JobParameters jobParameters = new JobParametersBuilder().addDate("date", new Date()).toJobParameters();
        Job job = context.getBean(jobName, Job.class);
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        System.out.println("jobExecution = " + jobExecution.getStatus());
        return jobExecution;
    }

    @Before
    public void setup() {
    	CommonProperties.USE_LOCAL_RESOURCE = true; 
    }

    @Test
    public void testCtFullJsonWriteJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String jobName = "ctFullJsonWriteJob";
        JobExecution jobExecution  = runJob(jobName);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus()); 
    }

    @Test
    public void testCtFullJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String jobName = "ctFullJob";
        JobExecution jobExecution  = runJob(jobName);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Test
    public void testCtPartialJsonWriteJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String jobName = "ctPartialJsonWriteJob";
        JobExecution jobExecution  = runJob(jobName);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Test
    public void testCtPartialJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String jobName = "ctPartialJob";
        JobExecution jobExecution  = runJob(jobName);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }
}
