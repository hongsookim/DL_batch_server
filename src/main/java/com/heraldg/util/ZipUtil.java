package com.heraldg.util;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;

public class ZipUtil {

    public static File unzip(File zip, File toDir) throws IOException, ArchiveException {
        final InputStream is = new FileInputStream(zip);
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
        ZipArchiveEntry entry = (ZipArchiveEntry)in.getNextEntry();
        File unzipped =new File(toDir, entry.getName());
        OutputStream out = new FileOutputStream(unzipped);
        IOUtils.copy(in, out);
        out.close();
        in.close();
        return unzipped;
    }


}
