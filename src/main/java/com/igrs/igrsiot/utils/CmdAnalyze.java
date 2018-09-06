package com.igrs.igrsiot.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class CmdAnalyze {
//    public static JSONObject decode(JSONObject jsonObject, JSONObject result, String data) throws UnsupportedEncodingException {
    public static JSONObject decode(JSONObject jsonObject, String data) throws UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        String type = jsonObject.getString("type");
        String ctype = jsonObject.getString("ctype");

        switch (type) {
            case "machine":
                switch (ctype) {
                    case "0":   // no need to analyze
                        break;
                    case "1":
                        break;
                    case "2":
                        byte[] cmd = data.getBytes(CharEncoding.ISO_8859_1);
                        if (cmd.length == 8) {  // query response
                            String onOff = String.valueOf(cmd[2]);
                            String sigSource;
                            switch (cmd[3]) {
                                case 0x0b:
                                    sigSource = "1";
                                    break;
                                case 0x0e:
                                    sigSource = "2";
                                    break;
                                case 0x0f:
                                    sigSource = "3";
                                    break;
                                case 0x11:
                                    sigSource = "4";
                                    break;
                                default:
                                    sigSource = "1";
                                    break;
                            }
                            String volume = String.valueOf(cmd[4]);
                            String device = jsonObject.getString("id");
                            result.put("cmdType", "query");
                            result.put("device", device);
                            result.put("switch", onOff);
                            result.put("sigSource", sigSource);
                            result.put("volume", volume);
                        }
                        break;
                    default:
                        break;
                }
            default:
                break;
        }

        return result;
    }

    public static String encode(JSONObject jsonObject, String cmdType, String param) throws UnsupportedEncodingException {
        String result = null;
        String type = jsonObject.getString("type");
        String ctype = jsonObject.getString("ctype");
        String cchannel = jsonObject.getString("cchannel");

        switch (type) {
            case "machine":
                switch (ctype) {
                    case "0":   //
                        if ((cchannel.length() != 0) && ((param != null) && (param.length() != 0))) {
                            result = "{ch_" + jsonObject.getString("cchannel") + ":" + param + "}";
                        }
                        break;
                    case "1":   // 75 inch machine
                        switch (cmdType) {
                            case "swtich":
                                if (param.equals("0")) {    // power off
                                    byte[] cmd = {(byte) 0x99, 0x23, (byte) 0x80, 0x01, (byte) 0x7f, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("1")) { // power on
                                    byte[] cmd = {(byte) 0x55, 0x00, (byte) 0xff, 0x46, (byte) 0xd3};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            case "sigSource":
                                if (param.equals("1")) {    // home
                                    // todo
                                    byte[] cmd = {0x55, 0x00, (byte) 0xff, 0x15, (byte) 0xea};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("2")) { // hdmi1
                                    byte[] cmd = {0x55, 0x00, (byte) 0xff, 0x11, (byte) 0xe6};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("3")) { // hdmi2
                                    byte[] cmd = {0x55, 0x00, (byte) 0xff, 0x09, (byte) 0xde};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("4")) { // computer
                                    // todo
                                    byte[] cmd = {0x55, 0x00, (byte) 0xff, 0x00, (byte) 0xad};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            case "volume":
                                if (param.equals("0")) {    // decrease volume
                                    byte[] cmd = {0x55, 0x00, (byte) 0xaa, 0x28, (byte) 0xa7};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("1")) { // increase volume
                                    byte[] cmd = {0x55, 0x00, (byte) 0xaa, 0x00, 0x63};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            case "query":
                                {
                                    byte[] cmd = {0x55, 0x00, (byte) 0xff, 0x46, (byte) 0xc7};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    case "2":   // 65 inch capacitance machine
                        switch (cmdType) {
                            case "switch":
                                if (param.equals("0")) {    // power off
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x01, 0x01, (byte) 0xfe, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("1")) {    // power on
                                    byte[] cmd = {(byte) 0x99, 0x23, (byte) 0x80, 0x01, 0x7f, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            case "sigSource":
                                if (param.equals("1")) {    // home
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x00, 0x01, (byte) 0xff, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("2")) { // hdmi1
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x0e, 0x01, (byte) 0xf1, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("3")) { // hdmi2
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x0f, 0x01, (byte) 0xf0, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("4")) { // computer
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x00, 0x01, (byte) 0xff, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            case "volume":
                                if (param.equals("0")) {    // decrease volume
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x18, 0x01, (byte) 0xe7, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                } else if (param.equals("1")) { // increase volume
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x17, 0x01, (byte) 0xe8, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            case "mute":
                                {
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x02, 0x01, (byte) 0xfd, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            case "query":
                                {
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x28, 0x01, (byte) 0xd7, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            case "menu":
                                {
                                    byte[] cmd = {(byte) 0x99, 0x23, 0x12, 0x01, (byte) 0xed, (byte) 0xaa};
                                    result = new String(cmd, CharEncoding.ISO_8859_1);
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        return result;
    }

    private static final Logger logger = LoggerFactory.getLogger(CmdAnalyze.class);
}
