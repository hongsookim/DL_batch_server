package com.herald.entities;

import java.util.List;

public class RankingDump {

    public String PRD_NO;
    public String CREATE_DT; // YYYYMMDDHH24MISS
    public String CATEGORY1;
    public String CATEGORY2;
    public String CATEGORY3;
    public String CATEGORY4;
    public String SELLER_TYPE;
    public String IMAGE_HASH;
    public String IS_BRAND;

    public List<RankingDumpItem> matchList;

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer(PRD_NO)
                .append(",")
                .append(CREATE_DT).append("|").append(CATEGORY1).append("|").append(CATEGORY2).append("|")
                .append(CATEGORY3).append("|").append(CATEGORY4).append("|").append(SELLER_TYPE).append("|").append(IMAGE_HASH).append("|").append(IS_BRAND);

        if (matchList == null) {
            result.append(",").append(0);
            return result.toString();
        }

        result.append(",").append(matchList.size());
        for (RankingDumpItem item : matchList) {
            result.append(",").append(item.toString());
        }
        return result.toString();
    }

}
