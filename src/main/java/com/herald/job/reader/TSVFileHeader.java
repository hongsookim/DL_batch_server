package com.herald.job.reader;

public enum TSVFileHeader {
	PRD_ID,
	PRD_NM,
	PRD_IMG_URL,
	LCTGR_ID,
	MCTGR_ID,
	SCTGR_ID,
	DCTGR_ID,
	LCTGR_NM,
	MCTGR_NM,
	SCTGR_NM,
	DCTGR_NM,
	CTLG_ID,
	CTLG_NM,
	CTLG_IMG_URL,
	CTLG_TAG;
    
    public static String[] getNames() {
        TSVFileHeader[] list = TSVFileHeader.values();
        String[] strList = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            strList[i] = list[i].toString();
        }
        return strList;
    }

}
