package com.herald.api.ml;

public class MlRequest {

	public String prod_id;
    public String image_url;

    @Override
    public String toString() {
        return "MlRequest{" + "prod_id=" + prod_id +  ", image_url=" + image_url + '\'' + '}';
    }
}
