import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Field;

import com.jogamp.nativewindow.awt.AWTGraphicsConfiguration;
import com.jogamp.nativewindow.awt.AWTGraphicsScreen;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;

public class Chair extends JFrame implements GLEventListener, MouseListener, MouseMotionListener {

    private static final long serialVersionUID = 1L;
    private int prevMouseX, prevMouseY;
    private float rotateX = 0;
    private float rotateY = 0;
    private FPSAnimator animator;
    private String color;

    private float[] colorArr;
    private float zDistance = -5.0f;
    private JButton homeButton;
    private GLCanvas glcanvas;

    public Chair(String color) {
        setTitle("3D Chair Renderer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Home button (create this early to avoid null pointer issues)
        homeButton = new JButton("Main Page");
        homeButton.addActionListener(e -> {
            try {
                if (animator != null && animator.isAnimating()) {
                    animator.stop();
                }
                MainFrame mp = new MainFrame();
                mp.setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error returning to main page: " + ex.getMessage(),
                        "Navigation Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(homeButton);
        getContentPane().add(buttonPanel, BorderLayout.NORTH);

        // Set color before initializing OpenGL
        this.color = color;
        setColor(color);

        // Initialize JOGL with robust error handling
        try {
            // Set up native libraries - do this first
            setupNativeLibraries();

            // Create and initialize OpenGL components
            initializeOpenGL();

            // Add the canvas to the frame
            setSize(800, 600);
            setLocationRelativeTo(null);

            // Add zoom functionality
            addZoomFunctionality();

            // Add status panel
            JPanel statusPanel = new JPanel();
            statusPanel.add(new JLabel("3D Chair: " + color));
            getContentPane().add(statusPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error initializing OpenGL: " + e.getMessage(),
                    "OpenGL Error",
                    JOptionPane.ERROR_MESSAGE);
            // Create a fallback panel with an error message instead of crashing
            createFallbackPanel();
        }
    }

    /**
     * Set up native libraries for OpenGL with better error handling
     */
    private void setupNativeLibraries() {
        try {
            // First, try to use the natives directory
            String projectDir = System.getProperty("user.dir");
            String nativesPath = projectDir + File.separator + "natives" + File.separator + "windows-amd64";
            String libPath = projectDir + File.separator + "lib";

            // Also check specific path from your error log
            String specificPath = "C:\\Users\\athth\\Downloads\\main-assessment-group-36-main\\lib";

            // Check if natives directory exists
            File nativesDir = new File(nativesPath);
            if (nativesDir.exists() && nativesDir.isDirectory()) {
                System.out.println("Found natives directory: " + nativesPath);
                System.setProperty("java.library.path", nativesPath + File.pathSeparator + System.getProperty("java.library.path", ""));
            } else {
                // Fall back to lib directory
                File libDir = new File(libPath);
                if (libDir.exists() && libDir.isDirectory()) {
                    System.out.println("Using lib directory: " + libPath);
                    System.setProperty("java.library.path", libPath + File.pathSeparator + System.getProperty("java.library.path", ""));
                } else if (new File(specificPath).exists()) {
                    System.out.println("Using specific path: " + specificPath);
                    System.setProperty("java.library.path", specificPath + File.pathSeparator + System.getProperty("java.library.path", ""));
                }
            }

            // Set JOGL-specific system properties
            System.setProperty("jogamp.gluegen.UseTempJarCache", "false");
            System.setProperty("jogamp.common.Debug", "true");

            // Force Java to look at the java.library.path again
            try {
                Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
                sysPathsField.setAccessible(true);
                sysPathsField.set(null, null);
            } catch (Exception e) {
                System.err.println("Failed to reset library path: " + e.getMessage());
            }

            // Print the current library path
            System.out.println("Java library path: " + System.getProperty("java.library.path"));

        } catch (Exception e) {
            System.err.println("Error setting up native libraries: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Attempt to load native libraries directly
     */
    private void loadNativeLibraries() {
        try {
            System.loadLibrary("gluegen_rt");
            System.out.println("Successfully loaded gluegen_rt");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load gluegen_rt: " + e.getMessage());
        }

        try {
            System.loadLibrary("jogl_desktop");
            System.out.println("Successfully loaded jogl_desktop");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load jogl_desktop: " + e.getMessage());
        }

        try {
            System.loadLibrary("nativewindow_awt");
            System.out.println("Successfully loaded nativewindow_awt");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load nativewindow_awt: " + e.getMessage());
        }

        try {
            System.loadLibrary("nativewindow_win32");
            System.out.println("Successfully loaded nativewindow_win32");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load nativewindow_win32: " + e.getMessage());
        }
    }

    // Fixed initializeOpenGL method to handle the ArrayIndexOutOfBoundsException
    // Replace the initializeOpenGL method in the Chair class with this fixed version
    private void initializeOpenGL() {
        try {
            // Set system properties to improve compatibility
            System.setProperty("jogl.disable.openglcore", "true");

            // First check if any OpenGL profile is available at all
            if (!GLProfile.isAvailable(GLProfile.GL2)) {
                throw new GLException("No GL2 profile available on this system");
            }

            // Explicitly get GL2 profile instead of trying multiple profiles
            GLProfile profile = GLProfile.get(GLProfile.GL2);
            System.out.println("Using GL2 profile");

            // Set up minimal capabilities
            GLCapabilities capabilities = new GLCapabilities(profile);
            capabilities.setDoubleBuffered(true);
            capabilities.setSampleBuffers(false);

            // Create canvas with explicit capabilities
            glcanvas = new GLCanvas(capabilities);

            // Add canvas to content pane
            getContentPane().add(glcanvas, BorderLayout.CENTER);

            // Add listeners manually
            glcanvas.addGLEventListener(this);
            glcanvas.addMouseListener(this);
            glcanvas.addMouseMotionListener(this);

            // Start animator
            animator = new FPSAnimator(glcanvas, 30);
            animator.start();

        } catch (Exception e) {
            System.err.println("Failed to initialize OpenGL: " + e.getMessage());
            e.printStackTrace();
            createFallbackPanel();
        }
    }

    private void addZoomFunctionality() {
        glcanvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                float zoomFactor = 0.1f; // Adjust for sensitivity
                if (notches < 0) {
                    zDistance += zoomFactor; // Zoom in
                } else {
                    zDistance -= zoomFactor; // Zoom out
                }
                // Limit zoom range for better usability
                zDistance = Math.max(-10.0f, Math.min(-2.0f, zDistance));
                glcanvas.repaint();
            }
        });
    }

    private void createFallbackPanel() {
        // Clear any existing components
        getContentPane().removeAll();

        // Create a panel with error message
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel errorLabel = new JLabel("3D view could not be initialized.", JLabel.CENTER);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        errorLabel.setForeground(Color.RED);

        JTextArea errorDetails = new JTextArea(
                "Unable to initialize OpenGL for 3D rendering.\n\n" +
                        "This may be due to:\n" +
                        "- Missing or incompatible graphics drivers\n" +
                        "- Insufficient system resources\n" +
                        "- Missing library files\n\n" +
                        "Please check that your system meets the requirements."
        );
        errorDetails.setEditable(false);
        errorDetails.setBackground(null);
        errorDetails.setLineWrap(true);
        errorDetails.setWrapStyleWord(true);

        JButton backButton = new JButton("Return to Main Page");
        backButton.addActionListener(e -> {
            MainFrame mp = new MainFrame();
            mp.setVisible(true);
            this.dispose();
        });

        errorPanel.add(errorLabel, BorderLayout.NORTH);
        errorPanel.add(errorDetails, BorderLayout.CENTER);
        errorPanel.add(backButton, BorderLayout.SOUTH);

        getContentPane().add(errorPanel);
        validate();
        repaint();
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

    public static void main(String[] args) {
        // Set system properties before anything else
        System.setProperty("sun.awt.noerasebackground", "true");
        System.setProperty("sun.java2d.noddraw", "true");
        System.setProperty("sun.java2d.opengl", "true");

        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                String chairColor = "Brown";
                if (args.length > 0) {
                    chairColor = args[0];
                }
                Chair renderer = new Chair(chairColor);
                renderer.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error creating 3D view: " + e.getMessage(),
                        "OpenGL Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        try {
            GL2 gl = drawable.getGL().getGL2();
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // White background
            gl.glEnable(GL2.GL_DEPTH_TEST);

            // Enable smooth shading for better appearance
            gl.glShadeModel(GL2.GL_SMOOTH);

            // Setup lighting
            setupLighting(gl);
        } catch (Exception e) {
            System.err.println("Error in init method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupLighting(GL2 gl) {
        // Basic lighting setup for better appearance
        float[] lightPosition = {10.0f, 10.0f, 10.0f, 1.0f};
        float[] whiteLight = {0.8f, 0.8f, 0.8f, 1.0f};
        float[] ambientLight = {0.2f, 0.2f, 0.2f, 1.0f};

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, whiteLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        if (animator != null && animator.isAnimating()) {
            animator.stop();
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        try {
            GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, zDistance); // Use zDistance for zoom
            gl.glRotatef(rotateX, 1, 0, 0);
            gl.glRotatef(rotateY, 0, 1, 0);

            // Render 3D chair
            renderChair(gl);

            gl.glFlush();
        } catch (Exception e) {
            System.err.println("Error in display method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void renderChair(GL2 gl) {
        // Set material properties for the chair
        float[] materialAmbient = {colorArr[0]*0.3f, colorArr[1]*0.3f, colorArr[2]*0.3f, 1.0f};
        float[] materialDiffuse = {colorArr[0], colorArr[1], colorArr[2], 1.0f};
        float[] materialSpecular = {0.3f, 0.3f, 0.3f, 1.0f};
        float materialShininess = 50.0f;

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, materialAmbient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, materialDiffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, materialSpecular, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, materialShininess);

        // Increase the y-coordinate for the seat
        float seatHeight = 0.2f; // Height of the seat
        float seatWidth = 1.2f; // Width of the seat
        float seatDepth = 1.0f; // Depth of the seat

        // Seat
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, seatHeight, 0.0f); // Translate to raise the seat
        drawCube(gl, seatWidth, seatHeight, seatDepth);
        gl.glPopMatrix();

        // Backrest - assuming the backrest is just above the seat
        gl.glPushMatrix();
        float backrestHeight = 0.9f;
        float backrestWidth = seatWidth;
        float backrestDepth = 0.1f;
        gl.glTranslatef(0.0f, seatHeight * 2 + backrestHeight / 2, seatDepth / 2 - backrestDepth/2);
        drawCube(gl, backrestWidth, backrestHeight, backrestDepth);
        gl.glPopMatrix();

        // Legs
        float legHeight = 0.8f; // Height of the legs
        float legWidth = 0.1f; // Width of the legs
        float legDepth = 0.1f; // Depth of the legs
        float[] legPositionsX = {-(seatWidth / 2 - legWidth/2), (seatWidth / 2 - legWidth/2)};
        float[] legPositionsZ = {-(seatDepth / 2 - legDepth/2), (seatDepth / 2 - legDepth/2)};

        for (float x : legPositionsX) {
            for (float z : legPositionsZ) {
                gl.glPushMatrix();
                gl.glTranslatef(x, -legHeight / 2, z);
                drawCube(gl, legWidth, legHeight, legDepth);
                gl.glPopMatrix();
            }
        }
    }

    private void drawCube(GL2 gl, float width, float height, float depth) {
        float w = width / 2;
        float h = height / 2;
        float d = depth / 2;

        gl.glBegin(GL2.GL_QUADS); // Begin drawing the cube with quads

        // Top Face
        gl.glNormal3f(0.0f, 1.0f, 0.0f); // Normal pointing up
        gl.glVertex3f(-w, h, -d);
        gl.glVertex3f(-w, h, d);
        gl.glVertex3f(w, h, d);
        gl.glVertex3f(w, h, -d);

        // Bottom Face
        gl.glNormal3f(0.0f, -1.0f, 0.0f); // Normal pointing down
        gl.glVertex3f(-w, -h, -d);
        gl.glVertex3f(w, -h, -d);
        gl.glVertex3f(w, -h, d);
        gl.glVertex3f(-w, -h, d);

        // Front Face
        gl.glNormal3f(0.0f, 0.0f, 1.0f); // Normal pointing to front
        gl.glVertex3f(-w, -h, d);
        gl.glVertex3f(w, -h, d);
        gl.glVertex3f(w, h, d);
        gl.glVertex3f(-w, h, d);

        // Back Face
        gl.glNormal3f(0.0f, 0.0f, -1.0f); // Normal pointing to back
        gl.glVertex3f(-w, -h, -d);
        gl.glVertex3f(-w, h, -d);
        gl.glVertex3f(w, h, -d);
        gl.glVertex3f(w, -h, -d);

        // Left Face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f); // Normal pointing left
        gl.glVertex3f(-w, -h, -d);
        gl.glVertex3f(-w, -h, d);
        gl.glVertex3f(-w, h, d);
        gl.glVertex3f(-w, h, -d);

        // Right Face
        gl.glNormal3f(1.0f, 0.0f, 0.0f); // Normal pointing right
        gl.glVertex3f(w, -h, d);
        gl.glVertex3f(w, -h, -d);
        gl.glVertex3f(w, h, -d);
        gl.glVertex3f(w, h, d);

        gl.glEnd(); // End of drawing
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        try {
            GL2 gl = drawable.getGL().getGL2();

            // Prevent division by zero
            if (height <= 0) {
                height = 1;
            }

            // Set viewport to cover the entire window
            gl.glViewport(0, 0, width, height);

            // Set up perspective projection
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            float aspect = (float) width / (float) height;
            // Use GLU to set up perspective
            GLU glu = new GLU();
            glu.gluPerspective(45.0f, aspect, 0.1f, 100.0f);

            // Switch back to modelview matrix
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
        } catch (Exception e) {
            System.err.println("Error in reshape method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        prevMouseX = e.getX();
        prevMouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getX() - prevMouseX;
        int deltaY = e.getY() - prevMouseY;

        rotateY += deltaX * 0.5f;  // Slow down rotation for better control
        rotateX += deltaY * 0.5f;

        prevMouseX = e.getX();
        prevMouseY = e.getY();

        if (glcanvas != null) {
            glcanvas.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}