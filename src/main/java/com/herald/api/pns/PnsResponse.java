package com.herald.api.pns;

import java.util.List;

/**
 * Created by sunho.hong on 2017. 2. 7..
 */
public class PnsResponse {

    List<TypeResponse> slack;

    static class TypeResponse {
        String notificationId;
        String body;
        String httpStatus;
        String description;
    }
}
