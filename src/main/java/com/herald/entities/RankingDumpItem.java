package com.herald.entities;

public class RankingDumpItem {

    public String PRD_NO;
    public int COLOR_SIMILARITY;
    public int EDGE_SIMILARITY;
    public String CATEGORY1;
    public String CATEGORY2;
    public String CATEGORY3;
    public String CATEGORY4;
    public String SELLER_TYPE;
    public String IMAGE_HASH;
    public String IS_BRAND;

    @Override
    public String toString() {
        return PRD_NO+","+COLOR_SIMILARITY+","+EDGE_SIMILARITY+","+
                CATEGORY1+"|"+CATEGORY2+"|"+CATEGORY3+"|"+CATEGORY4+"|"+SELLER_TYPE+"|"+IMAGE_HASH+"|"+IS_BRAND;
    }

    public void setCategory(String category) {
        if (category == null) {
            return;
        }

        String[] ctgrList = category.split("_");
        if (ctgrList == null || ctgrList.length != 3) {
            return;
        }

        this.CATEGORY1 = ctgrList[0];
        this.CATEGORY2 = ctgrList[1];
        this.CATEGORY3 = ctgrList[2];
    }
}
