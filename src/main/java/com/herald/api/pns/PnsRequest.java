package com.herald.api.pns;

import java.util.*;

/**
 * Created by sunho.hong on 2017. 2. 7..
 */
public class PnsRequest {

    public String notificationId;
    public String title;
    public String message;

    public List<String> receiverTypeList = new ArrayList<String>(Arrays.asList("slack"));
    public Map<String, List<TypeInfo>> receiverTypeInfo;

    /*
        {
            "channelKey": "T3QFSG37W/B41NVUASF/UlUUY04jmyZq7VxFCpEisPrX",
            "attachments": [
                {
                    "color": "good",
                    "text" : "Success: 1"
                },
                {
                    "color": "danger",
                    "text" : "Failure: 0"
                },
                {
                    "color": "#003E77",
                    "text" : "Total: 1"
                },
                {
                    "text" : "Timestamp: 2017-02-07"
                }
            ]
        }
     */

    class TypeInfo {
        String channelKey;
        List<TextInfo> attachments;
    }

    class TextInfo {
        String color;
        String text;

        public TextInfo(String color, String text) {
            this.color = color;
            this.text = text;
        }
    }

    public void setSlackInfo(String channelKey, int success, int failure, String duration) {
        TypeInfo typeInfo = new TypeInfo();
        typeInfo.channelKey = channelKey;

        typeInfo.attachments = new ArrayList<TextInfo>(Arrays.asList(
                new TextInfo("good", "Success: " + success),
                new TextInfo("danger", "Failure: " + failure),
                new TextInfo("#003E77", "Total: " + (success+failure))
        ));

        if (duration != null) {
            typeInfo.attachments.add(new TextInfo(null, "Duration: " + duration));
        }

        typeInfo.attachments.add(new TextInfo(null,"Timestamp: " + new Date().toString()));

        this.receiverTypeInfo = new HashMap<String, List<TypeInfo>>();
        this.receiverTypeInfo.put("slack", new ArrayList<TypeInfo>(Arrays.asList(typeInfo)));
    }
}
