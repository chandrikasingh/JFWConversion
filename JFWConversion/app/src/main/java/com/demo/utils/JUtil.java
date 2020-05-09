package com.demo.utils;

import android.text.TextUtils;

public class JUtil {
    public static final String SPACE_DELIMITER = " ";
    public static final String NO_SPACE = "";
    public static final String COMMA_DELIMITER = ",";
    public static final String COLON_DELIMITER = ":";

    public static String intToTwoDigitHexStr(int intValue){
        String str = Integer.toHexString(intValue);
        return aStringToTwoDigitHexStr(str);
    }

    public static String formedIntelLine(String prefixHint, String currentLine, String calculatedCrc) {
        String startASCII = ":";
        currentLine = currentLine.replaceAll(SPACE_DELIMITER, NO_SPACE);
        currentLine = currentLine.replace(startASCII, NO_SPACE);

        StringBuffer strBuf = new StringBuffer();
        int len = currentLine.length();
        if (currentLine != null ) {
            if(len >= 2) {
                if(!TextUtils.isEmpty(prefixHint)){
                    strBuf.append(prefixHint);
                }
                String byteCount = currentLine.substring(0, 2);
                int byteCnt = Integer.parseInt(byteCount, 16);
                strBuf.append("\n Byte Count ").append(startASCII).append(byteCount);
            }
            if(len >= 6) {
                String intelAddr = currentLine.substring(2, 6);
                int address = Integer.parseInt(intelAddr, 16);
                strBuf.append("\n Address [ ").append(intelAddr).append(" ]");
            }
            if(len >= 8) {
                String intelType = currentLine.substring(6, 8);
                int type = Integer.parseInt(intelType, 16);
                strBuf.append("\n Line Type [ ").append(intelType).append(" ]");
            }
            if(len >=10) {
                //Note : Intel CRC 1Byte/ 2digit, Current Application calculate CRC send by Arg
               /* int crcByteIndex = (len - 2);
                  String xSum = currentLine.substring(crcByteIndex, len);
                  int crc = Integer.parseInt(xSum, 16);
                */
                String data = currentLine.substring(8, len);  //crcByteIndex
                if (!TextUtils.isEmpty(data)) {
                    strBuf.append("\n Data [ ").append(data).append(" ]");
                }
                if(!TextUtils.isEmpty(calculatedCrc))
                  strBuf.append("\n CRC [").append(calculatedCrc).append(" ]");
              }
            }
        return strBuf.toString();
    }


    public static String aStringToTwoDigitHexStr(String hexStr) {
        //Rmv Space if available
        hexStr = hexStr.replaceAll(SPACE_DELIMITER, NO_SPACE);

        String twoDigitsHexValue = "";
        if(hexStr.length() %2 == 0) {
              twoDigitsHexValue = separatedStr(hexStr);
        }else{
            twoDigitsHexValue = separateHexOddStr( hexStr);
        }
       return twoDigitsHexValue;
    }

    private static String separateHexOddStr(String str) {
        int individualDigitNo = 2;
        int strLen = str.length();
        StringBuffer strBuf = new StringBuffer();
        int evenStrRepeat = (strLen -1) /individualDigitNo ;
        for (int i=0; i< evenStrRepeat; i++){
            int initialSubIndex = i*individualDigitNo;
            String chunkHexStr = str.substring(initialSubIndex, initialSubIndex+individualDigitNo );
            strBuf.append(chunkHexStr).append(SPACE_DELIMITER); //String.format("|%#X|", 93); => 0X5D
        }
        String leftOverStr = str.substring(strLen -1, strLen );
        String leftOverStrWithLeadingZero = String.format("%0" + 1 + "d%s", 0, leftOverStr);
        strBuf.append(leftOverStrWithLeadingZero);
        return strBuf.toString();
    }

    private static String separatedStr(String hexStr) {
        StringBuilder strBuilder = new StringBuilder(hexStr);
        for(int i = 2; i < strBuilder.length(); i += 3){
            strBuilder.insert(i, SPACE_DELIMITER);
        }
        return strBuilder.toString();
    }

    public static String stringArrToTwoDigitHexStr(String[] hexStr) {
        StringBuffer strBuffer = new StringBuffer();
        for(int i = 0; i < hexStr.length; i ++){
            if(i != (hexStr.length -1)){
                strBuffer.append(SPACE_DELIMITER);
            }
            strBuffer.append(hexStr[i]);
        }
        return strBuffer.toString().trim();
    }

    public static String twoDigitHexStrToAstrLine(String str){
        String resultStr = "";
        if(!TextUtils.isEmpty(str)) {
            resultStr = str.replace(SPACE_DELIMITER, "");
        }
        return resultStr;
    }

    public static String hexByteArrToStr(byte[] buf, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            sb.append(String.format("%02X%s", buf[i], delimiter));
        }
        return sb.toString();
    }
}
