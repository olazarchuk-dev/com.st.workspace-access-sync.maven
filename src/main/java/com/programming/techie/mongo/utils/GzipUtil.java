package com.programming.techie.mongo.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;

public class GzipUtil {

    public static final String UTF_8 = "UTF-8";
    public static final String ON_GZIP = "onGzip";

    public static String toCompress(String data, String charsetName)
            throws IOException {
        var out = new ByteArrayOutputStream();
        var gzip = new GZIPOutputStream(out);
        gzip.write(data.getBytes(charsetName));
        gzip.close();
        var compress = getEncoder().encodeToString(out.toByteArray());

        return compress;
    }

    public static String toDecompress(String data, String charsetName)
            throws IOException {
        var buf = getDecoder().decode(data);
        var gzip = new GZIPInputStream(new ByteArrayInputStream(buf));
        var br = new BufferedReader(new InputStreamReader(gzip, charsetName));
        var decompress = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            decompress.append(line);
        }

        return decompress.toString();
    }
}
