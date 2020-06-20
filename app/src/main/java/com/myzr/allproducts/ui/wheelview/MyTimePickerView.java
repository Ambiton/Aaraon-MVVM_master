package com.myzr.allproducts.ui.wheelview;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.ISelectTimeCallback;
import com.bigkoo.pickerview.view.WheelTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择器
 * Created by Sai on 15/11/22.
 * Updated by XiaoSong on 2017-2-22.
 */
public class MyTimePickerView {

    private WheelTime wheelTime; //自定义控件
    private PickerOptions pickerOptions;

    public MyTimePickerView(PickerOptions pickerOptions, View timePickerView) {
        this.pickerOptions=pickerOptions;
        initView(pickerOptions.context,timePickerView);
    }

    private void initView(Context context,View timePickerView) {
        // 时间转轮 自定义控件
//        if(){
//
//        }
//        timePickerView.setBackgroundColor(pickerOptions.bgColorWheel);

        initWheelTime(timePickerView);
    }

    private void initWheelTime(View timePickerView) {
        wheelTime = new WheelTime(timePickerView, pickerOptions.type, pickerOptions.textGravity, pickerOptions.textSizeContent);
        if (pickerOptions.timeSelectChangeListener != null) {
            wheelTime.setSelectChangeCallback(new ISelectTimeCallback() {
                @Override
                public void onTimeSelectChanged() {
                    try {
                        Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                        pickerOptions.timeSelectChangeListener.onTimeSelectChanged(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        wheelTime.setLunarMode(pickerOptions.isLunarCalendar);

        if (pickerOptions.startYear != 0 && pickerOptions.endYear != 0
                && pickerOptions.startYear <= pickerOptions.endYear) {
            setRange();
        }

        //若手动设置了时间范围限制
        if (pickerOptions.startDate != null && pickerOptions.endDate != null) {
            if (pickerOptions.startDate.getTimeInMillis() > pickerOptions.endDate.getTimeInMillis()) {
                throw new IllegalArgumentException("startDate can't be later than endDate");
            } else {
                setRangDate();
            }
        } else if (pickerOptions.startDate != null) {
            if (pickerOptions.startDate.get(Calendar.YEAR) < 1900) {
                throw new IllegalArgumentException("The startDate can not as early as 1900");
            } else {
                setRangDate();
            }
        } else if (pickerOptions.endDate != null) {
            if (pickerOptions.endDate.get(Calendar.YEAR) > 2100) {
                throw new IllegalArgumentException("The endDate should not be later than 2100");
            } else {
                setRangDate();
            }
        } else {//没有设置时间范围限制，则会使用默认范围。
            setRangDate();
        }

        setTime();
        wheelTime.setLabels(pickerOptions.label_year, pickerOptions.label_month, pickerOptions.label_day
                , pickerOptions.label_hours, pickerOptions.label_minutes, pickerOptions.label_seconds);
        wheelTime.setTextXOffset(pickerOptions.x_offset_year, pickerOptions.x_offset_month, pickerOptions.x_offset_day,
                pickerOptions.x_offset_hours, pickerOptions.x_offset_minutes, pickerOptions.x_offset_seconds);
        wheelTime.setItemsVisible(pickerOptions.itemsVisibleCount);
        wheelTime.setAlphaGradient(pickerOptions.isAlphaGradient);
        wheelTime.setCyclic(pickerOptions.cyclic);
        wheelTime.setDividerColor(pickerOptions.dividerColor);
        wheelTime.setDividerType(pickerOptions.dividerType);
        wheelTime.setLineSpacingMultiplier(pickerOptions.lineSpacingMultiplier);
        wheelTime.setTextColorOut(pickerOptions.textColorOut);
        wheelTime.setTextColorCenter(pickerOptions.textColorCenter);
        wheelTime.isCenterLabel(pickerOptions.isCenterLabel);
    }


    /**
     * 设置默认时间
     */
    public void setDate(Calendar date) {
        pickerOptions.date = date;
        setTime();
    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRange() {
        wheelTime.setStartYear(pickerOptions.startYear);
        wheelTime.setEndYear(pickerOptions.endYear);

    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRangDate() {
        wheelTime.setRangDate(pickerOptions.startDate, pickerOptions.endDate);
        initDefaultSelectedDate();
    }

    private void initDefaultSelectedDate() {
        //如果手动设置了时间范围
        if (pickerOptions.startDate != null && pickerOptions.endDate != null) {
            //若默认时间未设置，或者设置的默认时间越界了，则设置默认选中时间为开始时间。
            if (pickerOptions.date == null || pickerOptions.date.getTimeInMillis() < pickerOptions.startDate.getTimeInMillis()
                    || pickerOptions.date.getTimeInMillis() > pickerOptions.endDate.getTimeInMillis()) {
                pickerOptions.date = pickerOptions.startDate;
            }
        } else if (pickerOptions.startDate != null) {
            //没有设置默认选中时间,那就拿开始时间当默认时间
            pickerOptions.date = pickerOptions.startDate;
        } else if (pickerOptions.endDate != null) {
            pickerOptions.date = pickerOptions.endDate;
        }
    }

    /**
     * 设置选中时间,默认选中当前时间
     */
    private void setTime() {
        int year, month, day, hours, minute, seconds;
        Calendar calendar = Calendar.getInstance();

        if (pickerOptions.date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
        } else {
            year = pickerOptions.date.get(Calendar.YEAR);
            month = pickerOptions.date.get(Calendar.MONTH);
            day = pickerOptions.date.get(Calendar.DAY_OF_MONTH);
            hours = pickerOptions.date.get(Calendar.HOUR_OF_DAY);
            minute = pickerOptions.date.get(Calendar.MINUTE);
            seconds = pickerOptions.date.get(Calendar.SECOND);
        }

        wheelTime.setPicker(year, month, day, hours, minute, seconds);
    }


    public Date returnData() {
        Date date=new Date();
        if (pickerOptions.timeSelectListener != null) {
            try {
                date = WheelTime.dateFormat.parse(wheelTime.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return  date;
    }

    /**
     * 目前暂时只支持设置1900 - 2100年
     *
     * @param lunar 农历的开关
     */
    public void setLunarCalendar(boolean lunar) {
        try {
            int year, month, day, hours, minute, seconds;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(WheelTime.dateFormat.parse(wheelTime.getTime()));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);

            wheelTime.setLunarMode(lunar);
            wheelTime.setLabels(pickerOptions.label_year, pickerOptions.label_month, pickerOptions.label_day,
                    pickerOptions.label_hours, pickerOptions.label_minutes, pickerOptions.label_seconds);
            wheelTime.setPicker(year, month, day, hours, minute, seconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isLunarCalendar() {
        return wheelTime.isLunarMode();
    }

}
