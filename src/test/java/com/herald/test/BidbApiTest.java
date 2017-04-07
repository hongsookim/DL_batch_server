package com.herald.test;

import com.google.gson.Gson;
import com.herald.api.bidb.BidbApi;
import com.herald.api.bidb.BidbResponse;
import com.herald.properties.CommonProperties;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BidbApiTest {

    @Test
    public void testJson() {
        String data = "[{\"prd_no\": \"1516990701\",\"img_path\": \"http://image.11st.co.kr/am/9/9/0/7/0/1/1516990701_B.jpg\",\"category\": \"1001314_1001920_1007297\", \"value\": 0}, {\"prd_no\": \"1642579449\", \"image_path\": \"http://image.11st.co.kr/pd/16/5/7/9/4/4/9/1642579449_B.jpg\", \"category\": \"1001314_1001920_1007297\", \"value\": 0.69061387 }]";

        Gson gson = new Gson();
        BidbResponse[] response = gson.fromJson(data, BidbResponse[].class);
        assertNotNull(response);
    }

    @Test
    public void testRegisterResponse() {
    	CommonProperties properties = new CommonProperties();
        properties.setClSearchUrl("http://172.21.41.115:8080/v1/soho/search/{prod_no}?weight={weight}&start={start}&end={end}");

        BidbApi api = new BidbApi(); 
        api.catalogyProperties = properties;

        String prodNo = "1516990701";

        BidbResponse res = api.ctRegisterTest(properties.getClSearchUrl(), prodNo);  
        assertNotNull(res);
        assertTrue(res.equals("success"));
    }
}
