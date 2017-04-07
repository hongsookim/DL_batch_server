package com.herald.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.herald.api.ml.MlApi;
import com.herald.api.ml.MlResponse;
import com.herald.properties.CommonProperties;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MlApiTest {

    @Test
    public void testCatalogFeatureExtraction() {

    	CommonProperties CatalogProperties = new CommonProperties();
    	CatalogProperties.setCnnapi("http://175.126.56.240:10000/v1/getFeature");

        MlApi api = new MlApi();
        api.CatalogProperties = CatalogProperties;

        MlResponse res = api.extractFeature("825933261","http://image.11st.co.kr/al/5/9/7/7/3/5/1373597735_B.jpg");
        assertNotNull(res);
        System.out.println(res);
        assertEquals("825933261", res.prod_id); 
    }
      
    @Test
    public void testLoadImageFile() {
    	MockMultipartFile file = null;
    	try{
    		InputStream contentStream = this.getClass().getClassLoader()
    			    .getResourceAsStream("2016-12-19_10-57-06.193788..jpg");

    		file = new MockMultipartFile("imagefile", contentStream); 

	    	System.out.println("file size::"+file.getSize());
	    	System.out.println("file name::"+file.getOriginalFilename());
	    	System.out.println("file contents::"+file.getBytes());

    	}catch (FileNotFoundException fnfex){
    		System.out.println("FileNotFoundException::"+fnfex.getMessage());
    	}catch (IOException ioex) { 
    		System.out.println("IOException::"+ioex.getMessage());
    	}
    	
        assertEquals( 274368, file.getSize() ); 
    }
    
    @Test
    public void testClassLoader() {
    	    	   	
    	MockMultipartFile file = null;
    	try{
    		InputStream contentStream = this.getClass().getClassLoader()
    			    .getResourceAsStream("2016-12-19_10-57-06.193788..jpg");
    		file = new MockMultipartFile("imagefile", "2016-12-19_10-57-06.193788..jpg", "multipart/form-data", contentStream); 

	    	System.out.println("file size::"+file.getSize());
	    	System.out.println("file name::"+file.getOriginalFilename());
	    	System.out.println("file contents::"+file.getBytes());

    	}catch (FileNotFoundException fnfex){
    		System.out.println("FileNotFoundException::"+fnfex.getMessage());
    	}catch (IOException ioex) { 
    		System.out.println("IOException::"+ioex.getMessage());
    	}
    	
        assertEquals( "/Users/1001249/projects/realtime_11st_batch/src/test/resources/2016-12-19_10-57-06.193788..jpg", file.getOriginalFilename() ); 
    }
}
