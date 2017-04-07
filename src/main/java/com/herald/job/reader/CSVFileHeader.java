package com.herald.job.reader;

public enum CSVFileHeader {
    PRD_NO,
    PRD_NM,
    LCTGR_NM,
    MCTGR_NM,
    SCTGR_NM,
    IMG_URL,
    FEAT,
    CLABEL,
    CLABEL_NNDID;
    
    public static String[] getNames() {
        CSVFileHeader[] list = CSVFileHeader.values();
        String[] strList = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            strList[i] = list[i].toString();
        }
        return strList;
    }

}
