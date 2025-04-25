import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class TypeTwo extends JFrame implements GLEventListener, MouseListener, MouseMotionListener {

    private static final long serialVersionUID = 1L;
    private boolean isDraggingChair = false;
    private boolean isDraggingTable = false;
    private int prevX, prevY;
    private int chairX = 0, chairY = 0; // Position of the chair
    private int tableX = 0, tableY = 0; // Position of the table
    private float tableScale = 1;
    private float chairScale = 1;
    private int windowWidth = 1200; // Width of the window
    private int windowHeight = 900; // Height of the window

    private String tableColor;
    private String chairColor;


    private int floorMarginX = 75;
    private int floorMarginY = 160;
    private String floorColor;

    public TypeTwo(float tableScale , float chairScale , String tableColor , String chairColor , int floorMarginX , int floorMarginY , String floorColor)  {
        this.tableScale = tableScale;
        this.chairScale = chairScale;
        this.tableColor = tableColor;
        this.chairColor = chairColor;
        this.floorMarginX = floorMarginX;
        this.floorMarginY = floorMarginY;
        this.floorColor = floorColor;

        setTitle("Floor One Renderer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLJPanel gljpanel = new GLJPanel(capabilities);
        gljpanel.addGLEventListener(this);
        gljpanel.addMouseListener(this);
        gljpanel.addMouseMotionListener(this);

        getContentPane().add(gljpanel);

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> resetFurniturePosition());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(homeButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
    }

    private void resetFurniturePosition() {
        this.setVisible(false);
        MainFrame mp = new MainFrame();
        mp.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypeTwo renderer = new TypeTwo(1 , 1 , "brown" ,  "brown" , 75 , 160 , "Tan");
            renderer.setVisible(true);
        });
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // Background color
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Cleanup
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);


        drawFloorSq(gl , floorColor);
        // Draw furniture
        drawChair(gl, chairX, chairY , chairScale , chairColor);
        drawTable(gl, tableX, tableY , tableScale , tableColor);

        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, windowWidth, windowHeight, 0, -1.0, 1.0); // Adjust as necessary
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private  void drawFloorSq(GL2 gl , String color){
        // Draw floor
        if (color.equalsIgnoreCase("Turquoise")) {
            gl.glColor3f(0.25f, 0.88f, 0.82f); // Turquoise color
        } else if (color.equalsIgnoreCase("Lavender")) {
            gl.glColor3f(0.71f, 0.49f, 0.86f); // Lavender color
        } else if (color.equalsIgnoreCase("Crimson")) {
            gl.glColor3f(0.86f, 0.08f, 0.24f); // Crimson color
        } else if (color.equalsIgnoreCase("Amber")) {
            gl.glColor3f(1.0f, 0.75f, 0.0f); // Amber color
        } else if (color.equalsIgnoreCase("Emerald")) {
            gl.glColor3f(0.31f, 0.78f, 0.47f); // Emerald color
        } else {
            gl.glColor3f(0.82f, 0.71f, 0.55f); // Default color (Cedar_Brown)
        }

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(floorMarginX, floorMarginY); // Left-bottom
        gl.glVertex2f(windowWidth-floorMarginX, floorMarginY); // Right-bottom
        gl.glVertex2f(windowWidth -floorMarginX, windowHeight -floorMarginY); // Right-top75
        gl.glVertex2f(floorMarginX, windowHeight -floorMarginY); // Left-top
        gl.glEnd();
    }
    private void drawChair(GL2 gl, int x, int y, float scale , String color) {

        if(color.equalsIgnoreCase("BLACK")){
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }else if(color.equalsIgnoreCase("WHITE")){
            gl.glColor3f(1.0f, 1.0f, 1.0f);
        }else if(color.equalsIgnoreCase("GRAY")){
            gl.glColor3f(0.5f, 0.5f, 0.5f);
        }else if(color.equalsIgnoreCase("SILVER")){
            gl.glColor3f(0.75f, 0.75f, 0.75f);
        }else if(color.equalsIgnoreCase("RED")){
            gl.glColor3f(1.0f, 0.0f, 0.0f);
        }else if(color.equalsIgnoreCase("MAROON")){
            gl.glColor3f(0.5f, 0.0f, 0.0f);
        }else if(color.equalsIgnoreCase("YELLOW")){
            gl.glColor3f(1.0f, 1.0f, 0.0f);
        }else if(color.equalsIgnoreCase("OLIVE")){
            gl.glColor3f(0.5f, 0.5f, 0.0f);
        }else if(color.equalsIgnoreCase("LIME")){
            gl.glColor3f(0.0f, 1.0f, 0.0f);
        }else if(color.equalsIgnoreCase("GREEN")){
            gl.glColor3f(0.0f, 0.5f, 0.0f);
        }else if(color.equalsIgnoreCase("AQUA")){
            gl.glColor3f(0.0f, 1.0f, 1.0f);
        }else if(color.equalsIgnoreCase("TEAL")){
            gl.glColor3f(0.0f, 0.5f, 0.5f);
        }else if(color.equalsIgnoreCase("BLUE")){
            gl.glColor3f(0.0f, 0.0f, 1.0f);
        }else if(color.equalsIgnoreCase("NAVY")){
            gl.glColor3f(0.0f, 0.0f, 0.5f);
        }else if(color.equalsIgnoreCase("FUCHSIA")){
            gl.glColor3f(1.0f, 0.0f, 1.0f);
        }else if(color.equalsIgnoreCase("PURPLE")){
            gl.glColor3f(0.5f, 0.0f, 0.5f);
        }else if(color.equalsIgnoreCase("ORANGE")){
            gl.glColor3f(1.0f, 0.65f, 0.0f);
        }else{
            gl.glColor3f(0.5f, 0.35f, 0.05f); // Default to Brown if no match
        }
        // Draw chair seat

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f((100 * scale) + x, (150 * scale) + y); //leftUpper
        gl.glVertex2f((180 * scale) + x, (150 * scale) + y); // rightUpper
        gl.glVertex2f((180 * scale) + x, (180 * scale) + y); // rightDown
        gl.glVertex2f((100 * scale) + x, (180 * scale) + y); // leftDown
        gl.glEnd();

        // Draw chair back
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f((100 * scale) + x, (100 * scale) + y);
        gl.glVertex2f((130 * scale) + x, (100 * scale) + y);
        gl.glVertex2f((130 * scale) + x, (150 * scale) + y);
        gl.glVertex2f((100 * scale) + x, (150 * scale) + y);
        gl.glEnd();

        // Draw chair legs
        gl.glColor3f(0.3f, 0.3f, 0.3f); // Gray color
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f((110 * scale) + x, (180 * scale) + y); // Bottom
        gl.glVertex2f((120 * scale) + x, (180 * scale) + y);
        gl.glVertex2f((120 * scale) + x, (230 * scale) + y); // Top
        gl.glVertex2f((110 * scale) + x, (230 * scale) + y);

        gl.glVertex2f((160 * scale) + x, (180 * scale) + y); // Bottom
        gl.glVertex2f((170 * scale) + x, (180 * scale) + y);
        gl.glVertex2f((170 * scale) + x, (230 * scale) + y); // Top
        gl.glVertex2f((160 * scale) + x, (230 * scale) + y);
        gl.glEnd();
    }


    private void drawTable(GL2 gl, int x, int y, float scale , String color) {

        if(color.equalsIgnoreCase("BLACK")){
            gl.glColor3f(0.0f, 0.0f, 0.0f);
        }else if(color.equalsIgnoreCase("WHITE")){
            gl.glColor3f(1.0f, 1.0f, 1.0f);
        }else if(color.equalsIgnoreCase("GRAY")){
            gl.glColor3f(0.5f, 0.5f, 0.5f);
        }else if(color.equalsIgnoreCase("SILVER")){
            gl.glColor3f(0.75f, 0.75f, 0.75f);
        }else if(color.equalsIgnoreCase("RED")){
            gl.glColor3f(1.0f, 0.0f, 0.0f);
        }else if(color.equalsIgnoreCase("MAROON")){
            gl.glColor3f(0.5f, 0.0f, 0.0f);
        }else if(color.equalsIgnoreCase("YELLOW")){
            gl.glColor3f(1.0f, 1.0f, 0.0f);
        }else if(color.equalsIgnoreCase("OLIVE")){
            gl.glColor3f(0.5f, 0.5f, 0.0f);
        }else if(color.equalsIgnoreCase("LIME")){
            gl.glColor3f(0.0f, 1.0f, 0.0f);
        }else if(color.equalsIgnoreCase("GREEN")){
            gl.glColor3f(0.0f, 0.5f, 0.0f);
        }else if(color.equalsIgnoreCase("AQUA")){
            gl.glColor3f(0.0f, 1.0f, 1.0f);
        }else if(color.equalsIgnoreCase("TEAL")){
            gl.glColor3f(0.0f, 0.5f, 0.5f);
        }else if(color.equalsIgnoreCase("BLUE")){
            gl.glColor3f(0.0f, 0.0f, 1.0f);
        }else if(color.equalsIgnoreCase("NAVY")){
            gl.glColor3f(0.0f, 0.0f, 0.5f);
        }else if(color.equalsIgnoreCase("FUCHSIA")){
            gl.glColor3f(1.0f, 0.0f, 1.0f);
        }else if(color.equalsIgnoreCase("PURPLE")){
            gl.glColor3f(0.5f, 0.0f, 0.5f);
        }else if(color.equalsIgnoreCase("ORANGE")){
            gl.glColor3f(1.0f, 0.65f, 0.0f);
        }else{
            gl.glColor3f(0.5f, 0.35f, 0.05f); // Default to Brown if no match
        }
        // Draw table top

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f((100 * scale) + x, (400 * scale) + y); // leftUpper
        gl.glVertex2f((200 * scale) + x, (400 * scale) + y); // rightUpp
        gl.glVertex2f((200 * scale) + x, (430 * scale) + y); // rightDown
        gl.glVertex2f((100 * scale) + x, (430 * scale) + y); // leftDown
        gl.glEnd();

        // Draw table legs
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f((110 * scale) + x, (430 * scale) + y); // Bottom
        gl.glVertex2f((120 * scale) + x, (430 * scale) + y);
        gl.glVertex2f((120 * scale) + x, (470 * scale) + y); // Top
        gl.glVertex2f((110 * scale) + x, (470 * scale) + y);

        gl.glVertex2f((180 * scale) + x, (430 * scale) + y); // Bottom
        gl.glVertex2f((190 * scale) + x, (430 * scale) + y);
        gl.glVertex2f((190 * scale) + x, (470 * scale) + y); // Top
        gl.glVertex2f((180 * scale) + x, (470 * scale) + y);
        gl.glEnd();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // Check if the mouse is clicked on the chair
        if (x >= (100 * chairScale) + chairX && x <= (180 * chairScale) + chairX &&
                y >= (100 * chairScale) + chairY && y <= (230 * chairScale) + chairY) {
            isDraggingChair = true;
        }

        // Check if the mouse is clicked on the table
        if (x >= (100 * tableScale) + tableX && x <= (200 * tableScale) + tableX &&
                y >= (400 * tableScale) + tableY && y <= (470 * tableScale) + tableY) {
            isDraggingTable = true;
        }

        prevX = x;
        prevY = y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDraggingChair = false;
        isDraggingTable = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (isDraggingChair) {
            if(x <= floorMarginX + 105 || x >= windowWidth - (floorMarginX + 90)) {
                // Don't update chairX
            } else {
                chairX += x - prevX;
            }

            if(y >= windowHeight - (floorMarginY + 140) || y <= (floorMarginY + 140)) {
                // Don't update chairY
            } else {
                chairY += y - prevY;
            }
        }

        if (isDraggingTable) {
            if(x <= floorMarginX + 105 || x >= windowWidth - (floorMarginX + 90)) {
                // Don't update tableX
            } else {
                tableX += x - prevX;
            }

            if(y >= windowHeight - (floorMarginY + 140) || y <= (floorMarginY + 140)) {
                // Don't update tableY
            } else {
                tableY += y - prevY;
            }
        }

        prevX = x;
        prevY = y;

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}