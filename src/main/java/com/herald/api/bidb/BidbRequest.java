package com.herald.api.bidb;

import java.util.List;

public class BidbRequest {

    public String image_path;
    public String prd_no;

    public String lctgr_no;
    public String mctgr_no;
    public String sctgr_no;

    public List<Double> shape_feature;
    public List<Double> pattern_feature;
    public List<Double> color_feature;

    public String create_dt;
    public String brand_yn;

}
