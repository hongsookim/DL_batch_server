package com.herald.test;

import com.herald.job.reader.TSVFileHeader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TSVHeaderTest {

    @Test
    public void testFieldNameTmapPoi() {
        String[] headerNames = TSVFileHeader.getNames();
        String v1 = headerNames[0];

        assertEquals("PRD_ID", v1);
    }
}
