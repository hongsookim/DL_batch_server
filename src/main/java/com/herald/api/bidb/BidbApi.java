package com.herald.api.bidb;

import com.herald.properties.CommonProperties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class BidbApi {

    static final Logger logger = Logger.getLogger(BidbApi.class);

    @Autowired
    public CommonProperties CommonProperties;

    private BidbResponse registerInternal(BidbRequest req, String url) {
        RestTemplate restTemplate = new RestTemplate();
        BidbResponse response = restTemplate.postForObject(url, Arrays.asList(req), BidbResponse.class);
        logger.debug(response);
        return response;
    }
    
    public BidbResponse ctRegister(BidbRequest req) {
        return registerInternal(req, CommonProperties.getClRegisterUrl());
    }
    
    public BidbResponse ctRegisterTest(String url, String prodID) {
        RestTemplate restTemplate = new RestTemplate();
        BidbResponse response = restTemplate.getForObject(url, BidbResponse.class, prodID);
        logger.debug(response);
        return response;    }
    
}
