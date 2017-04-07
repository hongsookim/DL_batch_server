package com.herald.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.herald.entities.InputItem;
import com.herald.job.reader.TSVFileReader;
import com.herald.job.writer.JsonInputFileWriter;
import com.herald.properties.CommonProperties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class CatalogTSVFileItemTest {

    @Before
    public void setup() {
    	CommonProperties.USE_LOCAL_RESOURCE = true; 
    }

    public List<InputItem> getTSVItemList() throws Exception {
        TSVFileReader tsvFileReader = new TSVFileReader();
        tsvFileReader.CatalogProperties = new CommonProperties();
        tsvFileReader.DELIMITER = "\t";

        FlatFileItemReader reader = (FlatFileItemReader) tsvFileReader.tsvFileItemReaderPartial();
        assertNotNull(reader);
        reader.open(new ExecutionContext());

        ItemReader<InputItem> itemReader = reader;
        ArrayList<InputItem> list = new ArrayList();

        InputItem item = null;
        while((item = itemReader.read()) != null) {
            list.add(item);
        }

        // item0
        item = list.get(0);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("item[0] = " + gson.toJson(item));
 //       assertEquals("1000007344", item.PRD_ID);

        return list;
    }

    @Test
    public void testTSVItemReader() throws Exception {
        List<InputItem> list = getTSVItemList();
        assertNotNull(list);
    }

    @Test
    public void testTSVItemWriter() throws Exception {
        JsonInputFileWriter jsonFileWriter = new JsonInputFileWriter();
        jsonFileWriter.CatalogProperties = new CommonProperties();

        ItemWriter<InputItem> writer = jsonFileWriter.tsvFileItemWriterPartial();
        assertNotNull(writer);

        List<InputItem> list = getTSVItemList();
        assertNotNull(list);
        writer.write(list);
    }
    
    @Test
    public void testImageDownload() throws Exception {
    	List<InputItem> list = getTSVItemList();
    	
    	double total_time = 0.0;
//    	double total_size = 0.0;
    	for(int k = 0 ; k < list.size() ; k++) {
        	double start = System.currentTimeMillis();

    		InputItem cii = list.get(k);
	    	
	    	InputStream in = null;
	    	OutputStream out = null;
	    	
	    	try{
		    	URL url = new URL(cii.PRD_IMG_URL);
		    	in = new BufferedInputStream(url.openStream());
		    	String output_path = "/Users/1001249/Data/image_data/"+ cii.PRD_ID + ".jpg";
		    	out = new BufferedOutputStream(new FileOutputStream(output_path));
		
		    	for ( int i; (i = in.read()) != -1; ) {
		    	    out.write(i);
		    	}
//		    	File file = new File(output_path);
//		    	total_size = total_size + file.length();
		    	
		    	in.close();
		    	out.close();
		    	
		    	System.out.println("process time::"+cii.PRD_ID+"::"+(System.currentTimeMillis()-start)+" ms");
		    	total_time = total_time + (System.currentTimeMillis()-start);
	    	}catch(IOException ioe) {
	    		continue;
	    	}
	    }
    	System.out.println("Average process time::"+ (total_time / list.size())+" ms");
//    	System.out.println("Average file size::"+ ((total_size / list.size()) / 1024)+" KB");
    	
    	assertNotNull(list);
    }
}
