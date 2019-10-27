package timeClock;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class GetTime extends JFrame implements ActionListener
{

    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;
    public String weekDay;
    Timer timer;
    JColorChooserApplication colorSetInstance;

    public GetTime()
    {
        startTimer();
    }

    public void startTimer()
    {
        getCurrentTime();
        timer = new Timer(10, this);
        timer.start();

    }

    public void getCurrentTime()
    {
        long systemTime = System.currentTimeMillis();
        Date date = new Date(systemTime);
        int dayCount = date.getDay();
        year = date.getYear() + 1900;
        month = date.getMonth() + 1;
        day = date.getDate();
        hour = date.getHours();
        minute = date.getMinutes();
        second = date.getSeconds();
        String[] weekDaysStrings =
                { "星期天", "星期六", "星期五", "星期四", "星期三", "星期二", "星期一" };
        weekDay = weekDaysStrings[dayCount];
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(timer))
        {
            getCurrentTime();
        }
    }

    public String toString()
    {
        return "GetTime [year=" + year + ", month=" + month
                + ", day=" + day + ", hour=" + hour + ", minute="
                + minute + ", second=" + second + ", weekDay="
                + weekDay + ", timer=" + timer + "]";
    }

    public static void main(String[] args)
    {
        GetTime getTimeInstance = new GetTime();
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public int getHour()
    {
        return hour;
    }

    public void setHour(int hour)
    {
        this.hour = hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public void setMinute(int minute)
    {
        this.minute = minute;
    }

    public int getSecond()
    {
        return second;
    }

    public void setSecond(int second)
    {
        this.second = second;
    }

    public String getWeekDay()
    {
        return weekDay;
    }

    public void setWeekDay(String weekDay)
    {
        this.weekDay = weekDay;
    }
}
