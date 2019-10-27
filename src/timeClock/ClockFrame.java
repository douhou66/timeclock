package timeClock;

import mysqlConnect.MysqlDbConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Vector;

public class ClockFrame extends JFrame implements Runnable, ActionListener, TimeChangedImpl
{
    int screenWidth;
    int screenHeight;
    static double share;
    JToolBar toolBar = new JToolBar("时钟");
    JButton secondButton = new JButton("关闭秒针声音");
    JButton hourBellButton = new JButton("关闭闹钟");
    JButton clockColorSetButton = new JButton("时钟颜色设置");
    JButton BellConfiguration = new JButton("闹钟设置");
    JButton upScaleBt = new JButton("放大");
    JButton downScaleBt = new JButton("缩小");
    int playSecondPointerSoundFlag = 1;
    int hourBellFlag = 1;
    Container container;
    ClockPanel clockPanel;
    SoundPlayer ringBellSoundPlayer;
    SoundPlayer secondPointerSoundPlayer;
    Thread bellRingThread;
    volatile boolean isClockTerminated;
    Vector<BellTimeUnit<Integer, Integer, Integer>> bellTimeUnitList;
    private int preSecond = -1;
    private Calendar preCalendar;
    float scale = 1.0f;
    private int winWidth = 440;
    private int winHeight = 464;
    private int toolBarWidth = winWidth;
    private int toolBarHeight = winHeight - winWidth;
    private int dimensionWidth = 5;
    private int dimensionHeight = 5;
    private int clockPanelWidth = winWidth;
    private int clockPanelHeight = clockPanelWidth;
    private float maxScale; //最大放大比例
    private boolean isInWaitStatus;//用户推迟闹钟时间

    public boolean isInWaitStatus()
    {
        return isInWaitStatus;
    }

    public void setInWaitStatus(boolean inWaitStatus)
    {
        isInWaitStatus = inWaitStatus;
    }

