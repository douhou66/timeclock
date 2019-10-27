package timeClock;

import mysqlConnect.MysqlDbConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClockColorHandler extends JFrame implements ActionListener
{
    ClockPanel clockPanel;
    JColorChooserApplication instance = new JColorChooserApplication();
    JButton clockPanelColorButton = new JButton("内容面板颜色");
    Color clockPanelColor;
    JButton clockRingColorButton = new JButton("时钟表环颜色");
    Color clockRingColor;

    JButton clockContentColorButton = new JButton("时钟面板颜色");
    Color clockContentColor;

    JButton clockHourPointerColorButton = new JButton("时针颜色");
    Color clockHourPointerColor;

    JButton clockMinutePointerColorButton = new JButton("分针颜色");
    Color clockMinutePointerColor;
    JButton clockSecondPointerColorButton = new JButton("秒针颜色");
    Color clockSecondPointerColor;
    JButton dialGaugeBt=new JButton("时钟数字颜色");
    Color dialGaugeColor;
    JButton applyButton = new JButton("应用");

    public ClockColorHandler(ClockPanel clockPanel)
    {
        this.clockPanel = clockPanel;
        setBounds(400, 100, 200, 400);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent E)
            {
                setVisible(false);
            }
        });
        initComponent();

    }

    public void initComponent()
    {
        Container container = getContentPane();
        AncestorPanel ancestorPanel = new AncestorPanel();
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(7, 1, 40, 15));
        colorPanel.add(clockPanelColorButton);
        colorPanel.add(clockRingColorButton);
        colorPanel.add(clockContentColorButton);
        colorPanel.add(clockHourPointerColorButton);
        colorPanel.add(clockMinutePointerColorButton);
        colorPanel.add(clockSecondPointerColorButton);
        colorPanel.add(dialGaugeBt);
        ancestorPanel.add(colorPanel, BorderLayout.CENTER);
        colorPanel.setPreferredSize(new Dimension(300, 200));
        ancestorPanel.add(applyButton, BorderLayout.SOUTH);
        container.add(ancestorPanel);
        clockPanelColorButton.addActionListener(this);
        clockPanelColor = clockPanel.getColor("clockpanel");
        clockRingColorButton.addActionListener(this);
        clockRingColor = clockPanel.getColor("clockring");
        clockContentColorButton.addActionListener(this);
        clockContentColor = clockPanel.getColor("clockcontent");
        clockHourPointerColorButton.addActionListener(this);
        clockHourPointerColor = clockPanel.getColor("hourpointer");
        clockMinutePointerColorButton.addActionListener(this);
        clockMinutePointerColor = clockPanel.getColor("minutepointer");
        clockSecondPointerColorButton.addActionListener(this);
        clockSecondPointerColor = clockPanel.getColor("secondpointer");
        dialGaugeBt.addActionListener(this);
        dialGaugeColor =clockPanel.getColor("dialgauge");
        updateBackground();
        applyButton.setBackground(Color.GREEN);
        applyButton.addActionListener(this);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    private void updateBackground()
    {
        clockContentColorButton.setBackground(clockContentColor);
        clockPanelColorButton.setBackground(clockPanelColor);
        clockRingColorButton.setBackground(clockRingColor);
        clockHourPointerColorButton.setBackground(clockHourPointerColor);
        clockMinutePointerColorButton.setBackground(clockMinutePointerColor);
        clockSecondPointerColorButton.setBackground(clockSecondPointerColor);
        dialGaugeBt.setBackground(dialGaugeColor);
    }

    private void colorApplied()
    {

        String sqlString = "select red,green,blue,type from clockColor";
        ResultSet resultSet = new MysqlDbConnect().getResultSet(sqlString);
        int red = 0, green = 0, blue = 0;
        try
        {
            while (resultSet.next())
            {
                Color color = new Color(resultSet.getInt(1),
                        resultSet.getInt(2), resultSet.getInt(3));

                switch (resultSet.getString("type"))
                {

                    case "clockpanel":
                        red = clockPanelColor.getRed();
                        green = clockPanelColor.getGreen();
                        blue = clockPanelColor.getBlue();
                        clockPanel.updateColor(clockPanelColor, "clockpanel");
                        break;
                    case "clockring":
                        red = clockRingColor.getRed();
                        green = clockRingColor.getGreen();
                        blue = clockRingColor.getBlue();
                        clockPanel.updateColor(clockRingColor, "clockring");
                        break;
                    case "clockcontent":
                        red = clockContentColor.getRed();
                        green = clockContentColor.getGreen();
                        blue = clockContentColor.getBlue();
                        clockPanel.updateColor(clockContentColor, "clockcontent");
                        break;
                    case "hourpointer":
                        red = clockHourPointerColor.getRed();
                        green = clockHourPointerColor.getGreen();
                        blue = clockHourPointerColor.getBlue();
                        clockPanel.updateColor(clockHourPointerColor, "hourpointer");
                        break;
                    case "minutepointer":
                        red = clockMinutePointerColor.getRed();
                        green = clockMinutePointerColor.getGreen();
                        blue = clockMinutePointerColor.getBlue();
                        clockPanel.updateColor(clockMinutePointerColor, "minutepointer");
                        break;
                    case "secondpointer":
                        red = clockSecondPointerColor.getRed();
                        green = clockSecondPointerColor.getGreen();
                        blue = clockSecondPointerColor.getBlue();
                        clockPanel.updateColor(clockSecondPointerColor, "secondpointer");
                        break;
                    case "dialgauge":
                          red= dialGaugeColor.getRed();
                        green= dialGaugeColor.getGreen();
                        blue= dialGaugeColor.getBlue();
                        clockPanel.updateColor(dialGaugeColor,"dialgauge");
                        break;
                    default:
                        break;
                }
                String executeString = "update clockColor set red=" + red
                        + " ,green=" + green + ",blue=" + blue
                        + " where type=" + "'" + resultSet.getString("type") + "'" + " ";
                new MysqlDbConnect().execute(executeString);
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setVisible(false);

    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        Color selectedColor = null;
        if (e.getSource().equals(applyButton))
        {
            colorApplied();
        }
        else if (e.getSource().equals(clockPanelColorButton))
        {
            if ((selectedColor = instance.getChooseColor()) != null)
            {
                clockPanelColor = selectedColor;
            }

        }
        else if (e.getSource().equals(clockRingColorButton))
        {
            if ((selectedColor = instance.getChooseColor()) != null)
//                clockPanelColor=selectedColor;
            {
                clockRingColor = selectedColor;
            }
        }
        else if (e.getSource().equals(clockContentColorButton))
        {
            if ((selectedColor = instance.getChooseColor()) != null)
            {
                clockContentColor = selectedColor;
            }

        }
        else if (e.getSource().equals(clockHourPointerColorButton))
        {
            if ((selectedColor = instance.getChooseColor()) != null)
            {
                clockHourPointerColor = selectedColor;
            }
        }
        else if (e.getSource().equals(clockMinutePointerColorButton))
        {
            if ((selectedColor = instance.getChooseColor()) != null)
            {
                clockMinutePointerColor = selectedColor;
            }
        }
        else if (e.getSource().equals(clockSecondPointerColorButton))
        {
            if ((selectedColor = instance.getChooseColor()) != null)
            {
                clockSecondPointerColor = selectedColor;
            }
        }
        else if(e.getSource().equals(dialGaugeBt))
        {
            if((selectedColor=instance.getChooseColor())!=null)
                dialGaugeColor =selectedColor;
        }
        updateBackground();
    }

}
