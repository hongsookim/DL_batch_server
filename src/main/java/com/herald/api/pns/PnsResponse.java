package com.herald.api.pns;

import java.util.List;

public class PnsResponse {

    List<TypeResponse> slack;

    static class TypeResponse {
        String notificationId;
        String body;
        String httpStatus;
        String description;
    }
}
