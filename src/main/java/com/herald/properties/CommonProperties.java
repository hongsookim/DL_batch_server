package com.herald.properties;

import java.io.File;
import java.text.SimpleDateFormat;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@ConfigurationProperties(locations = "classpath:catalog.properties", ignoreUnknownFields = false, prefix="ct")
@Component
@Data
public class CommonProperties {

    private String clRegisterUrl;
    private String clSearchUrl; 

    private static String TEST_CSV_FULL = "designer_man_fashion.csv";
    private static String TEST_CSV_PARTIAL = "designer_man_fashion_part.csv";

    private static String TEST_TSV_FULL = "brand.tsv";
    private static String TEST_TSV_PARTIAL = "brand.tsv";
    
    private String fullcsv;
    private String partialcsv;
    private String fulltsv;
    private String partialtsv;
    private String imageUrlPrefix;
    private int    gridSize = 4;

    private String downloadPath = "result";
    private String cnnapi;

    public static String WORKING_DAY = null;
    public static boolean USE_LOCAL_RESOURCE = false;
    
    public String getPartialcsv() {
        if (USE_LOCAL_RESOURCE) {
            return "classpath:" + TEST_CSV_PARTIAL;
        } else {
            return partialcsv.replaceAll("TODAY", WORKING_DAY);
        }
    }

    public String getFullcsv() {
        if (USE_LOCAL_RESOURCE) {
            return "classpath:" + TEST_CSV_FULL;
        } else {
            return fullcsv;
        }
    }

    public String getPartialtsv() {
        if (USE_LOCAL_RESOURCE) {
            return "classpath:" + TEST_TSV_PARTIAL;
        } else {
            return partialtsv.replaceAll("TODAY", WORKING_DAY);
        }
    }

    public String getFulltsv() {
        if (USE_LOCAL_RESOURCE) {
            return "classpath:" + TEST_TSV_FULL;
        } else {
            return fulltsv;
        }
    }
    
    static {
        String target = System.getProperty("target");
        if (StringUtils.isEmpty(target) == false) {
            WORKING_DAY = target;
        } else {
            // today
            SimpleDateFormat formatInstance1 = new SimpleDateFormat("yyyyMMdd");
            WORKING_DAY = formatInstance1.format(new java.util.Date());
        }
    }

    public Resource getPartialcsvDownloadResource() {
        if (USE_LOCAL_RESOURCE) {
            return new ClassPathResource(TEST_CSV_PARTIAL);
        } else {
            String url = getPartialcsv();
            String filePath = url.substring(url.lastIndexOf("/") + 1, url.length()).replaceFirst(".zip", ".csv");
            return new FileSystemResource(downloadPath + File.separator + filePath);
        }
    }

    public Resource getFullcsvDownloadResource() {
        if (USE_LOCAL_RESOURCE) {
            return new ClassPathResource(TEST_CSV_FULL);
        } else {
            String url = fullcsv;
            String filePath = url.substring(url.lastIndexOf("/") + 1, url.length()).replaceFirst(".zip", ".csv");
            return new FileSystemResource(downloadPath + File.separator + filePath);
        }
    }
    
    public Resource getPartialtsvDownloadResource() {
        if (USE_LOCAL_RESOURCE) {
            return new ClassPathResource(TEST_TSV_PARTIAL);
        } else {
            String url = getPartialtsv();
            String filePath = url.substring(url.lastIndexOf("/") + 1, url.length()).replaceFirst(".zip", "tsv");
            return new FileSystemResource(downloadPath + File.separator + filePath);
        }
    }

    public Resource getFulltsvDownloadResource() {
        if (USE_LOCAL_RESOURCE) {
            return new ClassPathResource(TEST_TSV_FULL);
        } else {
            String url = fulltsv;
            String filePath = url.substring(url.lastIndexOf("/") + 1, url.length()).replaceFirst(".zip", ".tsv");
            return new FileSystemResource(downloadPath + File.separator + filePath);
        }
    }
}
