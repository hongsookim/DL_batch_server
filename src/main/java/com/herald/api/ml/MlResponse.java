package com.herald.api.ml;

import java.util.List;

public class MlResponse {

	public String status;
	public String prod_id;
	public String image_url;
    public List<Double> pca_feature;

    @Override
    public String toString() {
        return "MlResponse{" + "status=" + status + ", prod_id=" + prod_id + ", image_url=" + image_url + ", pca_feature=" + pca_feature + '}';
    }

    public String toSimpleString() {
        return "MlResponse{" + "status=" + status + ", prod_id=" + prod_id + ", image_url=" + image_url + ", pca_feature=" + pca_feature.size() + '}';
    }
}
