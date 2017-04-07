package com.herald.job.writer;

import com.google.gson.Gson;
import com.herald.api.bidb.BidbApi;
import com.herald.api.bidb.BidbRequest;
import com.herald.api.bidb.BidbResponse;
import com.herald.api.ml.MlApi;
import com.herald.api.ml.MlResponse;
import com.herald.entities.InputItem;
import com.herald.properties.CommonProperties;
import com.heraldg.util.Stats;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeatureExtractor {

    static final Logger logger = Logger.getLogger(FeatureExtractor.class);

    @Autowired
    MlApi cnnApi;

    @Autowired
    BidbApi bidbApi;

    @Autowired
    CommonProperties ctProperties;

    String batchNameCategory = "FeatureExtractor Catalogue";

    @Bean
    @Qualifier("featureExtractWriter")
    public ItemWriter<InputItem> featureExtractWriter() throws Exception {
        logger.debug("ctFeatureExtractWriter Start");
//        final File jsonPath = new File(ctProperties.getDownloadPath() + File.separator + resource.getFilename().replaceFirst(".tsv", ".json"));
        final File jsonPath = new File(ctProperties.getDownloadPath() + File.separator + "catalogue_results.csv");
        if (jsonPath.getParentFile().exists() == false) {
            jsonPath.getParentFile().mkdirs();
        }
        
        ItemWriter<InputItem> writer = new ItemWriter<InputItem>() {
        	Map<Thread, BufferedWriter> fileWriterMap = new HashMap<Thread, BufferedWriter>();
        	
        	@Override
            public void write(List<? extends InputItem> items) throws Exception {
            	            	
                Thread t = Thread.currentThread();
                BufferedWriter fileWriter = fileWriterMap.get(t);
                if (fileWriter == null) {
                    fileWriter = new BufferedWriter(new FileWriter(jsonPath+"."+t.getId()));
                    fileWriterMap.put(t, fileWriter);
                }

                logger.info(t.getName()+" | writer : list.size()="+items.size());
                for (InputItem poi : items) {
                    // Extract Feature
                	String prod_id = poi.PRD_ID;
                    String fullImgUrl = poi.PRD_IMG_URL;
                    //String fullImgUrl = ctProperties.getImageUrlPrefix() + poi.PRD_IMG_URL;
                    //MlResponse res = cnnApi.extractFeature(poi.PRD_ID, poi.LCTGR_NM, poi.MCTGR_NM, poi.SCTGR_NM, fullImgUrl);
                    MlResponse res = cnnApi.extractFeature(prod_id, fullImgUrl);
                    Gson gson = new Gson();
                    if (res == null || !"success".equals(res.status)) {
                        logger.error("[CNN] featureExtraction failed. PROD_NO="+poi.PRD_ID);
                        Stats.setError(batchNameCategory);
                        
                        //For failed items!!!
                        if(res == null) {
                        	res = new MlResponse();
                        	res.status = "fail";
                        	res.prod_id = poi.PRD_ID;
                        }
                        fileWriter.write(gson.toJson(res));
                        fileWriter.newLine();
                        
                        continue;
                    }

                    //For success items!!!
                    fileWriter.write(gson.toJson(res));
                    fileWriter.newLine();

                    logger.info(res.toSimpleString());
                }
                fileWriter.flush();
            }
        };
        return writer;
    }
    
//    @Bean
//    @Qualifier("featureExtractDBWriter")
//    public ItemWriter<CatalogInputItem> featureExtractDBWriter() throws Exception {
//        logger.debug("ctFeatureExtractWriter Start");
//
//        ItemWriter<CatalogInputItem> writer = new ItemWriter<CatalogInputItem>() {
//            @Override
//            public void write(List<? extends CatalogInputItem> items) throws Exception {
//                Thread t = Thread.currentThread();
//
//                logger.info(t.getName()+" | writer : list.size()="+items.size());
//                for (CatalogInputItem poi : items) {
//                    // Extract Feature
//                	String prod_id = poi.PRD_ID;
//                    String fullImgUrl = poi.PRD_IMG_URL;
//                    //String fullImgUrl = ctProperties.getImageUrlPrefix() + poi.PRD_IMG_URL;
//                    //MlResponse res = cnnApi.extractFeature(poi.PRD_ID, poi.LCTGR_NM, poi.MCTGR_NM, poi.SCTGR_NM, fullImgUrl);
//                    MlResponse res = cnnApi.extractFeature(prod_id, fullImgUrl);
//                    if (res == null || !"success".equals(res.status)) {
//                        logger.error("[CNN] featureExtraction failed. PROD_NO="+poi.PRD_ID);
//                        Stats.setError(batchNameCategory);
//                        continue;
//                    }
//
//                    logger.info(res.toSimpleString());
//
//                    BidbRequest ctRegisterRequest = new BidbRequest();
//                    ctRegisterRequest.prd_no = poi.PRD_ID;
//                    ctRegisterRequest.image_path = fullImgUrl;
//                    ctRegisterRequest.lctgr_no = poi.LCTGR_NM;
//                    ctRegisterRequest.mctgr_no = poi.MCTGR_NM;
//                    ctRegisterRequest.sctgr_no = poi.SCTGR_NM;
//
//                    //Category Register
//                    BidbResponse bidbRegisterResponse = bidbApi.ctRegister(ctRegisterRequest);
//                    if (bidbRegisterResponse == null) {
//                        logger.error("[BidbAPI] ctRegister failed. PRD_NO="+poi.PRD_ID);
//                        Stats.setError(batchNameCategory);
//                    } else {
//                        Stats.setSuccess(batchNameCategory);
//                    }
//
//                }
//            }
//        };
//        return writer;
//    }
}
