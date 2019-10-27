package timeClock;

import javax.swing.*;
import java.awt.*;

public class MyListCellRender extends JLabel implements ListCellRenderer
{
    private String text;
    private Color foreground;
    private Color background;
    private int preferedWidth = 200;
    private int preferedHeight = 40;
    private int seperatorDimenHeight = 10;

    public Component getListCellRendererComponent(
            JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus
    )
    {

        if (isSelected)
        {
            foreground = Color.RED;
            background = Color.BLUE;
            text = value.toString();

        }
        else
        {
            foreground = Color.GREEN;
            background = Color.ORANGE;
            text = value.toString();
        }
        setBackground(background);
        setForeground(foreground);
        AncestorPanel resComp = new AncestorPanel();
        resComp.setLayout(null);
        Component comp1 = getSeperatorComp();
        comp1.setBounds(0, 0, comp1.getWidth(), comp1.getHeight());
        resComp.add(comp1);
//        this.setPreferredSize(getPreferredSize());
        this.setBounds(0, comp1.getHeight(), preferedWidth, preferedHeight);
        setFont(new Font("jinti", Font.BOLD, 28));
        setOpaque(true);
        resComp.add(this);
        Component comp2 = getSeperatorComp();
        comp2.setBounds(0, preferedHeight + comp1.getHeight(), comp2.getWidth(), comp2.getHeight());
        resComp.add(comp2);
        resComp.setPreferredSize(new Dimension(preferedWidth, preferedHeight + 2 * seperatorDimenHeight));
        return resComp;
    }

    private Component getSeperatorComp()
    {
        JLabel separatorComp = new JLabel();
        separatorComp.setSize(preferedWidth, seperatorDimenHeight);
        return separatorComp;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(preferedWidth, preferedHeight);
    }

    public void paintComponent(Graphics g)
    {
        g.setColor(background);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(foreground);
        g.drawString(text, 45, 20);
    }
}
