package com.myzr.allproducts.utils.lisatools;

import android.text.TextUtils;

import com.tamsiree.rxtool.RxLogTool;

import java.util.ArrayList;
import java.util.HashMap;

public class LisaTools {
    private static LisaTools instance = new LisaTools();
    private static String TAG = "LisaTools";
    private static final int ORDER_TOP = 0;
    private static final int ORDER_BOTTOM = 1;
    private static final int ORDER_INT = 2;
    private static String TOP = "top";
    private static String BOTTOM = "top";
    private static String INT = "int";
    private static String A1 = "a1";
    private static String A8 = "a8";
    private static String COM = "com";
    private static String AVA = "ava";
    private static String CCTC = "cctc";
    private static String OPC = "opc";
    private static String SPLIT_ORDERSTR = "_";
    private static String SPLIT_DOUHAO = ",";
    private static String RESULT_ENOUGH = "物料充足,Donnot worry";
    private static String RESULT_DATA_ERR = "数据异常无法计算";
    private static String[] ORDER_FIRST = new String[]{A1, A1, AVA};
    private static String[] ORDER_SECOND = new String[]{A8, A8, AVA};
    private static String[] ORDER_THIRDT = new String[]{COM, COM, COM};
    private static String[] ORDER_FORTH = new String[]{COM, COM, CCTC};
    private static String[] ORDER_FIFTH = new String[]{OPC, OPC, CCTC};
    private static String[] ORDER_SIXTH = new String[]{OPC, OPC, AVA};
    private static String[] ORDER_SEVENTH = new String[]{A8, A8, COM};
    private static ArrayList<String[]> ORDERS_DEFAULT = new ArrayList<>();
    private ArrayList<String[]> orderArrays = new ArrayList<>();//记录tops,bottom,int 的安排顺序
    private int totalDats = 30;//总共需要记录的天数
    private static HashMap<String, LisaEntry> mapTops = new HashMap<>();
    private static HashMap<String, LisaEntry> mapBottoms = new HashMap<>();
    private static HashMap<String, LisaEntry> mapInts = new HashMap<>();
    private int moredDatasForEveryday = 20;//准备多预留的空间

    static {
        ORDERS_DEFAULT.add(ORDER_FIRST);
        ORDERS_DEFAULT.add(ORDER_SECOND);
        ORDERS_DEFAULT.add(ORDER_THIRDT);
        ORDERS_DEFAULT.add(ORDER_FORTH);
        ORDERS_DEFAULT.add(ORDER_FIFTH);
        ORDERS_DEFAULT.add(ORDER_SIXTH);
        ORDERS_DEFAULT.add(ORDER_SEVENTH);
        instance.orderArrays = ORDERS_DEFAULT;
    }

    public static LisaTools getInstance() {
        return instance;
    }

    /**
     * 以 _隔开[top,bottom,int],[top1,bottom1,int1]...
     *
     * @param order
     */
    public void initOrder(String order) {
        if (TextUtils.isEmpty(order)) {
            initOrder(ORDER_FIRST);
            return;
        }
        orderArrays.clear();
        String[] allOrders = order.split(SPLIT_ORDERSTR);
        if (allOrders.length == 0) {
            initOrder(ORDER_FIRST);
            return;
        }
        for (String orders : allOrders) {
            String[] detailOrder = orders.split(SPLIT_DOUHAO);
            if (detailOrder.length != ORDER_FIRST.length) {
                initOrder(ORDER_FIRST);
                break;
            }
            orderArrays.add(detailOrder);
        }
    }

    public void initAllDays(int days) {
        totalDats = days;
    }

    public void initOrder(String[] orderFirst) {
        orderArrays.clear();
        orderArrays.add(orderFirst);
        for (String[] temps : ORDERS_DEFAULT) {
            if (!temps.equals(orderFirst)) {
                orderArrays.add(temps);
            }
        }
    }

    public void initTotalDays() {

    }

    public void initTopA1(String numsArrayStr) {
        initTop(A1, numsArrayStr);
    }

    public void initTopA8(String numsArrayStr) {
        initTop(A8, numsArrayStr);
    }

