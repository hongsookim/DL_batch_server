package com.herald.entities;

import javax.persistence.Entity;

@Entity
public class InputItem {
    public String PRD_ID;			//상품아이디 
    public String PRD_NM; 			//상품 이름 
    public String PRD_IMG_URL;		//상품이미지 URL 
    public String LCTGR_ID;			//상품대카 ID
    public String MCTGR_ID;			//상품중카 ID
    public String SCTGR_ID;			//상품소카 ID
    public String DCTGR_ID;			//상품세카 ID
    public String LCTGR_NM;			//상품대카 이름
    public String MCTGR_NM;			//상품중카 이름
    public String SCTGR_NM;			//상품소카 이름  
    public String DCTGR_NM;			//상품세카 이름 
    public String CTLG_ID;			//카탈로그 ID
    public String CTLG_NM;			//카탈로그 이
    public String CTLG_IMG_URL;		//카탈로그 이미지 URL
    public String CTLG_TAG;			//카탈로그 남성/여성 분류 태그 
    
}
