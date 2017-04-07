package com.herald.test;

import com.herald.job.reader.CSVFileHeader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CSVHeaderTest {

    @Test
    public void testFieldNameTmapPoi() {
        String[] headerNames = CSVFileHeader.getNames();
        String v1 = headerNames[0];

        assertEquals("PRD_NO", v1);
    }
}
