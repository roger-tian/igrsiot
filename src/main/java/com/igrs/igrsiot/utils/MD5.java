package com.igrs.igrsiot.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5 {
    public static String md5(String text) {
        String encodeStr = DigestUtils.md5Hex(text);
        logger.debug("MD5: {}", encodeStr);

        return encodeStr;
    }

    public static boolean verify(String text, String md5) {
        String md5Text = md5(text);
        if(md5Text.equalsIgnoreCase(md5)) {
            return true;
        }

        return false;
    }

    private static final Logger logger = LoggerFactory.getLogger(MD5.class);
}
