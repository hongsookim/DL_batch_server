package com.herald.api.ml;

import com.herald.properties.CommonProperties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MlApi {

    static final Logger logger = Logger.getLogger(MlApi.class);

    @Autowired
    public CommonProperties CommonProperties;

    public MlResponse extractFeature(String prod_id, String imgPath) {
        String cnnapi = CommonProperties.getCnnapi();

        MlRequest req = new MlRequest();
        req.prod_id = prod_id;
        req.image_url = imgPath;

        RestTemplate restTemplate = new RestTemplate();
        try {
            MlResponse response = restTemplate.postForObject(cnnapi, req, MlResponse.class);
            return response;
        } catch(Exception e) {
            logger.debug("[ML] image analysis failed.");
            return null;
        }
    }
}
