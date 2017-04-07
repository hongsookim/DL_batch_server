package com.herald.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.herald.entities.ResultItem;
import com.herald.job.reader.CSVFileReader;
import com.herald.job.writer.JsonResultFileWriter;
import com.herald.properties.CommonProperties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class CatalogCSVFileItemTest {

    @Before
    public void setup() {
    	CommonProperties.USE_LOCAL_RESOURCE = true; 
    }
    
    public List<ResultItem> getCSVItemList() throws Exception {
        CSVFileReader csvFileReader = new CSVFileReader();
        csvFileReader.CatalogyProperties = new CommonProperties(); 

        FlatFileItemReader reader = (FlatFileItemReader) csvFileReader.csvFileItemReaderPartial();
        assertNotNull(reader);
        reader.open(new ExecutionContext());

        ItemReader<ResultItem> itemReader = reader;
        ArrayList<ResultItem> list = new ArrayList();

        ResultItem item = null;
        while((item = itemReader.read()) != null) {
            list.add(item);
        }

        assertEquals(4, list.size());

        // item0
        item = list.get(0);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("item[0] = " + gson.toJson(item));
        assertEquals("1369866158", item.PRD_ID);
        assertEquals("셔츠", item.MCTGR_NM);

        // item3
        item = list.get(3);
        System.out.println("item[5] = " + gson.toJson(item));


        return list;
    }

    @Test
    public void testCSVItemReader() throws Exception {
        List<ResultItem> list = getCSVItemList();
        assertNotNull(list);
    }

    @Test
    public void testCSVItemWriter() throws Exception {
        JsonResultFileWriter jsonFileWriter = new JsonResultFileWriter();
        jsonFileWriter.CatalogProperties = new CommonProperties();

        ItemWriter<ResultItem> writer = jsonFileWriter.csvFileItemWriterPartial();
        assertNotNull(writer);

        List<ResultItem> list = getCSVItemList();
        assertNotNull(list);
        writer.write(list);
    }
}
