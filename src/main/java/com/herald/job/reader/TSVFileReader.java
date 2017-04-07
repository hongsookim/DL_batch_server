package com.herald.job.reader;

import com.herald.entities.InputItem;
import com.herald.properties.CommonProperties;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TSVFileReader<T> extends FlatFileItemReader<T> {

    //CatalogProperties
    @Autowired
    public CommonProperties CatalogProperties;
    
    public static String DELIMITER = "\t";

    @Bean
    @Qualifier("tsvPartialBatchReader")
    public ItemReader<InputItem> tsvFileItemReaderPartial() throws Exception {
        return getItemReader(CatalogProperties.getPartialtsvDownloadResource());
    }

    @Bean
    @Qualifier("tsvFullBatchReader")
    public ItemReader<InputItem> tsvFileItemReaderFull() throws Exception {
        return getItemReader(CatalogProperties.getFulltsvDownloadResource());
    }

    AtomicInteger readCount = new AtomicInteger();
    @Override
    public synchronized T read() throws Exception, UnexpectedInputException, ParseException {
        try {
            T result = super.read();
            readCount.incrementAndGet();
            return result;
        } catch(Exception e) {
            if (readCount.get() == 0) {
                throw e;
            } else {
                return null;
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //super.afterPropertiesSet();
    }

    private ItemReader<InputItem> getItemReader(Resource resource) throws Exception {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(DELIMITER);	//tab delimiter
        tokenizer.setNames(TSVFileHeader.getNames());
        DefaultLineMapper<InputItem> lineMapper = new DefaultLineMapper<InputItem>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new FieldSetMapper<InputItem>() {
            @Override
            public InputItem mapFieldSet(FieldSet fieldSet) throws BindException {

            	InputItem item = new InputItem();
                item.PRD_ID = fieldSet.readRawString(TSVFileHeader.PRD_ID.toString());
                item.PRD_NM = fieldSet.readRawString(TSVFileHeader.PRD_NM.toString());
                item.PRD_IMG_URL = fieldSet.readRawString(TSVFileHeader.PRD_IMG_URL.toString());
                item.LCTGR_ID = fieldSet.readRawString(TSVFileHeader.LCTGR_ID.toString());
                item.MCTGR_ID = fieldSet.readRawString(TSVFileHeader.MCTGR_ID.toString());
                item.SCTGR_ID = fieldSet.readRawString(TSVFileHeader.SCTGR_ID.toString());
                item.DCTGR_ID = fieldSet.readRawString(TSVFileHeader.DCTGR_ID.toString());
                item.LCTGR_NM = fieldSet.readRawString(TSVFileHeader.LCTGR_NM.toString());
                item.MCTGR_NM = fieldSet.readRawString(TSVFileHeader.MCTGR_NM.toString());
                item.SCTGR_NM = fieldSet.readRawString(TSVFileHeader.SCTGR_NM.toString());
                item.DCTGR_NM = fieldSet.readRawString(TSVFileHeader.DCTGR_NM.toString());
                item.CTLG_ID = fieldSet.readRawString(TSVFileHeader.CTLG_ID.toString());
                item.CTLG_NM = fieldSet.readRawString(TSVFileHeader.CTLG_NM.toString());
                item.CTLG_IMG_URL = fieldSet.readRawString(TSVFileHeader.CTLG_IMG_URL.toString());
                item.CTLG_TAG = fieldSet.readRawString(TSVFileHeader.CTLG_TAG.toString());
                return item;

            }
        });

        TSVFileReader<InputItem> reader = new TSVFileReader();
        reader.setEncoding("UTF-8");
        reader.setResource(resource);
        reader.setLineMapper(lineMapper);
        return reader;
    }
}
