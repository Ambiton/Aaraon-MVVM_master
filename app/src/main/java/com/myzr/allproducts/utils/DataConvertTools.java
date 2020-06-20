package com.myzr.allproducts.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tts on 17/4/1.
 */

public class DataConvertTools {

    public static short[] byteToShortArray1(byte[] src) {

        int count = src.length >> 1;
        short[] dest = new short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) (src[i * 2] << 8 | src[2 * i + 1] & 0xff);
        }
        return dest;
    }

    public static byte[] shortToByteArray1(short[] src) {

        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2] = (byte) (src[i] >> 8);
            dest[i * 2 + 1] = (byte) (src[i] >> 0);
        }

        return dest;
    }


    public static Short[] byteToShortArrayLH(byte[] src) {

        int count = src.length >> 1;
        Short[] dest = new Short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) ((src[i * 2] & 0xff) | ((src[2 * i + 1] & 0xff) << 8));
        }
        return dest;
    }

    public static ArrayList<Short> getWaveWaveShorts(byte [] reciceDatas){
        ArrayList<Short> arrayList=new ArrayList<>();
        int length=reciceDatas.length;
        if(reciceDatas!=null&&reciceDatas.length==207){
            byte[]waves=new byte[length];
            for(int i=0;i<length-7;i++){
                waves[i]=reciceDatas[i+6];
            }
            Short[] wavesShort=byteToShortArrayHL(reciceDatas);
            arrayList=new ArrayList<Short>(Arrays.asList(wavesShort));
        }
        return arrayList;
    }
    /**
     * 字符串转换成十六进制字符串
     * @param String str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }
    public static Short[] byteToShortArrayHL(byte[] src) {

        int count = src.length >> 1;
        Short[] dest = new Short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) (((src[i * 2] & 0xff)<< 8) | (src[2 * i + 1] & 0xff) );
        }
        return dest;
    }

    public static byte[] shortToByteArray(short[] src) {

        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2] = (byte) (src[i]);
            dest[i * 2 + 1] = (byte) (src[i] >> 8);
        }

        return dest;
    }


}
