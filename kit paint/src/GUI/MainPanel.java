/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import paint.Coordinate;

/**
 *
 * @author Taposh
 */
public class MainPanel extends javax.swing.JPanel {

    /**
     * Creates new form MainPanel
     */
    private Color backGroundColor, foreGroundColor;
    protected static Vector xPolygon, yPolygon, vPolygon;
    int DRAWMODE, FREEHAND = 1, LINE = 2, CIRCLE = 3, RECT = 4, ROUNDED_REC = 5, ARC = 6, BRUSH = 7, TEXT = 8, ERASE = 9, POLYGON = 10;
    int x = 574, y = 358;
    private boolean solid, polygonBuffer;
    public Image image;
    public Graphics2D graphics2d;
    BufferedImage saveBI;
    int OldX, OldY, CurX, CurY;
    int fontStyle;
    Font font;
    JPanel b = this.ButtonPanel;
    private Stroke drawingStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    private Stroke drawingStroke2 = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    private Stroke drawingStroke3 = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    private Stroke drawingStroke4 = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    private Stroke drawingStroke5 = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    private Stroke drawingStroke6 = new BasicStroke(6, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    private File fileName;
    GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String fontNames[] = e.getAvailableFontFamilyNames();
    int check =1;
 //   Toolkit tk = Toolkit.getDefaultToolkit();
//    Image pencilImg = tk.getImage(getClass().getResource("/GUI/MyPencil.gif"));
////    Image eraserImg = tk.getImage(getClass().getResource("/GUI/eraser_16_16.gif"));
////    Image BrushImg =  tk.getImage(getClass().getResource("/GUI/brush_16_16.gif"));
//    Cursor pencilCursor = tk.createCustomCursor(pencilImg, new Point(15,15), "Pencil Cursor");
////            Cursor eraserCursor = tk.createCustomCursor(eraserImg, null, "Eraser Cursor");
////            Cursor BrushCursor = tk.createCustomCursor(BrushImg, null, "Brush Cursor");
    public MainPanel() {
        xPolygon = new Vector();
        yPolygon = new Vector();
        vPolygon = new Vector();
        polygonBuffer = false;
        setDoubleBuffered(false);
        initComponents();
        setFont();
        jPanel2.setVisible(false);
        jPanel4.setVisible(false);
        buttonGroup1.add(size1);
        buttonGroup1.add(size2);
        buttonGroup1.add(size3);
        buttonGroup1.add(size4);
        buttonGroup1.add(size5);
        buttonGroup1.add(size6);
        buttonGroup2.add(brush1);
        buttonGroup2.add(brush2);
        buttonGroup2.add(brush3);
        buttonGroup2.add(brush4);
        buttonGroup2.add(brush5);
        buttonGroup2.add(brush6);
        
    }
    public void readImage(){
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.showOpenDialog(null);
        File fileName2;
        fileName2 = fileChooser.getSelectedFile();
        
        try {
            image=ImageIO.read(fileName2);
            graphics2d = (Graphics2D) image.getGraphics();
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setPaint(Color.black);
            repaint();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    public void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(2000, 2000);
            graphics2d = (Graphics2D) image.getGraphics();
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        else{
            
        }
        g.drawImage(image, 0, 0, null);
        redrawVectorBuffer(graphics2d);
        if (DRAWMODE == POLYGON) {
            int xPos[] = new int[xPolygon.size()];
            int yPos[] = new int[yPolygon.size()];

            for (int count = 0; count < xPos.length; count++) {
                xPos[count] = ((Integer) (xPolygon.elementAt(count))).intValue();
                yPos[count] = ((Integer) (yPolygon.elementAt(count))).intValue();
            }
            graphics2d.drawPolyline(xPos, yPos, xPos.length);
            polygonBuffer = true;
            repaint();
        }
    }

    private void redrawVectorBuffer(Graphics g) {
        for (int i = 0; i < vPolygon.size(); i++) {
            int xPos[] = new int[((Coordinate) vPolygon.elementAt(i)).getXPolygon().size()];
            int yPos[] = new int[((Coordinate) vPolygon.elementAt(i)).getYPolygon().size()];

            for (int count = 0; count < xPos.length; count++) {
                xPos[count] = ((Integer) ((Coordinate) vPolygon.elementAt(i)).getXPolygon().elementAt(count)).intValue();
                yPos[count] = ((Integer) ((Coordinate) vPolygon.elementAt(i)).getYPolygon().elementAt(count)).intValue();
            }
            graphics2d.setColor(((Coordinate) vPolygon.elementAt(i)).colour());
            graphics2d.drawPolyline(xPos, yPos, xPos.length);
        }
    }

    public void flushPolygonBuffer() {

        vPolygon.add(new Coordinate(xPolygon, yPolygon, GetForeGroundColor()));
        xPolygon.removeAllElements();
        yPolygon.removeAllElements();
        polygonBuffer = false;
        repaint();
    }

    public boolean isExistPolygonBuffer() {
        return polygonBuffer;
    }

    public void DrawObject() {
        graphics2d.setPaint(GetForeGroundColor());
        repaint();
    }
    public void clear() {
        graphics2d.setPaint(Color.white);
        graphics2d.fillRect(0, 0, 2000, 2000);
        graphics2d.setPaint(Color.black);
        repaint();
    }
    public void clear(int i) {
         xPolygon.clear();
         yPolygon.clear();
         vPolygon.clear();
        graphics2d.setPaint(Color.white);
        graphics2d.fillRect(0, 0, 2000, 2000);
        graphics2d.setPaint(Color.black);
        repaint();
    }

    public void setDrawMode(int n) {
        DRAWMODE = n;
    }

    public int getDrawMode() {
        return DRAWMODE;
    }

    public void SetBackGroundColor() {
        backGroundColor = JColorChooser.showDialog(null, "Background Color", backGroundColor);
        repaint();

        if (backGroundColor != null) {
            graphics2d.setPaint(backGroundColor);
            graphics2d.fillRect(0, 0, 2000, 2000);
            graphics2d.setBackground(backGroundColor);
            this.BackGroundColorButton.setBackground(backGroundColor);
            graphics2d.setPaint(Color.BLACK);
            repaint();
        }
    }

    public void SetBackGroundColor(Color aColor) {
        backGroundColor = aColor;
        if (backGroundColor != null) {
            this.setBackground(backGroundColor);
            graphics2d.setBackground(backGroundColor);
            this.BackGroundColorButton.setBackground(backGroundColor);
        }
    }

    public void SetForeGroundColor() {
        foreGroundColor = JColorChooser.showDialog(null, "ForeGround Color", foreGroundColor);
        repaint();
        if (foreGroundColor != null) {
            //graphics2d.setBackground(foreGroundColor);
            graphics2d.setPaint(foreGroundColor);
            this.ForeGroundColorButton.setBackground(foreGroundColor);
        }
    }

    public void SetForeGroundColor(Color aColor) {
        foreGroundColor = aColor;
        if (foreGroundColor != null) {
            graphics2d.setPaint(foreGroundColor);
            this.ForeGroundColorButton.setBackground(foreGroundColor);
        }
    }

    public Color GetBackGroundColor() {
        return backGroundColor;
    }

    public Color GetForeGroundColor() {
        return foreGroundColor;
    }

    public int returnX() {
        return x;
    }

    public int returnY() {
        return y;
    }

    public void setSolidMode(Boolean setSolid) {
        solid = setSolid.booleanValue();
    }

    public Boolean getSolidMode() {
        return Boolean.valueOf(solid);
    }

    public void setFont() {
        String[] sizeList = {"8", "10", "12", "14", "16", "18", "20", "22", "24", "36", "48", "72"};
        DefaultComboBoxModel model, model2;
        model = new DefaultComboBoxModel(fontNames);
        model2 = new DefaultComboBoxModel(sizeList);
        jComboBox1.setModel(model);
        jComboBox2.setModel(model2);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        brush1 = new javax.swing.JRadioButton();
        brush2 = new javax.swing.JRadioButton();
        brush3 = new javax.swing.JRadioButton();
        brush4 = new javax.swing.JRadioButton();
        brush5 = new javax.swing.JRadioButton();
        brush6 = new javax.swing.JRadioButton();
        ClearButton = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        PencilButton = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        ButtonPanel = new javax.swing.JPanel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton12 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        BackGroundColorButton = new javax.swing.JButton();
        ForeGroundColorButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        LineButton = new javax.swing.JButton();
        OvalButton = new javax.swing.JButton();
        RectangleButton = new javax.swing.JButton();
        RoundRecButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        size1 = new javax.swing.JRadioButton();
        size2 = new javax.swing.JRadioButton();
        size3 = new javax.swing.JRadioButton();
        size6 = new javax.swing.JRadioButton();
        size5 = new javax.swing.JRadioButton();
        size4 = new javax.swing.JRadioButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jCheckBox6 = new javax.swing.JCheckBox();
        Dot = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Position = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(172, 188, 218));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(172, 188, 218));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/images.jpg"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(172, 188, 218));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255)));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jCheckBox3.setBackground(new java.awt.Color(172, 188, 218));
        jCheckBox3.setText("Bold");
        jCheckBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox3ItemStateChanged(evt);
            }
        });

        jCheckBox4.setBackground(new java.awt.Color(172, 188, 218));
        jCheckBox4.setText("Italic");
        jCheckBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox4ItemStateChanged(evt);
            }
        });

        jCheckBox5.setBackground(new java.awt.Color(172, 188, 218));
        jCheckBox5.setText("Plain");
        jCheckBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox5ItemStateChanged(evt);
            }
        });

        jButton5.setText("Ok");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Cancel");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addGap(8, 8, 8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(21, 21, 21))
        );

        jPanel5.setBackground(new java.awt.Color(172, 188, 218));

        brush1.setBackground(new java.awt.Color(172, 188, 218));
        brush1.setText(" 1");

        brush2.setBackground(new java.awt.Color(172, 188, 218));
        brush2.setText(" 2");
        brush2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brush2ActionPerformed(evt);
            }
        });

        brush3.setBackground(new java.awt.Color(172, 188, 218));
        brush3.setText(" 3");

        brush4.setBackground(new java.awt.Color(172, 188, 218));
        brush4.setText(" 4");

        brush5.setBackground(new java.awt.Color(172, 188, 218));
        brush5.setText(" 5");

        brush6.setBackground(new java.awt.Color(172, 188, 218));
        brush6.setText(" 6");
        brush6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brush6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(brush1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(brush3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(brush5))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(brush2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(brush4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(brush6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brush1)
                    .addComponent(brush3)
                    .addComponent(brush5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brush2)
                    .addComponent(brush4)
                    .addComponent(brush6)))
        );

        ClearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/clear.png"))); // NOI18N
        ClearButton.setToolTipText("Clear");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Draw-Eraser-32.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Gnome-Edit-Clear-32.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        PencilButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/pencil_32.png"))); // NOI18N
        PencilButton.setToolTipText("Pencil");
        PencilButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PencilButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PencilButtonActionPerformed(evt);
            }
        });

        jSeparator5.setBackground(new java.awt.Color(153, 153, 255));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator6.setBackground(new java.awt.Color(102, 153, 255));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(PencilButton, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1214, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PencilButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ClearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator5)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator6))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Home", jPanel1);

        ButtonPanel.setBackground(new java.awt.Color(172, 188, 218));
        ButtonPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255), 3));
        ButtonPanel.setPreferredSize(new java.awt.Dimension(2000, 42));
        ButtonPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonPanelMouseEntered(evt);
            }
        });

        jCheckBox2.setBackground(new java.awt.Color(172, 188, 218));
        jCheckBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox2ItemStateChanged(evt);
            }
        });
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(255, 0, 51));
        jButton12.setForeground(new java.awt.Color(255, 0, 51));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(255, 255, 255));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(51, 51, 255));
        jButton13.setForeground(new java.awt.Color(51, 51, 255));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(0, 0, 0));
        jButton14.setForeground(new java.awt.Color(51, 51, 51));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(255, 255, 0));
        jButton15.setForeground(new java.awt.Color(255, 255, 0));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(0, 153, 102));
        jButton16.setForeground(new java.awt.Color(0, 153, 102));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setBackground(new java.awt.Color(204, 204, 204));
        jButton17.setForeground(new java.awt.Color(204, 204, 204));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(153, 153, 153));
        jButton18.setForeground(new java.awt.Color(153, 153, 153));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(255, 0, 255));
        jButton19.setForeground(new java.awt.Color(255, 0, 255));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(153, 255, 255));
        jButton20.setForeground(new java.awt.Color(153, 255, 255));
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setBackground(new java.awt.Color(255, 102, 0));
        jButton21.setForeground(new java.awt.Color(255, 102, 0));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(204, 0, 102));
        jButton22.setForeground(new java.awt.Color(204, 0, 102));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(102, 153, 255));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Grid View");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Background");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Foreground");

        jSeparator4.setBackground(new java.awt.Color(102, 153, 255));
        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        BackGroundColorButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        BackGroundColorButton.setForeground(new java.awt.Color(255, 255, 255));
        BackGroundColorButton.setToolTipText("BackGround Color");
        BackGroundColorButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BackGroundColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackGroundColorButtonActionPerformed(evt);
            }
        });

        ForeGroundColorButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        ForeGroundColorButton.setToolTipText("ForeGround Color");
        ForeGroundColorButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ForeGroundColorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ForeGroundColorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ButtonPanelLayout = new javax.swing.GroupLayout(ButtonPanel);
        ButtonPanel.setLayout(ButtonPanelLayout);
        ButtonPanelLayout.setHorizontalGroup(
            ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ButtonPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(ButtonPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(BackGroundColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(ButtonPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(ForeGroundColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ButtonPanelLayout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton21))
                    .addGroup(ButtonPanelLayout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(1500, Short.MAX_VALUE))
        );
        ButtonPanelLayout.setVerticalGroup(
            ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ButtonPanelLayout.createSequentialGroup()
                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ButtonPanelLayout.createSequentialGroup()
                        .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                                        .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                            .addComponent(jButton18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(ButtonPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1)
                            .addGroup(ButtonPanelLayout.createSequentialGroup()
                                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(BackGroundColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ForeGroundColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ButtonPanelLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox2)
                                    .addComponent(jLabel2)
                                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Color", ButtonPanel);

        jPanel3.setBackground(new java.awt.Color(172, 188, 218));

        LineButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Draw-Line-32.png"))); // NOI18N
        LineButton.setToolTipText("Line");
        LineButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LineButtonActionPerformed(evt);
            }
        });

        OvalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/mini_circle.png"))); // NOI18N
        OvalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OvalButtonActionPerformed(evt);
            }
        });

        RectangleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Shape-Square-32.png"))); // NOI18N
        RectangleButton.setToolTipText("Rectangle");
        RectangleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RectangleButtonActionPerformed(evt);
            }
        });

        RoundRecButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/rounded-rectangle.png"))); // NOI18N
        RoundRecButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RoundRecButtonActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(172, 188, 218));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        size1.setBackground(new java.awt.Color(172, 188, 218));
        size1.setText(" 1 ");

        size2.setBackground(new java.awt.Color(172, 188, 218));
        size2.setText(" 2");

        size3.setBackground(new java.awt.Color(172, 188, 218));
        size3.setText(" 3");

        size6.setBackground(new java.awt.Color(172, 188, 218));
        size6.setText(" 6");

        size5.setBackground(new java.awt.Color(172, 188, 218));
        size5.setText(" 5");

        size4.setBackground(new java.awt.Color(172, 188, 218));
        size4.setText(" 4");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(size2)
                    .addComponent(size1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(size3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(size5))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(size4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(size6))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(size1)
                    .addComponent(size3)
                    .addComponent(size5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(size4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(size2)
                        .addComponent(size6))))
        );

        jCheckBox1.setBackground(new java.awt.Color(172, 188, 218));
        jCheckBox1.setText("Fill");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/index.jpg"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Draw-Polygon-32.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jCheckBox6.setBackground(new java.awt.Color(172, 188, 218));
        jCheckBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox6ItemStateChanged(evt);
            }
        });

        Dot.setText("Dot");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(LineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OvalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RectangleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RoundRecButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCheckBox1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jCheckBox6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Dot)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1554, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(RoundRecButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(RectangleButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(OvalButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jCheckBox1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Dot, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jCheckBox6)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Shape", jPanel3);

        jPanel6.setBackground(new java.awt.Color(172, 188, 218));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Kit Paint © 2013");

        Position.setBackground(new java.awt.Color(153, 255, 255));
        Position.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Position.setOpaque(true);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel1)
                .addGap(688, 688, 688)
                .addComponent(Position, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1232, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(jLabel1))
            .addComponent(Position, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 617, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BackGroundColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackGroundColorButtonActionPerformed
        SetBackGroundColor();
        repaint();
    }//GEN-LAST:event_BackGroundColorButtonActionPerformed

    private void ForeGroundColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ForeGroundColorButtonActionPerformed
        SetForeGroundColor();
        repaint();
    }//GEN-LAST:event_ForeGroundColorButtonActionPerformed

    private void PencilButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PencilButtonActionPerformed
        setDrawMode(FREEHAND);
        DrawObject();
    }//GEN-LAST:event_PencilButtonActionPerformed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        CurX = evt.getX();
        CurY = evt.getY();
        if (graphics2d != null) {
            if (DRAWMODE == ERASE) {
                graphics2d.setStroke(new BasicStroke(2));
                if (brush1.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(3));
                } else if (brush2.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(5));
                } else if (brush3.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(7));
                } else if (brush4.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(8));
                } else if (brush5.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(10));
                } else if (brush6.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(13));
                }
                graphics2d.setPaint(Color.white);
                graphics2d.drawLine(OldX, OldY, CurX, CurY);
                repaint();
                OldX = CurX;
                OldY = CurY;
            }
            if (DRAWMODE == FREEHAND) {
               // graphics2d.setStroke(drawingStroke);
                graphics2d.setStroke(new BasicStroke(1));
                graphics2d.drawLine(OldX, OldY, CurX, CurY);
                repaint();
                OldX = CurX;
                OldY = CurY;
            }
            if (DRAWMODE == BRUSH) {
                graphics2d.setStroke(new BasicStroke(2));
                if (brush1.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(2));
                } else if (brush2.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(3));
                } else if (brush3.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(4));
                } else if (brush4.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(5));
                } else if (brush5.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(6));
                } else if (brush6.isSelected()) {
                    graphics2d.setStroke(new BasicStroke(7));
                }
                graphics2d.drawLine(OldX, OldY, CurX, CurY);
                repaint();
                OldX = CurX;
                OldY = CurY;
            }
            graphics2d.setStroke(new BasicStroke(1));
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        OldX = evt.getX();
        OldY = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        int NewX, NewY;
        NewX = evt.getX();
        NewY = evt.getY();
        if (DRAWMODE == LINE) {
            if(check==0)
            {
                graphics2d.setStroke(drawingStroke);
                if (size1.isSelected()) {
                    graphics2d.setStroke(drawingStroke);
                } else if (size2.isSelected()) {
                    graphics2d.setStroke(drawingStroke2);
                } else if (size3.isSelected()) {
                    graphics2d.setStroke(drawingStroke3);
                } else if (size4.isSelected()) {
                    graphics2d.setStroke(drawingStroke4);
                } else if (size5.isSelected()) {
                    graphics2d.setStroke(drawingStroke5);
                } else if (size6.isSelected()) {
                    graphics2d.setStroke(drawingStroke6);
                }
            }
            else{
            if (size1.isSelected()) {
                graphics2d.setStroke(new BasicStroke(1));
            } else if (size2.isSelected()) {
                graphics2d.setStroke(new BasicStroke(2));
            } else if (size3.isSelected()) {
                graphics2d.setStroke(new BasicStroke(3));
            } else if (size4.isSelected()) {
                graphics2d.setStroke(new BasicStroke(4));
            } else if (size5.isSelected()) {
                graphics2d.setStroke(new BasicStroke(5));
            } else if (size6.isSelected()) {
                graphics2d.setStroke(new BasicStroke(6));
            }
            //repaint();
            }
            graphics2d.drawLine(OldX, OldY, NewX, NewY);
            repaint();
        }
        if (DRAWMODE == CIRCLE) {
               if(check==0)
               {
                    graphics2d.setStroke(drawingStroke);
                    if (size1.isSelected()) {
                        graphics2d.setStroke(drawingStroke);
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(drawingStroke2);
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(drawingStroke3);
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(drawingStroke4);
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(drawingStroke5);
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(drawingStroke6);
                    }
               }
               else{
                    if (size1.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(1));
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(2));
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(3));
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(4));
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(5));
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(6));
                    }
               }
            if (solid) {
                if (OldX > NewX || OldY > NewY) {
                    graphics2d.fillOval(NewX, NewY, OldX - NewX, OldY - NewY);
                } else {
                    graphics2d.fillOval(OldX, OldY, NewX - OldX, NewY - OldY);
                }
            } else {
                if (OldX > NewX || OldY > NewY) {
                    graphics2d.drawOval(NewX, NewY, OldX - NewX, OldY - NewY);
                } else {
                    graphics2d.drawOval(OldX, OldY, NewX - OldX, NewY - OldY);
                }
            }
            repaint();
        }

        if (DRAWMODE == RECT) {
                if(check==0)
                {
                    graphics2d.setStroke(drawingStroke);
                    if (size1.isSelected()) {
                        graphics2d.setStroke(drawingStroke);
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(drawingStroke2);
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(drawingStroke3);
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(drawingStroke4);
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(drawingStroke5);
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(drawingStroke6);
                    }
                }
                else{
                    if (size1.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(1));
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(2));
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(3));
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(4));
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(5));
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(6));
                    }
                }
            if (solid) {
                if (OldX > NewX || OldY > NewY) {
                    graphics2d.fillRect(NewX, NewY, OldX - NewX, OldY - NewY);
                } else {
                    graphics2d.fillRect(OldX, OldY, NewX - OldX, NewY - OldY);
                }
            } else {
                if (OldX > NewX || OldY > NewY) {
                    graphics2d.drawRect(NewX, NewY, OldX - NewX, OldY - NewY);
                } else {
                    graphics2d.drawRect(OldX, OldY, NewX - OldX, NewY - OldY);
                }
            }
            repaint();
        }
        if (DRAWMODE == ROUNDED_REC) {

            if(check==0)
                {
                    graphics2d.setStroke(drawingStroke);
                    if (size1.isSelected()) {
                        graphics2d.setStroke(drawingStroke);
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(drawingStroke2);
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(drawingStroke3);
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(drawingStroke4);
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(drawingStroke5);
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(drawingStroke6);
                    }
                }
                else{
                    if (size1.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(1));
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(2));
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(3));
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(4));
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(5));
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(6));
                    }
                }

            if (solid) {
                if (OldX > NewX || OldY > NewY) {
                    graphics2d.fillRoundRect(NewX, NewY, OldX - NewX, OldY - NewY, 25, 25);
                } else {
                    graphics2d.fillRoundRect(OldX, OldY, NewX - OldX, NewY - OldY, 25, 25);
                }
            } else {
                if (OldX > NewX || OldY > NewY) {
                    graphics2d.drawRoundRect(NewX, NewY, OldX - NewX, OldY - NewY, 25, 25);
                } else {
                    graphics2d.drawRoundRect(OldX, OldY, NewX - OldX, NewY - OldY, 25, 25);
                }
            }
            repaint();
        }
        if (DRAWMODE == ARC) {
                if(check==0)
                {
                    graphics2d.setStroke(drawingStroke);
                    if (size1.isSelected()) {
                        graphics2d.setStroke(drawingStroke);
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(drawingStroke2);
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(drawingStroke3);
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(drawingStroke4);
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(drawingStroke5);
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(drawingStroke6);
                    }
                }
                else{
                    if (size1.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(1));
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(2));
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(3));
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(4));
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(5));
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(6));
                    }
                }

            if (OldX > NewX || OldY > NewY) {
                graphics2d.drawArc(OldX, OldY, NewX, NewY, 50, 90);
            } else {
                graphics2d.drawArc(NewX, NewY, OldX, OldY, 50, -90);
            }
            repaint();
        }
        if (DRAWMODE == TEXT) {
            String m = jTextField2.getText();

            graphics2d.drawString(m, NewX, NewY);
            repaint();
        }
        if (DRAWMODE == this.POLYGON) {
            
            if(check==0)
                {
                    graphics2d.setStroke(drawingStroke);
                    if (size1.isSelected()) {
                        graphics2d.setStroke(drawingStroke);
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(drawingStroke2);
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(drawingStroke3);
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(drawingStroke4);
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(drawingStroke5);
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(drawingStroke6);
                    }
                }
                else{
                    if (size1.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(1));
                    } else if (size2.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(2));
                    } else if (size3.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(3));
                    } else if (size4.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(4));
                    } else if (size5.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(5));
                    } else if (size6.isSelected()) {
                        graphics2d.setStroke(new BasicStroke(6));
                    }
                }
            
            xPolygon.add(new Integer(evt.getX()));
            yPolygon.add(new Integer(evt.getY()));
            polygonBuffer = true;
            repaint();
        }
        repaint();
        OldX = OldY = NewX = NewY = 0;
    }//GEN-LAST:event_formMouseReleased

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        repaint();
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
         setCursor(new Cursor(Cursor.DEFAULT_CURSOR));    }//GEN-LAST:event_formMouseExited

    private void ButtonPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonPanelMouseEntered
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        repaint();
    }//GEN-LAST:event_ButtonPanelMouseEntered

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        image = createImage(2000, 2000);
        graphics2d = (Graphics2D) image.getGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        clear(1);
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
        int gridHeight = 10;
        int widthHeight = 10;
        Dimension size = getSize();
        double width = size.getWidth();
        double height = size.getHeight();
        if (jCheckBox2.isSelected()) {

            System.out.println(width + " " + height + " " + gridHeight);
            int horizontalLines = (int) height / gridHeight;
            System.out.println(horizontalLines);

            for (int i = 0; i < height; i = i + gridHeight) {
                graphics2d.setColor(Color.lightGray);
                graphics2d.drawLine(0, i, (int) width, i);
            }

            for (int i = 0; i < width; i = i + widthHeight) {
                graphics2d.setColor(Color.lightGray);
                graphics2d.drawLine(i, 0, i, (int) height);

            }
        } else {
            clear();
        }
        repaint();
    }//GEN-LAST:event_jCheckBox2ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setDrawMode(BRUSH);
        DrawObject();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jPanel2.setVisible(true);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String fontString = jComboBox1.getModel().getSelectedItem().toString();
        int fontsz = Integer.parseInt(jComboBox2.getModel().getSelectedItem().toString());
        font = new Font(fontString, fontStyle, fontsz);
        graphics2d.setFont(font);
        jPanel2.setVisible(false);
        setDrawMode(TEXT);
        DrawObject();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jCheckBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox3ItemStateChanged
        fontStyle = Font.BOLD;
    }//GEN-LAST:event_jCheckBox3ItemStateChanged

    private void jCheckBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox4ItemStateChanged
        fontStyle = Font.ITALIC;
    }//GEN-LAST:event_jCheckBox4ItemStateChanged

    private void jCheckBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox5ItemStateChanged
        fontStyle = Font.PLAIN;
    }//GEN-LAST:event_jCheckBox5ItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jPanel2.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void brush2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brush2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_brush2ActionPerformed

    private void brush6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brush6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_brush6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        setDrawMode(ERASE);
        DrawObject();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        graphics2d.setPaint(Color.RED);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        graphics2d.setPaint(Color.WHITE);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        graphics2d.setPaint(Color.BLACK);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        graphics2d.setPaint(Color.YELLOW);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        graphics2d.setPaint(Color.BLUE);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        graphics2d.setPaint(Color.GREEN);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        graphics2d.setPaint(Color.LIGHT_GRAY);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        graphics2d.setPaint(Color.DARK_GRAY);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        graphics2d.setPaint(Color.PINK);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        graphics2d.setPaint(Color.CYAN);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        graphics2d.setPaint(Color.ORANGE);
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        graphics2d.setPaint(Color.MAGENTA);
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (isExistPolygonBuffer() != false) {
            flushPolygonBuffer();
        }
        jPanel4.setVisible(true);
        setDrawMode(POLYGON);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jPanel4.setVisible(true);
        setDrawMode(ARC);
        DrawObject();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        if (jCheckBox1.isSelected()) {
            setSolidMode(Boolean.TRUE);
        } else {
            setSolidMode(Boolean.FALSE);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void RoundRecButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RoundRecButtonActionPerformed
        jPanel4.setVisible(true);
        setDrawMode(ROUNDED_REC);
        DrawObject();
    }//GEN-LAST:event_RoundRecButtonActionPerformed

    private void RectangleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RectangleButtonActionPerformed
        jPanel4.setVisible(true);
        setDrawMode(RECT);
        DrawObject();
    }//GEN-LAST:event_RectangleButtonActionPerformed

    private void OvalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OvalButtonActionPerformed
        jPanel4.setVisible(true);
        setDrawMode(CIRCLE);
        DrawObject();
    }//GEN-LAST:event_OvalButtonActionPerformed

    private void LineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LineButtonActionPerformed
        jPanel4.setVisible(true);
        setDrawMode(LINE);
        DrawObject();
    }//GEN-LAST:event_LineButtonActionPerformed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
    int xco = evt.getX();
    int yco = evt.getY();
    String position = String.format("%d,%d", xco, yco);
    Position.setText(position);
    }//GEN-LAST:event_formMouseMoved

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox6ItemStateChanged
        if(jCheckBox6.isSelected()) check = 0;
        else check = 1;
    }//GEN-LAST:event_jCheckBox6ItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackGroundColorButton;
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JButton ClearButton;
    private javax.swing.JLabel Dot;
    private javax.swing.JButton ForeGroundColorButton;
    private javax.swing.JButton LineButton;
    private javax.swing.JButton OvalButton;
    private javax.swing.JButton PencilButton;
    private javax.swing.JLabel Position;
    private javax.swing.JButton RectangleButton;
    private javax.swing.JButton RoundRecButton;
    private javax.swing.JRadioButton brush1;
    private javax.swing.JRadioButton brush2;
    private javax.swing.JRadioButton brush3;
    private javax.swing.JRadioButton brush4;
    private javax.swing.JRadioButton brush5;
    private javax.swing.JRadioButton brush6;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JRadioButton size1;
    private javax.swing.JRadioButton size2;
    private javax.swing.JRadioButton size3;
    private javax.swing.JRadioButton size4;
    private javax.swing.JRadioButton size5;
    private javax.swing.JRadioButton size6;
    // End of variables declaration//GEN-END:variables
    // This method returns a buffered image with the contents of an image
}
