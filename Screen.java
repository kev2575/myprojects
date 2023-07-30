import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
public class Screen implements ActionListener {
    private JFrame frame;
    private BufferedImage canvas; // where the lines are drawn.
    public final static int black = Color.BLACK.getRGB();
    private JTextArea textBox;
    private ImageIcon img;
    private JPanel panel;
    private Graphics graphics; // a class that holds the algorithms
    private final double Vsx = 511.5;
    private final double Vcx = 511.5;
    private final double Vcy = 300;
    private final double Vsy = 300;
    private double x = 6;
    private double y = 8;
    private double z = 7.5;
    private double s = 15;
    private double d = 60;
    /**
     * It constructs a screen that has a canvas where the program draws lines and allows you select
     * the algorithm you want to use and write the number of random lines you want to have drawn.
     * It then has a button that will draw the lines based on the input.
     */
    public Screen() {
        graphics = new Graphics(); // holds the line algorithm and actually draws the lines.
        frame = new JFrame("graphics");
        panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // uses a grid layout to make the gui
        panel.setBorder(BorderFactory.createEmptyBorder());
        canvas = new BufferedImage(1024, 600, BufferedImage.TYPE_INT_RGB);
        changeBackground(canvas); // changes the background to white.
        GridBagConstraints constraints = new GridBagConstraints(); // the constraints determine where the objects are placed
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.NORTH;
        textBox = new JTextArea("Example command \"inputLines test1.txt 8 12\""); // where you put in the number of lines
        textBox.setPreferredSize(new Dimension(500,50));
        img = new ImageIcon(canvas);
        JLabel label = new JLabel(img);
        JButton button = new JButton("input");
        JButton help = new JButton("Help");
        JLabel text = new JLabel("Please enter a valid command");
        button.addActionListener(this);
        help.addActionListener(this);
        // adds all components to the panel then adds the panel to the frame
        panel.add(help, constraints);
        constraints.gridy = 1;
        panel.add(label, constraints);
        constraints.gridy = 2;
        panel.add(text, constraints);
        constraints.gridy = 3;
        panel.add(textBox, constraints);
        constraints.gridy = 4;
        panel.add(button,constraints);
        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    } // screen
    public static void main(String[] args) {
        Screen screen = new Screen();
        /*screen.graphics.InputLines("test1.txt", 8, 12);
        screen.graphics.worldConversion(6, 8, 7.5, 60, 15);
        screen.graphics.twoDConversion(511.5, 511.5,300, 300);
        screen.graphics.display(screen.canvas);
        screen.panel.repaint(); */
    } // main
    /**
     * changes the background of the canvas to white basically removing all lines.
     * @param canvas -the component where the lines are drawn.
     */
    public static void changeBackground(BufferedImage canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j<height; j++) {
                canvas.setRGB(i, j, (Color.WHITE.getRGB()));
            } // for
        } // for
    } //changeBackground
    /**
     *  when the input button gets pressed then it takes the command and performs the action.
     * 
     * @param e- the action event that is thrown when the button is clicked.
     */
     public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equals("input")) {
            changeBackground(canvas);
            panel.repaint();
            //System.out.println(textBox.getText());
            String[] commands = textBox.getText().split(" ");
            //System.out.println(commands[0]);
            if(commands[0].equalsIgnoreCase("inputLines")) { //input function
                try {
                runNow(() -> inputLine(commands[1], commands[2], commands[3]));
                } catch(Exception a) {
                    System.out.println(a.getMessage());
                }
            } else if(commands[0].equalsIgnoreCase("outputLines")) { // output function
                try {
                    runNow(() -> output(commands[1], commands[2], commands[3]));
                } catch(Exception a) {
                    System.out.println(a.getMessage());
                }
            } else if(commands[0].equalsIgnoreCase("Display")) { // Display
                runNow(() -> graphics.display(canvas));
                panel.repaint();
            } else if(commands[0].equalsIgnoreCase("Translate")) { // translate
                try {
                runNow(() -> translate(commands[1], commands[2], commands[3]));
                } catch(Exception a) {
                    System.out.println(a.getMessage());
                } // try
            } else if(commands[0].equalsIgnoreCase("Scale")) { // scale
                try {
                    runNow(() -> scale(commands[1], commands[2], commands[3], commands[4], commands[5], commands[6]));
                } catch(Exception a) {
                    System.out.println(a.getMessage());
                }  // try
            } else if(commands[0].equalsIgnoreCase("Rotate")) { //rotate
                try {
                    runNow(() -> rotate(commands[1], commands[2]));
                } catch(Exception a) {
                    System.out.println(a.getMessage());
                }  // try
            }else if(commands[0].equalsIgnoreCase("inputdata")) { //input data
                try {
                    runNow(() -> inputData(commands[1], commands[2], commands[3], commands[4], commands[5]));
                } catch(Exception a) {
                    System.out.println(a.getMessage());
                }  // try
            } else {
                JOptionPane.showMessageDialog(null, 
                              "not a valid command. please put in a valid command", 
                              "Warning", 
                              JOptionPane.WARNING_MESSAGE);
            }
        } else if(e.getActionCommand().equalsIgnoreCase("Help")) {
            help();
        } // if
    } // actionPerformed
    /**
     * creates a thread.
     * @param target is what is being run on the thread.
     */
    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
            t.setDaemon(true);
            t.start();
    } // runNow
    /**
     *  a help message to help the user put in commands.
     */
    public void help() {
        String message = "for using this graphics interface you need to type in commands in the text box.\n"
        + "you must type in the exact command with the appropriate data after it for the program to work.\n"
        + "The first command is inputLines which takes lines from a file and displays it\n" 
        + "The way to type it is \"inputLines filename number of coordinates number of lines\" It takes three arguments.\n"
        + "The next command is outputLines which takes the data from the graphics class and puts it in a file\n"
        + "The way to type it is \"outputLines filename  number of coordinates number of lines\" it takes three arguments.\n"
        + "The next command is Display which displays the lines in the array which is part of the graphics\n"
        + "you just need to type \"display\"\n"
        + "The next command is Scale which scales based on a center point\n"
        + "The way to type it is \"scale Sx Sy Sz Cx Cy Cz\" It takes 6 doubles\n"
        + "The next command is translate which moves the object across the screen\n"
        +"The way to type it is\"translate Tx Ty Tz \" It takes three doubles\n"
        + "The next command is rotate which rotates the object around a axis\n"
        + "The way to type it is \"rotate degree axis\" options for axis is x y z It takes a double and a string\n"
        + "The next command is inputdata which changes the x y z of the viewpoint and can change the d  and s\n"
        + "which are the distance from the screen and screen size.\n"
        + "The way to type the command is \"inputdata x y z d s\" It takes five doubles.\n"
        +"The commands are not case sensitive.\n";
        JOptionPane.showMessageDialog(null, 
        message, 
        "Help", 
        JOptionPane.WARNING_MESSAGE);
    } // help
    /**
     * parses the commands the user gives and then uses the graphics methods to
     * display the image in a file onto the screen.
     * @param file -a file the user gives that will be opened and the info taken out of.
     * @param numCor - number of coordinates
     * @param numLines - number of lines.
     */
    public void inputLine(String file, String numCor, String numLines) {
        int numC = Integer.parseInt(numCor);
        int numL = Integer.parseInt(numLines);
        graphics.InputLines(file, numC, numL);
        graphics.worldConversion(x, y, z, d, s);
        graphics.twoDConversion(Vsx, Vcx,Vsy, Vcy);
        graphics.display(canvas);
        panel.repaint();
    }
    /**
     * outputs the coordinates in the eye coordinate system.
     * @param file -the file where the information will go.
     * @param numCor - number of coordinates
     * @param numLines - number of lines.
     */
    public void output(String file, String numCor, String numLines) {
        int numC = Integer.parseInt(numCor);
        int numL = Integer.parseInt(numLines);
        graphics.outputLines(file, numC, numL);
    }
    /**
     *  parses the info from the user and then translates the image.
     * @param tx - the number x is translated by
     * @param ty - the number y is translated by
     * @param tz - the number z is translated by
     */
    public void translate(String tx, String ty, String tz) {
        double x = Double.parseDouble(tx);
        double y = Double.parseDouble(ty);
        double z = Double.parseDouble(tz);
        graphics.translate(x, y, z);
        graphics.worldConversion(this.x, this.y, this.z, d, s);
        graphics.twoDConversion(Vsx, Vcx,Vsy, Vcy);
        graphics.display(canvas);
        panel.repaint();
    }
    /**
     *  parses the information given by the user then scales the image.
     * @param sx - the number x is scaled by
     * @param sy - the number y is scaled by
     * @param sz - the number z is scaled by
     * @param cx the x part of the arbitary point.
     * @param cy the y part of the arbitary point.
     * @param cz the z part of the arbitary point.
     */
    public void scale(String sx, String sy, String sz, String cx, String cy, String cz) {
        double scaleX = Double.parseDouble(sx);
        double scaleY = Double.parseDouble(sy);
        double scaleZ = Double.parseDouble(sz);
        double centerX = Double.parseDouble(cx);
        double centerY = Double.parseDouble(cy);
        double centerZ = Double.parseDouble(cz);
        graphics.scale(scaleX, scaleY, scaleZ, centerX, centerY, centerZ);
        graphics.worldConversion(x, y, z, d, s);
        graphics.twoDConversion(Vsx, Vcx,Vsy, Vcy);
        graphics.display(canvas);
        panel.repaint();
    }
    /**
     * rotates the image by a certain angle and axis.
     * @param angle - angle the object is rotated by.
     * @param axis - the axis the object is rotated about.
     */
    public void rotate(String angle, String axis) {
        double ang = Double.parseDouble(angle);
        graphics.rotation(ang, axis);
        graphics.worldConversion(x, y, z, d, s);
        graphics.twoDConversion(Vsx, Vcx,Vsy, Vcy);
        graphics.display(canvas);
        panel.repaint();
    }
    /**
     *  allows the user to change the paramters of the object.
     * @param x - the x part of the viewpoint coordinate.
     * @param y - the y part of the viewpoint coordinate.
     * @param z - the z part of the viewpoint coordinate.
     * @param d - the distance from the screen.
     * @param s - screen size.
     */
    public void inputData(String x, String y, String z, String d, String s) {
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
        this.z = Double.parseDouble(z);
        this.d = Double.parseDouble(d);
        this.s = Double.parseDouble(s);
        graphics.worldConversion(this.x, this.y, this.z, this.d, this.s);
        graphics.twoDConversion(Vsx, Vcx,Vsy, Vcy);
        graphics.display(canvas);
        panel.repaint();
    }
} // Screen