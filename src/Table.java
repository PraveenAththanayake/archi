import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.*;

public class Table extends JFrame implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private float zDistance = -5.0f;
    private float[] colorArr;
    private int prevMouseX, prevMouseY;
    private float rotateX = 0;
    private float rotateY = 0;
    private FPSAnimator animator;
    private JButton homeButton;
    private String color;

    public Table(String color) {
        setTitle("3D Table Renderer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas glcanvas = new GLCanvas(capabilities);
        glcanvas.addGLEventListener(this);
        glcanvas.addMouseListener(this);
        glcanvas.addMouseMotionListener(this);

        getContentPane().add(glcanvas);
        setSize(800, 600);
        setLocationRelativeTo(null);
        this.color = color;
        setColor(color);
        animator = new FPSAnimator(glcanvas, 60);
        animator.start();
        /// Zoom Functioning
        glcanvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                float zoomFactor = 0.1f; // Adjust this value for more or less sensitivity
                if (notches < 0) {
                    zDistance += zoomFactor; // Zoom in
                } else {
                    zDistance -= zoomFactor; // Zoom out
                }
                glcanvas.repaint(); // Repaint the canvas with the new zoom distance
            }
        });
        homeButton = new JButton("Main Page");
        homeButton.addActionListener(e -> {
            MainFrame mp = new MainFrame();
            mp.setVisible(true);
            this.setVisible(false);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(homeButton);
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
    }

    private void setColor(String color) {
        // Define color array based on the input color.
        float[] brown = {0.5f, 0.35f, 0.05f};
        float[] black = {0.0f, 0.0f, 0.0f};
        float[] Red = {0.5f, 0.0f, 0.0f};
        float[] Blue = {0.0f, 0.0f, 0.5f};
        float[] darkBrown = {0.3f, 0.2f, 0.1f};
        float[] beige = {0.96f, 0.96f, 0.86f};
        float[] tan = {0.82f, 0.71f, 0.55f};
        float[] lightGray = {0.83f, 0.83f, 0.83f};
        float[] ivory = {1.0f, 1.0f, 0.94f};
        float[] sand = {0.76f, 0.70f, 0.50f};
        float[] olive = {0.50f, 0.50f, 0.0f};
        float[] navy = {0.0f, 0.0f, 0.5f};
        float[] teal = {0.0f, 0.5f, 0.5f};

        switch (color.toUpperCase()) {
            case "BLACK":
                colorArr = black;
                break;
            case "RED":
                colorArr = Red;
                break;
            case "BROWN":
                colorArr = brown;
                break;
            case "BLUE":
                colorArr = Blue;
                break;
            case "DARKBROWN":
                colorArr = darkBrown;
                break;
            case "BEIGE":
                colorArr = beige;
                break;
            case "TAN":
                colorArr = tan;
                break;
            case "LIGHTGRAY":
                colorArr = lightGray;
                break;
            case "IVORY":
                colorArr = ivory;
                break;
            case "SAND":
                colorArr = sand;
                break;
            case "OLIVE":
                colorArr = olive;
                break;
            case "NAVY":
                colorArr = navy;
                break;
            case "TEAL":
                colorArr = teal;
                break;
            default:
                colorArr = brown; // Default to brown if no match is found.
                break;
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(1f, 1f, 1f, 1f);
        gl.glEnable(GL.GL_DEPTH_TEST);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        animator.stop();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, zDistance); // Use zDistance for zoom
        gl.glRotatef(rotateX, 1, 0, 0);
        gl.glRotatef(rotateY, 0, 1, 0);

        gl.glColor3fv(colorArr, 0);
        renderTable(gl);

        gl.glFlush();
    }

    private void renderTable(GL2 gl) {
        float tableTopThickness = 0.4f;
        float tableLegThickness = 0.2f;
        float tableLegHeight = 0.7f;
        float tableWidth = 2f;
        float tableDepth = 3f;

        // Draw tabletop
        gl.glColor3fv(colorArr , 0); // Wood color
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-tableWidth / 2, tableTopThickness / 2, -tableDepth / 2);
        gl.glVertex3f(-tableWidth / 2, tableTopThickness / 2, tableDepth / 2);
        gl.glVertex3f(tableWidth / 2, tableTopThickness / 2, tableDepth / 2);
        gl.glVertex3f(tableWidth / 2, tableTopThickness / 2, -tableDepth / 2);
        gl.glEnd();

        // Draw the four legs
        float[] legPositions = new float[]{
                -tableWidth / 2 + tableLegThickness / 2, -tableDepth / 2 + tableLegThickness / 2,
                -tableWidth / 2 + tableLegThickness / 2, tableDepth / 2 - tableLegThickness / 2,
                tableWidth / 2 - tableLegThickness / 2, -tableDepth / 2 + tableLegThickness / 2,
                tableWidth / 2 - tableLegThickness / 2, tableDepth / 2 - tableLegThickness / 2
        };

        for (int i = 0; i < legPositions.length; i += 2) {
            float legX = legPositions[i];
            float legZ = legPositions[i + 1];
            // Draw leg
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(legX - tableLegThickness / 2, -tableLegHeight, legZ - tableLegThickness / 2);
            gl.glVertex3f(legX - tableLegThickness / 2, -tableLegHeight, legZ + tableLegThickness / 2);
            gl.glVertex3f(legX + tableLegThickness / 2, -tableLegHeight, legZ + tableLegThickness / 2);
            gl.glVertex3f(legX + tableLegThickness / 2, -tableLegHeight, legZ - tableLegThickness / 2);

            gl.glVertex3f(legX - tableLegThickness / 2, 0, legZ - tableLegThickness / 2);
            gl.glVertex3f(legX - tableLegThickness / 2, 0, legZ + tableLegThickness / 2);
            gl.glVertex3f(legX + tableLegThickness / 2, 0, legZ + tableLegThickness / 2);
            gl.glVertex3f(legX + tableLegThickness / 2, 0, legZ - tableLegThickness / 2);

            gl.glVertex3f(legX - tableLegThickness / 2, -tableLegHeight, legZ - tableLegThickness / 2);
            gl.glVertex3f(legX - tableLegThickness / 2, 0, legZ - tableLegThickness / 2);
            gl.glVertex3f(legX - tableLegThickness / 2, 0, legZ + tableLegThickness / 2);
            gl.glVertex3f(legX - tableLegThickness / 2, -tableLegHeight, legZ + tableLegThickness / 2);

            gl.glVertex3f(legX + tableLegThickness / 2, -tableLegHeight, legZ + tableLegThickness / 2);
            gl.glVertex3f(legX + tableLegThickness / 2, 0, legZ + tableLegThickness / 2);
            gl.glVertex3f(legX + tableLegThickness / 2, 0, legZ - tableLegThickness / 2);
            gl.glVertex3f(legX + tableLegThickness / 2, -tableLegHeight, legZ - tableLegThickness / 2);
            gl.glEnd();
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        double aspect = (double) width / (double) height;
        gl.glFrustum(-aspect, aspect, -1.0, 1.0, 2.0, 10.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glScalef(1.0f, -1.0f, 1.0f);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Optional: Implement this if needed
    }

    @Override
    public void mousePressed(MouseEvent e) {
        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Optional: Implement this if needed
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Optional: Implement this if needed
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Optional: Implement this if needed
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getX() - prevMouseX;
        int deltaY = e.getY() - prevMouseY;

        rotateY += deltaX * 0.1f;
        rotateX += deltaY * 0.1f;

        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Optional: Implement this if needed
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        float zoomFactor = 0.1f; // Adjust this value for more or less sensitivity
        if (notches < 0) {
            zDistance += zoomFactor; // Zoom in
        } else {
            zDistance -= zoomFactor; // Zoom out
        }
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Table table = new Table("Brown");
            table.setVisible(true);
        });
    }
}
