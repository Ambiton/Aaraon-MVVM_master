package com.goldze.mvvmhabit.entity;

import android.net.Uri;

import com.goldze.mvvmhabit.R;

/**
 * @author Areo
 * @description:
 * @date : 2020/5/16 22:45
 */
public class StyleResEntity {
    public static String FILENAME_BACKGROUND = "mainbackground";
    public static String FILENAME_PILLOW = "pillow";
    public static String FILENAME_STOP = "stop";
    public static String FILENAME_WARMPILLOW = "warmpillow";
    public static String FILENAME_PAUSE = "pause";
    public static String FILENAME_ROATION_POS_HIGH_GIF = "roation_pos_high_normal";
    public static String FILENAME_ROATION_POS_MID_GIF = "roation_pos_mid_normal";
    public static String FILENAME_ROATION_POS_LOW_GIF = "roation_pos_low_normal";
    public static String FILENAME_ROATION_REV_HIGH_GIF = "roation_rev_high_normal";
    public static String FILENAME_ROATION_REV_MID_GIF = "roation_rev_low_normal";
    public static String FILENAME_ROATION_REV_LOW_GIF = "roation_rev_mid_normal";
    public static String FILENAME_PRODUCT_LOGO = "logo";
    private Uri pillowUri;
    private Uri stopUri;
    private Uri warmpillowUri;
    private Uri pauseUri;

    private Uri roationPosHighNormalUri;
    private Uri roationPosMidNormalUri;
    private Uri roationPosLowNormalUri;

    private Uri roationRevHighNormalUri;
    private Uri roationRevMidNormalUri;
    private Uri roationRevLowNormalUri;

    private Uri logoUri;
    public Object getLogoUri() {
        return logoUri == null ? R.mipmap.applauncher : logoUri;
    }

    public void setLogoUri(Uri logoUri) {
        this.logoUri = logoUri;
    }


    public Object getBackgroundUri() {
        return backgroundUri == null ? R.mipmap.mainbackground : backgroundUri;
    }

    public void setBackgroundUri(Uri backgroundUri) {
        this.backgroundUri = backgroundUri;
    }

    private Uri backgroundUri;

    private Object hit;

    public Object getPillowUri() {
        return pillowUri == null ? R.drawable.pillow : pillowUri;
    }

    public void setPillowUri(Uri pillowUri) {
        this.pillowUri = pillowUri;
    }

    public Object getStopUri() {
        return stopUri == null ? R.drawable.stop : stopUri;
    }

    public void setStopUri(Uri stopUrih) {
        this.stopUri = stopUrih;
    }

    public Object getWarmpillowUri() {
        return warmpillowUri == null ? R.mipmap.warmpillow : warmpillowUri;
    }

    public void setWarmpillowUri(Uri warmpillowUri) {
        this.warmpillowUri = warmpillowUri;
    }

    public Object getPauseUri() {
        return pauseUri == null ? R.drawable.pause : pauseUri;
    }

    public void setPauseUri(Uri pauseUri) {
        this.pauseUri = pauseUri;
    }

    public Object getRoationPosHighNormalUri() {
        return roationPosHighNormalUri == null ? R.drawable.roation_pos_high_normal : roationPosHighNormalUri;
    }

    public void setRoationPosHighNormalUri(Uri roationPosHighNormalUri) {
        this.roationPosHighNormalUri = roationPosHighNormalUri;
    }

    public Object getRoationPosMidNormalUri() {
        return roationPosMidNormalUri == null ? R.drawable.roation_pos_mid_normal : roationPosMidNormalUri;
    }

    public void setRoationPosMidNormalUri(Uri roationPosMidNormalUri) {
        this.roationPosMidNormalUri = roationPosMidNormalUri;
    }

    public Object getRoationPosLowNormalUri() {
        return roationPosLowNormalUri == null ? R.drawable.roation_pos_low_normal : roationPosLowNormalUri;
    }

    public void setRoationPosLowNormalUri(Uri roationPosLowNormalUri) {
        this.roationPosLowNormalUri = roationPosLowNormalUri;
    }

    public Object getRoationRevHighNormalUri() {
        return roationRevHighNormalUri == null ? R.drawable.roation_rev_high_normal : roationRevHighNormalUri;
    }

    public void setRoationRevHighNormalUri(Uri roationRevHighNormalUri) {
        this.roationRevHighNormalUri = roationRevHighNormalUri;
    }

    public Object getRoationRevMidNormalUri() {
        return roationRevMidNormalUri == null ? R.drawable.roation_rev_mid_normal : roationRevMidNormalUri;
    }

    public void setRoationRevMidNormalUri(Uri roationRevMidNormalUri) {
        this.roationRevMidNormalUri = roationRevMidNormalUri;
    }

    public Object getRoationRevLowNormalUri() {
        return roationRevLowNormalUri == null ? R.drawable.roation_rev_low_normal : roationRevLowNormalUri;
    }

    public void setRoationRevLowNormalUri(Uri roationRevLowNormalUri) {
        this.roationRevLowNormalUri = roationRevLowNormalUri;
    }

    public StyleResEntity() {
    }
}
