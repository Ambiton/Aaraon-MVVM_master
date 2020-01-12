package com.goldze.mvvmhabit;

import android.util.Log;

import com.inuker.bluetooth.library.utils.ByteUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);
        byte[]strs=ByteUtils.stringToBytes("ATSTART");
        Log.e("yuanjian",strs.length+" str size");
    }
}