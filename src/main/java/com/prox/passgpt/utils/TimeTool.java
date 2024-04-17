package com.prox.passgpt.utils;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeTool {
    public static final long TIME_DAY = 1000 * 60 * 60 * 24;
    public static long calculatorMilliSecondsToTimeOfDay(LocalTime time) {
        // Lấy thời điểm hiện tại
        Calendar now = Calendar.getInstance();

        // Đặt thời gian khởi động vào 10:00 AM (thay đổi theo nhu cầu)
        Calendar scheduledTime = Calendar.getInstance();
        scheduledTime.set(Calendar.HOUR_OF_DAY, time.getHour());
        scheduledTime.set(Calendar.MINUTE, time.getMinute());
        scheduledTime.set(Calendar.SECOND, time.getMinute());

        // Tính thời gian trễ đến thời điểm khởi động
        long initialDelay = scheduledTime.getTimeInMillis() - now.getTimeInMillis();
        if (initialDelay < 0) {
            // Nếu thời gian đã trôi qua thì thêm 1 ngày (hoặc tùy ý) để đặt lịch cho ngày mai
            initialDelay += 24 * 60 * 60 * 1000; // 24 giờ
        }
        return initialDelay;
    }

    public static void setTimeGM7() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
    }


}

