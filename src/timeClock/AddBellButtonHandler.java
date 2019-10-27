package timeClock;

import mysqlConnect.MysqlDbConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddBellButtonHandler extends JFrame implements ActionListener
{
    JLabel setTimeLabel = new JLabel("闹钟时间");
    JButton affirmButton = new JButton("应用");
    JComboBox hourComboBox;
    JComboBox minuteComboBox;
    DefaultListModel model;
    JButton addButton;
    int hour = 0;
    int minute = 0;
    ClockFrame clockFrame;

    public AddBellButtonHandler(
            JComboBox hourComboBox,
            JComboBox minuteComboBox, DefaultListModel model, JButton addButton, ClockFrame clockFrame
    )
    {
        this.hourComboBox = hourComboBox;
        this.minuteComboBox = minuteComboBox;
        this.model = model;
        this.addButton = addButton;
        this.clockFrame = clockFrame;
        setBounds(800, 100, 300, 70);
        setLayout(new FlowLayout());
        Container container = getContentPane();
        container.add(setTimeLabel);
        container.add(hourComboBox);
        container.add(new JLabel("时"));
        container.add(minuteComboBox);
        container.add(new JLabel("分"));
        container.add(affirmButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        hourComboBox.addActionListener(this);
        minuteComboBox.addActionListener(this);
        affirmButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub

        if (e.getSource().equals(hourComboBox))
        {
            hour = Integer.parseInt(((JComboBox) e.getSource())
                    .getSelectedItem().toString());
        }
        if (e.getSource().equals(minuteComboBox))
        {
            minute = Integer.parseInt(((JComboBox) e.getSource())
                    .getSelectedItem().toString());
        }
        if (e.getSource().equals(affirmButton))
        {
            if (isExist(hour, minute))
            {
                JOptionPane.showMessageDialog(null, "闹钟已经存在");
                return;
            }
            String ringTimeString = null;
            SimpleDateFormat format = new SimpleDateFormat();
            Calendar selectCalendar = Calendar.getInstance();
            selectCalendar.set(Calendar.HOUR_OF_DAY, hour);
            selectCalendar.set(Calendar.MINUTE, minute);
            model.addElement(new SimpleDateFormat("HH:mm").format(selectCalendar.getTime()));
            ListBellHandler.setOrderForListCell(model);
            String sqlString = "insert into ringTable(hour,minute) values("
                    + hour + "," + minute + ")";
            new MysqlDbConnect().execute(sqlString);
            clockFrame.updateBellTimeList();


        }
        if (e.getSource().equals(addButton))
        {
            setVisible(true);
        }

    }

    private boolean isExist(int hour, int minute)
    {
        boolean result = false;
        String sqlString = "select count(*) from ringTable where hour=" + hour
                + " and minute=" + minute + "";
        ResultSet rSet = new MysqlDbConnect().getResultSet(sqlString);
        try
        {
            if (rSet.next())
            {
                if (rSet.getInt(1) != 0)
                {
                    result = true;
                }
            }

        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return result;
    }
}
