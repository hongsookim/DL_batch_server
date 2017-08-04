package com.herald.job.writer;

import com.google.gson.Gson;
import com.herald.entities.InputItem;
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
public class JsonInputFileWriter {

    static final Logger logger = Logger.getLogger(JsonInputFileWriter.class);

    @Autowired
    public CommonProperties ctProperties;

    @Bean
    @Qualifier("tsvPartialBatchWriter")
    public ItemWriter<InputItem> tsvFileItemWriterPartial() throws Exception {
        return getItemWriter(ctProperties.getPartialtsvDownloadResource());
    }

    @Bean
    @Qualifier("tsvFullBatchWriter")
    public ItemWriter<InputItem> tsvFileItemWriterFull() throws Exception {
        return getItemWriter(ctProperties.getFulltsvDownloadResource());
    }

    public ItemWriter<InputItem> getItemWriter(Resource resource) throws Exception {
        logger.debug("tsvFileItemWriter Start");

        final File jsonPath = new File(ctProperties.getDownloadPath() + File.separator + resource.getFilename().replaceFirst(".tsv", ".json"));
        if (jsonPath.getParentFile().exists() == false) {
            jsonPath.getParentFile().mkdirs();
        }

        ItemWriter<InputItem> writer = new ItemWriter<InputItem>() {
            Map<Thread, BufferedWriter> fileWriterMap = new HashMap<Thread, BufferedWriter>();

            @Override
            public void write(List<? extends InputItem> items) throws Exception {
                Thread t = Thread.currentThread();
                BufferedWriter fileWriter = fileWriterMap.get(t);
                if (fileWriter == null) {
                    fileWriter = new BufferedWriter(new FileWriter(jsonPath+"."+t.getId()));
                    fileWriterMap.put(t, fileWriter);
                }

                logger.info(t.getName()+" | writer : list.size()="+items.size());
                for (InputItem poi : items) {
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