    public void initTopCom(String numsArrayStr) {
        initTop(COM, numsArrayStr);
    }

    public void initTopOpc(String numsArrayStr) {
        initTop(OPC, numsArrayStr);
    }

    private void initTop(String name, String numsArrayStr) {
        if (TextUtils.isEmpty(numsArrayStr)) {
            return;
        }
        String dataStrs[] = numsArrayStr.split(",");
        int len = dataStrs.length;
        if (len < totalDats) {
            return;
        }
        int dataInts[] = new int[len];
        int index = 0;
        for (String t : dataStrs) {
            dataInts[index] = Integer.parseInt(t);
            index++;
        }
        LisaEntry lisaEntry = new LisaEntry(TOP + name, dataInts, 0,0);
        mapTops.put(name, lisaEntry);
    }

    public void initBottomA1(String numsArrayStr) {
        initBottom(A1, numsArrayStr);
    }

    public void initBottomA8(String numsArrayStr) {
        initBottom(A8, numsArrayStr);
    }

    public void initBottomCom(String numsArrayStr) {
        initBottom(COM, numsArrayStr);
    }

    public void initBottomOpc(String numsArrayStr) {
        initBottom(OPC, numsArrayStr);
    }

    private void initBottom(String name, String numsArrayStr) {
        if (TextUtils.isEmpty(numsArrayStr)) {
            return;
        }
        String dataStrs[] = numsArrayStr.split(",");
        int len = dataStrs.length;
        if (len < totalDats) {
            return;
        }
        int dataInts[] = new int[len];
        int index = 0;
        for (String t : dataStrs) {
            dataInts[index] = Integer.parseInt(t);
            index++;
        }
        LisaEntry lisaEntry = new LisaEntry(BOTTOM + name, dataInts, 0,0);
        mapBottoms.put(name, lisaEntry);
    }

    public void initIntAva(String numsArrayStr) {
        initINT(AVA, numsArrayStr);
    }

    public void initIntCom(String numsArrayStr) {
        initINT(COM, numsArrayStr);
    }

    public void initIntCctv(String numsArrayStr) {
        initINT(CCTC, numsArrayStr);
    }

    private void initINT(String name, String numsArrayStr) {
        if (TextUtils.isEmpty(numsArrayStr)) {
            return;
        }
        String dataStrs[] = numsArrayStr.split(",");
        int len = dataStrs.length;
        if (len < totalDats) {
            return;
        }
        int dataInts[] = new int[len];
        int index = 0;
        for (String t : dataStrs) {
            dataInts[index] = Integer.parseInt(t);
            index++;
        }
        LisaEntry lisaEntry = new LisaEntry(INT + name, dataInts, 0,0);
        mapInts.put(name, lisaEntry);
    }

    public void initMoreDatasForEveryDay(int nums) {
        moredDatasForEveryday = nums;
    }

