package com.herald.job.tasklet;


import com.heraldg.util.ZipUtil;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.net.URL;

public class DownloadTasklet implements Tasklet {

    static final Logger logger = Logger.getLogger(DownloadTasklet.class);

    private String url;
    private String downloadPath;

    public void setUrl(String url, String downloadPath) {
        this.url = url;
        this.downloadPath = downloadPath;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        if (url == null) {
            throw new Exception("Download URL is not correct.");
        }

        File zipFile = new File(downloadPath+"/" + (url.substring(url.lastIndexOf("/") + 1, url.length())));
        logger.info("url="+url+", zipFile="+zipFile);
        FileUtils.copyURLToFile(new URL(url), zipFile);
        File csvFile = ZipUtil.unzip(zipFile, new File(downloadPath));

        logger.info("csvFile="+csvFile);
        zipFile.deleteOnExit();

        return RepeatStatus.FINISHED;
    }
}
