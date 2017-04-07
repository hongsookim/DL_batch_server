package com.herald.job.reader;

import com.herald.entities.ResultItem;
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
public class CSVFileReader<T> extends FlatFileItemReader<T> {

    //CatalogyProperties
    @Autowired
    public CommonProperties CatalogyProperties;
    

    @Bean
    @Qualifier("categoryPartialBatchReader")
    public ItemReader<ResultItem> csvFileItemReaderPartial() throws Exception {
        return getItemReader(CatalogyProperties.getPartialcsvDownloadResource());
    }

    @Bean
    @Qualifier("categoryFullBatchReader")
    public ItemReader<ResultItem> csvFileItemReaderFull() throws Exception {
        return getItemReader(CatalogyProperties.getFullcsvDownloadResource());
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

    private ItemReader<ResultItem> getItemReader(Resource resource) throws Exception {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames(CSVFileHeader.getNames());
        DefaultLineMapper<ResultItem> lineMapper = new DefaultLineMapper<ResultItem>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new FieldSetMapper<ResultItem>() {
            @Override
            public ResultItem mapFieldSet(FieldSet fieldSet) throws BindException {
            	ResultItem item = new ResultItem();
                item.PRD_ID = fieldSet.readRawString(CSVFileHeader.PRD_NO.toString());
                item.PRD_NM = fieldSet.readRawString(CSVFileHeader.PRD_NM.toString());
                item.LCTGR_NM = fieldSet.readRawString(CSVFileHeader.LCTGR_NM.toString());
                item.MCTGR_NM = fieldSet.readRawString(CSVFileHeader.MCTGR_NM.toString());
                item.SCTGR_NM = fieldSet.readRawString(CSVFileHeader.SCTGR_NM.toString());
                item.PRD_IMG_URL = fieldSet.readRawString(CSVFileHeader.IMG_URL.toString());
                item.FEAT = fieldSet.readRawString(CSVFileHeader.FEAT.toString());
                item.CLABEL = fieldSet.readRawString(CSVFileHeader.CLABEL.toString());
                item.CLABEL_NNDID = fieldSet.readRawString(CSVFileHeader.CLABEL_NNDID.toString());
                return item;

            }
        });

        CSVFileReader<ResultItem> reader = new CSVFileReader();
        reader.setEncoding("UTF-8");
        reader.setResource(resource);
        reader.setLineMapper(lineMapper);
        return reader;
    }
}
