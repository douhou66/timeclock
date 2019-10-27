package timeClock;

import javax.swing.*;
import java.awt.*;

public class JColorChooserApplication extends JFrame

{
    public Color color;

    public Color getChooseColor()
    {
        color = JColorChooser.showDialog(null, "颜色选择", Color.WHITE);

        if (color != null)
        {
            getRGB();
        }
        return color;
    }

    public void getRGB()
    {
        System.out.println(color.getRed() + " " + color.getGreen()
                + "  " + color.getBlue());
    }

    public static void main(String[] args)
    {
        JColorChooserApplication instance = new JColorChooserApplication();
        instance.getChooseColor();
    }
}
