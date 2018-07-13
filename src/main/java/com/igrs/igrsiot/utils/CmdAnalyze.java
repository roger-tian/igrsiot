package com.igrs.igrsiot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CmdAnalyze {
    public static String doAnalyze(String type, char[] data, int len) {
        String result = null;

        switch (type) {
            case "0":
                return result;
            case "1":
                int i;
                for (i=0; i<cmdRecvChar1.length; i++) {
                    if (data.equals(cmdRecvChar1[i])) {
                        break;
                    }
                }
                result = cmdRecvStr1[i];
                if ((data[0] == 0x00) && (data[1] == 55) && (data[2] == 0xAA) && (data[3] == 0x3A)) { //set volume level
                    // todo
                }
                break;
            default:
                return result;
        }

        return result;
    }

    public static char[] doAnalyze(String type, String data) {
        char[] result = new char[5];

        switch (type) {
            case "0":
                return result;
            case "1":
                int i;
                for (i=0; i<cmdSendStr1.length; i++) {
                    if (cmdSendStr1[i].equals(data)) {
                        break;
                    }
                }
                result = cmdSendChar1[i];
                if ((result[0] == 0x55) && (result[1] == 0x00) && (result[2] == 0xAA) && (result[3] == 0x3A)) { //set volume level
                    // todo
                }
                break;
            case "2":
                break;
            default:
                return result;
        }

        return result;
    }

    private static char[][] cmdSendChar1 = {{0x55, 0x00, 0xEE, 0x50, 0xD1}, {0x55, 0x00, 0xEE, 0x00, 0xD5},
            {0x55, 0x00, 0xEE, 0x01, 0x56}, {0x55, 0x00, 0xEE, 0x02, 0x57}, {0x55, 0x00, 0xEE, 0x03, 0x58},
            {0x55, 0x00, 0xEE, 0x04, 0x59}, {0x55, 0x00, 0xEE, 0x00, 0x55}, {0x55, 0x00, 0xEE, 0x01, 0x69},
            {0x55, 0x00, 0xEE, 0x00, 0x6B}, {0x55, 0x00, 0xAA, 0x00, 0x6F}, {0x55, 0x00, 0xEE, 0x0A, 0x89},
            {0x55, 0x00, 0xEE, 0x14, 0x93}, {0x55, 0x00, 0xEE, 0x1E, 0x9D}, {0x55, 0x00, 0xAA, 0x28, 0xA7},
            {0x55, 0x00, 0xAA, 0x00, 0x63}, {0x55, 0x00, 0xEE, 0x26, 0x34}, {0x55, 0x00, 0xEE, 0x00, 0x5F},
            {0x55, 0x00, 0xFF, 0x11, 0xe6}, {0x55, 0x00, 0xFF, 0x09, 0xDE}, {0x55, 0x00, 0xff, 0x15, 0xEA},
            {0x55, 0x00, 0xFF, 0x08, 0xDD}, {0x55, 0x00, 0xFF, 0x00, 0xAD}, {0x55, 0x00, 0xff, 0x04, 0xD9},
            {0x55, 0x00, 0xFF, 0x0C, 0xE1}, {0x55, 0x00, 0xFF, 0x0A, 0x8B}, {0x55, 0x00, 0xFF, 0x0A, 0x8B},
            {0x55, 0x00, 0xFF, 0x0A, 0x8B}, {0x55, 0x00, 0xFF, 0x0A, 0x8B}, {0x55, 0x00, 0xFF, 0x0A, 0x8B},
            {0x55, 0x00, 0xFF, 0x0A, 0x8B}, {0x55, 0x00, 0xFF, 0x0A, 0x8B}, {0x55, 0x00, 0xFF, 0x0A, 0x8B},
            {0x55, 0x00, 0xAA, 0x14, 0x95}, {0x55, 0x00, 0xFF, 0x1E, 0x9F}, {0x55, 0x00, 0xFF, 0x1E, 0x9F},
            {0x55, 0x00, 0xFF, 0x1E, 0x9F}, {0x55, 0x00, 0xFF, 0x1E, 0x9F}, {0x55, 0x00, 0xAA, 0x28, 0xA7},
            {0x55, 0x00, 0xAA, 0x00, 0x63}, {0x55, 0x00, 0xAA, 0x3A, 0x00}, {0x55, 0x00, 0xFF, 0x00, 0xFF},
            {0x55, 0x00, 0xFF, 0x00, 0xE3}, {0x55, 0x00, 0xFF, 0x46, 0xC7}, {0x55, 0x00, 0xFF, 0x46, 0xC7},
            {0x55, 0x00, 0xFF, 0x46, 0x88}, {0x55, 0x00, 0xFF, 0x46, 0xD3}, {0x99, 0x23, 0x80, 0x01, 0x7F}};
    private static String[] cmdSendStr1 = {"{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_10:1}"};
    private static char[] cmdRecvChar1[] = {{0x00, 0x55, 0xEE, 0x50, 0xD1}, {0x00, 0x55, 0xEE, 0x00, 0xD5},
            {0x00, 0x55, 0xEE, 0x01, 0x56}, {0x00, 0x55, 0xEE, 0x02, 0x57}, {0x00, 0x55, 0xEE, 0x03, 0x58},
            {0x00, 0x55, 0xEE, 0x04, 0x59}, {0x00, 0x55, 0xEE, 0x00, 0x55}, {0x00, 0x55, 0xEE, 0x00, 0x69},
            {0x00, 0x55, 0xEE, 0x00, 0x6B}, {0x00, 0x55, 0xAA, 0x00, 0x6F}, {0x00, 0x55, 0xEE, 0x0A, 0x89},
            {0x00, 0x55, 0xEE, 0x14, 0x93}, {0x00, 0x55, 0xEE, 0x1E, 0x9D}, {0x00, 0x55, 0xAA, 0x28, 0xA7},
            {0x00, 0x55, 0xAA, 0x00, 0x63}, {0x00, 0x55, 0xEE, 0x26, 0x34}, {0x00, 0x55, 0xEE, 0x00, 0x5F},
            {0x00, 0x55, 0xFF, 0x11, 0xE6}, {0x00, 0x55, 0xFF, 0x09, 0xDE}, {0x00, 0x55, 0xFF, 0x15, 0xEA},
            {0x00, 0x55, 0xFF, 0x08, 0xDD}, {0x00, 0x55, 0xFF, 0x00, 0xAD}, {0x00, 0x55, 0xFF, 0x04, 0xD9},
            {0x00, 0x55, 0xFF, 0x0C, 0xE1}, {0x00, 0x55, 0xFF, 0x11, 0xE6}, {0x00, 0x55, 0xFF, 0x09, 0xDE},
            {0x00, 0x55, 0xFF, 0x15, 0xEA}, {0x00, 0x55, 0xFF, 0x08, 0xDD}, {0x00, 0x55, 0xFF, 0x00, 0xAD},
            {0x00, 0x55, 0xFF, 0x04, 0xD9}, {0x00, 0x55, 0xFF, 0x0C, 0xE1}, {0x00, 0x55, 0xFF, 0x0F, 0xE4},
            {0x00, 0x55, 0xAA, 0x3A, 0x00}, {0x00, 0x55, 0xFF, 0x1E, 0x9F}, {0x00, 0x55, 0xFF, 0x1E, 0x8F},
            {0x00, 0x55, 0xFF, 0x1E, 0x7F}, {0x00, 0x55, 0xFF, 0x1E, 0x6F}, {0x00, 0x55, 0xAA, 0x28, 0xA7},
            {0x00, 0x55, 0xAA, 0x00, 0x63}, {0x00, 0x55, 0xAA, 0x3A, 0x00}, {0x00, 0x55, 0xFF, 0x00, 0xFF},
            {0x00, 0x55, 0xFF, 0x00, 0xE3}, {0x00, 0x55, 0xFF, 0x00, 0xFF}, {0x00, 0x55, 0xFF, 0x00, 0xE3},
            {0x00, 0x55, 0xFF, 0x46, 0x88}, {0x00, 0x55, 0xFF, 0x46, 0xD3}, {0xAA}};
    private static String cmdRecvStr1[] = {"{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}",
            "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_xx:x}", "{ch_10:1}"};

    private static final Logger logger = LoggerFactory.getLogger(CmdAnalyze.class);
}
