package com.herald.job.writer;

import com.google.gson.Gson;
import com.herald.entities.ResultItem;
import com.herald.properties.CommonProperties;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonResultFileWriter {

    static final Logger logger = Logger.getLogger(JsonResultFileWriter.class);

    @Autowired
    public CommonProperties ctProperties;

    @Bean
    @Qualifier("ctPartialBatchWriter")
    public ItemWriter<ResultItem> csvFileItemWriterPartial() throws Exception {
        return getItemWriter(ctProperties.getPartialcsvDownloadResource());
    }

    @Bean
    @Qualifier("ctFullBatchWriter")
    public ItemWriter<ResultItem> csvFileItemWriterFull() throws Exception {
        return getItemWriter(ctProperties.getFullcsvDownloadResource());
    }

    public ItemWriter<ResultItem> getItemWriter(Resource resource) throws Exception {
        logger.debug("csvFileItemWriter Start");

        final File jsonPath = new File(ctProperties.getDownloadPath() + File.separator + resource.getFilename().replaceFirst(".csv", ".json"));
        if (jsonPath.getParentFile().exists() == false) {
            jsonPath.getParentFile().mkdirs();
        }

        ItemWriter<ResultItem> writer = new ItemWriter<ResultItem>() {
            Map<Thread, BufferedWriter> fileWriterMap = new HashMap<Thread, BufferedWriter>();

            @Override
            public void write(List<? extends ResultItem> items) throws Exception {
                Thread t = Thread.currentThread();
                BufferedWriter fileWriter = fileWriterMap.get(t);
                if (fileWriter == null) {
                    fileWriter = new BufferedWriter(new FileWriter(jsonPath+"."+t.getId()));
                    fileWriterMap.put(t, fileWriter);
                }

                logger.info(t.getName()+" | writer : list.size()="+items.size());
                for (ResultItem poi : items) {
                    Gson gson = new Gson();
                    fileWriter.write(gson.toJson(poi));
                    fileWriter.newLine();
                }
                fileWriter.flush();
            }
        };
        return writer;
    }
}
