package GUI;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import java.util.Arrays;
import javax.imageio.*;
import javax.swing.*;

public class ScreenImage {

    private static List<String> types = Arrays.asList(ImageIO.getWriterFileSuffixes());

    public static BufferedImage createImage(JComponent component, Rectangle region) {
        //  Make sure the component has a size and has been layed out.
        //  (necessary check for components not added to a realized frame)

        if (!component.isDisplayable()) {
            Dimension d = component.getSize();

            if (d.width == 0 || d.height == 0) {
                d = component.getPreferredSize();
                component.setSize(d);
            }

            //	layoutComponent( component );
        }

        BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        //  Paint a background for non-opaque components,
        //  otherwise the background will be black

        if (!component.isOpaque()) {
            g2d.setColor(component.getBackground());
            g2d.fillRect(region.x, region.y, region.width, region.height);
        }

        g2d.translate(-region.x, -region.y);
        component.paint(g2d);
        g2d.dispose();
        return image;
    }

    public static void writeImage(BufferedImage image)
            throws IOException {



        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showSaveDialog(null);
        File fileName2;
        fileName2 = fileChooser.getSelectedFile();
        String title = fileName2.getName();
        if (title == null) {
            return;
        }

        int offset = title.lastIndexOf(".");

        if (offset == -1) {
            String message = "file suffix was not specified";
            throw new IOException(message);
        }

        if (fileName2 != null) {
            try {
            } catch (Exception exp) {

                JOptionPane.showMessageDialog(null, "Can't Open File");
            }
        }
        String type = title.substring(offset + 1);
        if (types.contains(type)) {

            ImageIO.write(image, type, fileName2);

        } else {
            String message = "unknown writer file suffix (" + type + ")";
            throw new IOException(message);
        }
    }


}