    private float getClockScale()
    {
        ResultSet rSet = null;
        float resScale = 1.0f;
        MysqlDbConnect dbConnect = new MysqlDbConnect();
        rSet = dbConnect.getResultSet("select scale from clockscale");
        try
        {
            if (rSet.next())
            {
                resScale = rSet.getFloat("scale");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        dbConnect.closeConnection();
        return resScale;
    }

    public ClockFrame()
    {

        ringBellSoundPlayer = new SoundPlayer("soundTime" + File.separator + "公鸡叫声.wav");
        ringBellSoundPlayer.setIsLoop(true);
        secondPointerSoundPlayer = new SoundPlayer("soundTime" + File.separator + "secondPointerSound.wav");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        clockPanel = new ClockPanel();
        container = getContentPane();
        screenWidth = (int) toolkit.getScreenSize().getWidth();
        screenHeight = (int) toolkit.getScreenSize().getHeight();
//        ClockFrame.share = screenWidth * 1.0 / screenHeight;
        setLayout(null);
        bellTimeUnitList = new Vector<BellTimeUnit<Integer, Integer, Integer>>();
        bellTimeUnitList = updateBellTimeList();
        scale = getClockScale();
        setBounds((int) ((screenWidth - scale * winWidth) / 2), (int) ((screenHeight - scale * winHeight) / 2),
                (int) (scale * winWidth), (int) (scale * winHeight));
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
//               super.windowClosing(e);
                System.exit(0);
            }
        });
        maxScale = Math.min(screenWidth * 1.0f / winWidth, screenHeight * 1.0f / winHeight);
        preCalendar = Calendar.getInstance();
        preSecond = preCalendar.get(Calendar.SECOND);
        bellRingThread = new Thread(this, "闹铃控制线程");
        bellRingThread.start();
        setResizable(false);
        setVisible(true);

    }


    public void initComponent()
    {

        toolBar.removeAll();
        container.removeAll();
        clockPanel.removeAll();
        toolBar.add(secondButton);
        toolBar.addSeparator(new Dimension((int) (scale * dimensionWidth), (int) (scale * dimensionHeight)));
        toolBar.add(hourBellButton);
        toolBar.addSeparator(new Dimension((int) (scale * dimensionWidth), (int) (scale * dimensionHeight)));
        toolBar.add(clockColorSetButton);
        toolBar.addSeparator(new Dimension((int) (dimensionWidth * scale), (int) (scale * dimensionHeight)));
        toolBar.add(BellConfiguration);
        toolBar.addSeparator(new Dimension((int) (dimensionWidth * scale), (int) (scale * dimensionHeight)));
        toolBar.add(upScaleBt);
        toolBar.addSeparator(new Dimension((int) (dimensionWidth * scale), (int) (scale * dimensionHeight)));
        toolBar.add(downScaleBt);
        container.add(toolBar);
        toolBar.setBounds(0, 0, (int) (scale * toolBarWidth), (int) (scale * toolBarHeight));
        clockPanel.queryClockColorFromDb();
        container.add(clockPanel);
        clockPanel.setBounds(0, toolBar.getHeight(), (int) (scale * clockPanelWidth), (int) (scale * clockPanelHeight));
        clockColorSetButton.addActionListener(this);
        BellConfiguration.addActionListener(this);
        secondButton.addActionListener(this);
        hourBellButton.addActionListener(this);
        upScaleBt.addActionListener(this);
        downScaleBt.addActionListener(this);

    }


    public static void main(String[] args)
    {
        ClockFrame clockFrame = new ClockFrame();
        clockFrame.initComponent();
    }

    public void run()
    {
        while (!isClockTerminated)
        {
            Thread currentThread = Thread.currentThread();
            if (preSecond != Calendar.getInstance().get(Calendar.SECOND))
            {
                timeChanged(preCalendar, Calendar.getInstance());
                preCalendar = Calendar.getInstance();
                preSecond = preCalendar.get(Calendar.SECOND);
            }
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void timeChanged(Calendar oldCalendar, Calendar newCalendar)
    {
        if (playSecondPointerSoundFlag == 1)
        {
            secondPointerSoundPlayer.play();
        }
        if (hourBellFlag == 1 && isBellTimeElapsed(newCalendar) && !isInWaitStatus())
        {
            System.out.println("时间到了");
            ResponseRingBellHandler.getSingleton(this,ringBellSoundPlayer, newCalendar).initComponent();
        }
        clockPanel.repaint();
    }

    class BellTimeUnit<H, M, S>
    {
        H h;
        M m;
        S s;

        public BellTimeUnit(H h, M m, S s)
        {
            this.h = h;
            this.m = m;
            this.s = s;
        }

        H getHour()
        {
            return h;
        }

        M getMinute()
        {
            return m;
        }

        S getSecond()
        {
            return s;
        }

    }

    public synchronized Vector<BellTimeUnit<Integer, Integer, Integer>> updateBellTimeList()
    {
        bellTimeUnitList.clear();
        MysqlDbConnect mysqlDbConnect = new MysqlDbConnect();
        String sqlString = "select * from ringTable";
        ResultSet resultSet = mysqlDbConnect.getResultSet(sqlString);
        try
        {
            while (resultSet.next())
            {
                bellTimeUnitList.add(new BellTimeUnit<Integer, Integer, Integer>(resultSet.getInt("hour"),
                        resultSet.getInt("minute"), 0));
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            mysqlDbConnect.closeConnection();
        }
        return bellTimeUnitList;
    }

    private boolean isBellTimeElapsed(Calendar now)
    {

        int flag = 0;
//        bellTimeUnitList = updateBellTimeList();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        for (BellTimeUnit<Integer, Integer, Integer> bellTimeUnit : bellTimeUnitList)
        {
            if (hour == bellTimeUnit.getHour() && minute == bellTimeUnit.getMinute() &&
                    second == bellTimeUnit.getSecond())
            {
                flag = 1;
                break;
            }
        }
        return flag == 1 ? true : false;
    }


    private void peerBellTime()
    {


    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub
        if (e.getSource().equals(secondButton))
        {
            if (playSecondPointerSoundFlag == 1)
            {
                playSecondPointerSoundFlag = 0;
                secondButton.setText("开启秒针声音");
            }
            else if (playSecondPointerSoundFlag == 0)
            {
                playSecondPointerSoundFlag = 1;
                secondButton.setText("关闭秒针声音");

            }

        }
        if (e.getSource().equals(BellConfiguration))
        {
            new ListBellHandler(this);
        }
        if (e.getSource().equals(hourBellButton))
        {
            if (hourBellFlag == 1)
            {
                hourBellButton.setText("开启闹钟");
                hourBellFlag = -1;
            }
            else
            {
                hourBellButton.setText("关闭闹钟");
                hourBellFlag = 1;
            }
        }
        if (e.getSource().equals(clockColorSetButton))
        {
            new ClockColorHandler(clockPanel);
        }
        if (e.getSource().equals(upScaleBt))
        {
            scale = (scale + 0.1) <= maxScale ? (scale + 0.1f) : scale;
            JOptionPane.showMessageDialog(null, "重启生效，当前缩放比例为" + new DecimalFormat(".0").format(scale));
            updateClockScale(scale);
        }
        if (e.getSource().equals(downScaleBt))
        {
            scale = (scale - 0.1) >= 1.0f ? (scale - 0.1f) : scale;
            JOptionPane.showMessageDialog(null, "重启生效，当前缩放比例为" + new DecimalFormat(".0").format(scale));
            updateClockScale(scale);
        }
//        System.out.println("scale:" + scale);
    }

    private void updateClockScale(float scale)
    {
        MysqlDbConnect dbConection = new MysqlDbConnect();
        dbConection.execute("update clockscale set scale=" + new DecimalFormat(".0").format(scale));
        dbConection.closeConnection();
    }
}

interface TimeChangedImpl
{
    void timeChanged(Calendar oldCalendar, Calendar newCalendar);
}
