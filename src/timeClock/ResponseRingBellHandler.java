package timeClock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ResponseRingBellHandler extends JFrame implements ActionListener
{
    private static SoundPlayer soundPlayer;
    private int putOffTimeInterval = 10 * 60 * 1000; //推迟时间
    JButton closeRingButton = new JButton("关闭闹钟");
    JButton putOffRingButton = new JButton("推迟十分钟");
    Dimension dimension;
    static ResponseRingBellHandler uniqueInstance;
    private static Calendar initRingBellTime;
    private static ClockFrame clockFrame;
    private Timer putOffTimer;
    private Thread ringBellTimeThread;
    private boolean isBellRinging;
    private long maxBellRingTimeLength = 1 * 60 * 1000L;//闹钟最长唱响时间

    private ResponseRingBellHandler()
    {
        super.setTitle("闹钟报时");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        dimension = toolkit.getScreenSize();
        initComponent();
        ringBellTimeThread = new Thread()
        {
            @Override
            public void run()
            {
//                super.run();
                while (true)
                {
                    if (isBellRinging &&
                            Calendar.getInstance().getTimeInMillis() - initRingBellTime.getTimeInMillis() >=
                                    maxBellRingTimeLength)
                    {
//                        closeRingButtonOperation();
                        putOffRingButtonOperation();
                    }
                    try
                    {
                        Thread.sleep(20);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        ringBellTimeThread.start();

    }

    private void closeRingButtonOperation()
    {
        soundPlayer.stop();
        dispose();
        clockFrame.setInWaitStatus(false);
        isBellRinging = false;
        if (putOffTimer != null)
        {
            putOffTimer.cancel();
        }
    }

    public static ResponseRingBellHandler getSingleton(
            ClockFrame clockFrame, SoundPlayer soundPlayer, Calendar originalDate
    )
    {
        ResponseRingBellHandler.clockFrame = clockFrame;
        ResponseRingBellHandler.soundPlayer = soundPlayer;
        ResponseRingBellHandler.initRingBellTime = originalDate;
        if (uniqueInstance == null)
        {
            uniqueInstance = new ResponseRingBellHandler();
        }
        return uniqueInstance;
    }

    public void initComponent()
    {
        soundPlayer.play();
        isBellRinging = true;
        Container container = getContentPane();
        setBounds((int) (dimension.getWidth() * 0.4),
                (int) (dimension.getHeight() * 0.2),
                (int) (dimension.getWidth() * 0.2),
                (int) (dimension.getHeight() * 0.6));
        setLayout(new GridLayout(2, 1));
        closeRingButton.setFont(new Font("jin", Font.BOLD, 25));
        closeRingButton.setForeground(Color.BLUE);
        putOffRingButton.setFont(new Font("jin", Font.BOLD, 25));
        putOffRingButton.setForeground(Color.GREEN);
        container.add(closeRingButton);
        container.add(putOffRingButton);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
//                super.windowClosed(e);
                putOffRingButtonOperation();
            }
        });
        setVisible(true);
        closeRingButton.addActionListener(this);
        putOffRingButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(closeRingButton))
        {
            closeRingButtonOperation();
        }
        if (e.getSource().equals(putOffRingButton))
        {
            putOffRingButtonOperation();
        }

    }

    private void putOffRingButtonOperation()
    {
        soundPlayer.stop();
        isBellRinging = false;
        setVisible(false);
        clockFrame.setInWaitStatus(true);
        initRingBellTime.add(Calendar.MILLISECOND, putOffTimeInterval);
        putOffTimer = new Timer();
        putOffTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                soundPlayer.play();
                isBellRinging = true;
                ResponseRingBellHandler.this.setVisible(true);
            }
        }, initRingBellTime.getTime());

    }

}
