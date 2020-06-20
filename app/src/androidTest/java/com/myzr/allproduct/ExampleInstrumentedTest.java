package com.myzr.allproduct;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.myzr.allproduct.utils.RxRegTool;
import com.vondear.rxtool.RxDataTool;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.goldze.mvvmhabit", appContext.getPackageName());
        boolean istrue= RxRegTool.isMobileSimple("16656788765");
        boolean istrue2= RxRegTool.isMobileExact("16656788765");
        boolean istrue3= RxRegTool.isMobile("16656788765");
        int a=0;
    }
}
