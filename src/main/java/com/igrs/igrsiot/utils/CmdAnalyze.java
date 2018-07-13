package com.igrs.igrsiot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdAnalyze {
    public String doAnalyze(String type, byte[] data, int len) {
        String result = null;

        switch (type) {
            case "0":
                return result;
            case "1":
                if (data.equals(cmdRecv)) {

                }
                break;
            default:
                return result;
        }

        return result;
    }

    public byte[] doAnalyze(String type, String data) {
        byte[] result = new byte[0];

        switch (type) {
            case "0":
                return result;
            case "1":
                break;
            case "2":
                break;
            default:
                return result;
        }
        
        return result;
    }

    private byte[] cmdSend;
    private byte[] cmdRecv;

    private static final Logger logger = LoggerFactory.getLogger(CmdAnalyze.class);
}
