package com.myzr.allproducts.utils;

import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

/**
 * @author Areo
 * @description:
 * @date : 2020/4/5 21:05
 */
public class BleWriteData {
    private byte[]commonds;
    private BleWriteResponse bleWriteResponse;

    public BleWriteData(byte[] commonds, BleWriteResponse bleWriteResponse) {
        this.commonds = commonds;
        this.bleWriteResponse = bleWriteResponse;
    }

    public byte[] getCommonds() {
        return commonds;
    }

    public void setCommonds(byte[] commonds) {
        this.commonds = commonds;
    }

    public BleWriteResponse getBleWriteResponse() {
        return bleWriteResponse;
    }

    public void setBleWriteResponse(BleWriteResponse bleWriteResponse) {
        this.bleWriteResponse = bleWriteResponse;
    }
}
