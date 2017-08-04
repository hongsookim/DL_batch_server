package com.heraldg.util;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Stats {

    static final Logger logger = Logger.getLogger(Stats.class);

    public AtomicInteger success = new AtomicInteger();
    public AtomicInteger error   = new AtomicInteger();

    private long startTime;

    private static HashMap<String, Stats> instances = new HashMap<>();

    public synchronized static Stats getStats(String name) {
        Stats stat = instances.get(name);
        if (stat == null) {
            stat = new Stats();
            instances.put(name, stat);
            stat.startTime = System.currentTimeMillis();
        }

        return stat;
    }

    public static HashMap<String, Stats> getInstances() {
        return instances;
    }

    public String getDuration() {
        long millis = System.currentTimeMillis() - startTime;
        return DurationFormatUtils.formatDuration(millis, "H:mm:ss", true);
    }

    public static void setSuccess(String name) {
        getStats(name).success.incrementAndGet();
    }

    public static void setError(String name) {
        getStats(name).error.incrementAndGet();
    }

    public static void show() {
        for (String key : instances.keySet()) {
            Stats stat = instances.get(key);
            logger.info("================================");
            logger.info("name    = " + key);
            logger.info("success = " + stat.success.get());
            logger.info("error   = " + stat.error.get());
            logger.info("duration= " + stat.getDuration());
            logger.info("================================");
        }
    }



}
