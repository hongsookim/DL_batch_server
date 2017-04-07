package com.herald.api.pns;

import com.herald.properties.PnsProperties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sunho.hong on 2017. 2. 7..
 */
@Component
public class PnsApi {

    static final Logger logger = Logger.getLogger(PnsApi.class);

    @Autowired
    PnsProperties pnsProperties;

    public PnsResponse reportBySlack(String title, String message, int success, int failure, String duration) {
        if (pnsProperties.isEnabled() == false) {
            return null;
        }

        PnsRequest req = new PnsRequest();
        req.notificationId = pnsProperties.getNotificationID();
        req.title = title;
        if (message.startsWith("http://")) {
            req.message = req.message = "Batch Job Result - " + message;
        } else {
            req.message = "Batch Job Result - *_" + message + "_*";
        }
        req.receiverTypeList = new ArrayList<String>(Arrays.asList("slack"));
        req.setSlackInfo(pnsProperties.getChannelKey(), success, failure, duration);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("appKey", pnsProperties.getAppKey());
        HttpEntity<PnsRequest> entity = new HttpEntity<PnsRequest>(req, headers);

        PnsResponse response = restTemplate.postForObject(pnsProperties.getApiUrl(), entity, PnsResponse.class);
        return response;
    }



}
