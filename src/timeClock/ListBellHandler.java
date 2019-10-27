package timeClock;

import mysqlConnect.MysqlDbConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class ListBellHandler extends JFrame implements ActionListener
{
    JList list;
    DefaultListModel model;
    JScrollPane scrollPane = new JScrollPane();
    JButton addButton = new JButton("添加");
    JButton delButton = new JButton("删除");
    JComboBox hourComboBox;
    JComboBox minuteComboBox;
    JButton ringBellButton;

    public void finalize()
    {
        try
        {
            super.finalize();
        }
        catch (Throwable e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private ClockFrame clockFrame;

    public ListBellHandler(ClockFrame clockFrame)
    {
        this.clockFrame = clockFrame;
        setBounds(new Rectangle(500, 100, 200, 450));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        model = new DefaultListModel();
        list = new JList(model);
        list.setBackground(Color.GRAY);
        scrollPane.setViewportView(list);
        scrollPane
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Vector<Integer> hourVector = new Vector<Integer>();
        Vector<Integer> secondVector = new Vector<Integer>();
        for (int i = 0; i < 60; i++)
        {
            if (i >= 24)
            {
                secondVector.addElement(i);
                continue;
            }
            hourVector.addElement(i);
            secondVector.addElement(i);
        }
        hourComboBox = new JComboBox(hourVector);
        minuteComboBox = new JComboBox(secondVector);
        list.setCellRenderer(new MyListCellRender());
        initComponent();
    }

    public void initComponent()
    {
        String sqlString = "select * from ringTable";
        ResultSet rSet = new MysqlDbConnect().getResultSet(sqlString);
        try
        {
            while (rSet.next())
            {
                String limitedTime = null;
                int hour = rSet.getInt("hour");
                int minute = rSet.getInt("minute");
                Calendar ringTime = Calendar.getInstance();
                ringTime.set(Calendar.HOUR_OF_DAY, hour);
                ringTime.set(Calendar.MINUTE, minute);
                limitedTime = new SimpleDateFormat("HH:mm").format(ringTime.getTime());
                model.addElement(limitedTime);
            }
            setOrderForListCell(model);

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        Container container = getContentPane();
        scrollPane.setPreferredSize(new Dimension(200, 415));
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.setPreferredSize(new Dimension(200, 35));
        // addButton.setBounds(0, 0, 80, 40);
        // delButton.setBounds(110, 0, 80, 40);
        addButton.setBackground(new Color(210, 159, 58));
        delButton.setBackground(new Color(209, 159, 58));
        panel.add(addButton);
        panel.add(delButton);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(panel, BorderLayout.SOUTH);
        setResizable(false);
        setVisible(true);

        addButton.addActionListener(new AddBellButtonHandler(hourComboBox,
                minuteComboBox, model, addButton, clockFrame));
        delButton.addActionListener(this);
    }

    public static  void setOrderForListCell(DefaultListModel model)
    {
        // TODO Auto-generated method stub
        Object[] allObjects = model.toArray();
        for (int i = 0; i < allObjects.length - 1; i++)
        {
            int minIndex = i;
            for (int j = i + 1; j < allObjects.length; j++)
            {
                if (allObjects[minIndex].toString().compareTo(allObjects[j].toString()) > 0)
                {
                    minIndex = j;
                }
            }
            Object tempObject = allObjects[i];
            if (!tempObject.equals(allObjects[minIndex]))
            {
                allObjects[i] = allObjects[minIndex];
                allObjects[minIndex] = tempObject;
            }
           /* if (allObjects[i].toString()
                    .compareTo(allObjects[i + 1].toString()) > 0)
            {
                allObjects[i] = allObjects[i + 1];
                allObjects[i + 1] = tempObject;
            }*/
        }
        model.removeAllElements();
        for (int i = 0; i < allObjects.length; i++)
            model.addElement(allObjects[i]);

    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(delButton))
        {
            int choice = -1;
            String[] options =
                    {"确定", "取消"};
            Object[] selectedObjects = list.getSelectedValues();
            choice = JOptionPane.showOptionDialog(null, "你确定删除此选项吗", "通知",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
            if (choice == 0)
            {
                for (int i = 0; i < selectedObjects.length; i++)
                {
                    model.removeElement(selectedObjects[i]);
                    String getTextString = selectedObjects[i].toString();
                    String[] itemStrings = getTextString.split(":");
                    try
                    {
                        int hour = Integer.parseInt(itemStrings[0].trim());
                        int minute = Integer.parseInt(itemStrings[1].trim());
                        String sqlString = "delete from ringTable where hour="
                                + hour + " and minute=" + minute + "";
                        new MysqlDbConnect().execute(sqlString);
                        clockFrame.updateBellTimeList();

                    }
                    catch (Exception e2)
                    {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }
}
