package com.herald.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(locations = "classpath:catalogy.properties", ignoreUnknownFields = true, prefix="pns")
@Component
@Data
public class PnsProperties {

    private String apiUrl;
    private String appKey;
    private String channelKey;
    private String notificationID;
    private boolean enabled;

}