    public String judgeRules(String completeDataEveryDay) {
        String result = RESULT_ENOUGH;
        String[] completeDatasStr = completeDataEveryDay.split(SPLIT_DOUHAO);
        if (completeDatasStr.length < totalDats) {
            return "输入的天数不足" + totalDats + "天，请确认后再输入";
        }
        int[] needCompleteData = new int[totalDats];
        for (int i = 0; i < completeDatasStr.length; i++) {
            needCompleteData[i] = Integer.parseInt(completeDatasStr[i]);
        }
        int dayIndex = 0;
        while (dayIndex < totalDats) {
            int enoughData = needCompleteData[dayIndex] + moredDatasForEveryday;
            int hasCompleted = 0;
            boolean isComplete = false;
            for (int i = 0; i < orderArrays.size(); i++) {
                String[] orderArray = orderArrays.get(i);
                if (orderArray.length == ORDER_FIRST.length) {
                    LisaEntry topEntry = mapTops.get(orderArray[ORDER_TOP]);
                    LisaEntry bottomEntry = mapBottoms.get(orderArray[ORDER_BOTTOM]);
                    LisaEntry intsEntry = mapInts.get(orderArray[ORDER_INT]);
                    if (topEntry == null || bottomEntry == null || intsEntry == null) {
                        RxLogTool.e(TAG, "topEntry==null||bottomEntry==null||intsEntry==null");
                        result = RESULT_DATA_ERR;
                        break;
                    }
                    int topLeftToday = 0;
                    int bottomLeftToday = 0;
                    int insLeftToday = 0;
                    if(topEntry.getIndexDay()==dayIndex){
                        topLeftToday = topEntry.getEveryDayNums()[dayIndex];
                    }
                    if(bottomEntry.getIndexDay()==dayIndex){
                        bottomLeftToday = bottomEntry.getEveryDayNums()[dayIndex];
                    }
                    if(intsEntry.getIndexDay()==dayIndex){
                        insLeftToday = intsEntry.getEveryDayNums()[dayIndex];
                    }
                    int topLeft = topEntry.getLeftNumsToToday() + topLeftToday;
                    int bottomLeft = bottomEntry.getLeftNumsToToday()+bottomLeftToday;
                    int insLeft = intsEntry.getLeftNumsToToday()+insLeftToday;
                    int maxCoup = getMaxCoup(topLeft, bottomLeft, insLeft);
                    if (hasCompleted + maxCoup >= enoughData) {
                        maxCoup = enoughData - hasCompleted;
                        hasCompleted = enoughData;
                        isComplete = true;
                    } else {
                        hasCompleted = hasCompleted + maxCoup;
                    }

                    topEntry.setLeftNumsToToday(topLeft - maxCoup);
                    topEntry.setIndexDay(dayIndex+1);
                    bottomEntry.setLeftNumsToToday(bottomLeft - maxCoup);
                    bottomEntry.setIndexDay(dayIndex+1);
                    intsEntry.setLeftNumsToToday(insLeft - maxCoup);
                    intsEntry.setIndexDay(dayIndex+1);
                    mapTops.put(orderArray[ORDER_TOP], topEntry);
                    mapBottoms.put(orderArray[ORDER_BOTTOM], bottomEntry);
                    mapInts.put(orderArray[ORDER_INT], intsEntry);
                    if (isComplete) {
                        break;
                    }
                    if (i == orderArrays.size() - 1) {
                        result = "警告，供货不足，正常运行到第" + (dayIndex+1) + "天，该天需要"+enoughData+"片，完成了"+hasCompleted+"片,剩余"+(enoughData-hasCompleted)+"片未完成,\n运行到的方案为：" + orderArray[ORDER_TOP] + "," + orderArray[ORDER_BOTTOM] + "," + orderArray[ORDER_INT] + ";\r\n\n ";
                        for (int index = 0; index < orderArrays.size(); index++) {
                            String[] orderArrayForStr = orderArrays.get(index);
                            LisaEntry topEntryForStr = mapTops.get(orderArrayForStr[ORDER_TOP]);
                            LisaEntry bottomEntryForStr = mapBottoms.get(orderArrayForStr[ORDER_BOTTOM]);
                            LisaEntry intsEntryForStr = mapInts.get(orderArrayForStr[ORDER_INT]);
                            String tem= "****方案：" + orderArrayForStr[ORDER_TOP] + "," + orderArrayForStr[ORDER_BOTTOM] + "," + orderArrayForStr[ORDER_INT] + "剩余情况如下:\r\n "+"  top--> " + orderArrayForStr[ORDER_TOP] + "剩余" + topEntryForStr.getLeftNumsToToday() + "片;\n " +
                                    "  bottom--> " + orderArrayForStr[ORDER_BOTTOM] + "剩余" + bottomEntryForStr.getLeftNumsToToday() + "片;\n " +
                                    "  int--> " + orderArrayForStr[ORDER_INT] + "剩余" + intsEntryForStr.getLeftNumsToToday() + "片;\n\n";
                            result=result+tem;
                        }
                    }
                } else {
                    RxLogTool.e(TAG, "orderArray.length is err ,is " + orderArray.length);
                }
            }
            if (!isComplete) {
                break;
            }
            dayIndex++;
        }
        return result;
    }

    private int getMaxCoup(int top, int bottom, int ints) {
        int result = top;
        if (result > bottom) {
            result = bottom;
        }
        if (result > ints) {
            result = ints;
        }
        return result;
    }
}
