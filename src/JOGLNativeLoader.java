import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;

public class JOGLNativeLoader {

    public static void loadNativeLibraries() {
        try {
            String projectDir = System.getProperty("user.dir");
            String libPath = projectDir + File.separator + "lib";
            File libDir = new File(libPath);

            if (!libDir.exists() || !libDir.isDirectory()) {
                throw new RuntimeException("Library directory not found: " + libPath);
            }

            System.out.println("Native library path: " + libPath);
            System.out.println("Available libraries:");
            Arrays.stream(libDir.listFiles()).forEach(f -> System.out.println(" - " + f.getName()));

            // 1. Set critical system properties before loading any JOGL class
            System.setProperty("jogamp.debug.JNILibLoader", "true");
            System.setProperty("jogamp.debug.NativeLibrary", "true");
            System.setProperty("jogamp.gluegen.UseTempJarCache", "false");

            // 2. Add the lib path to java.library.path
            addLibraryPath(libPath);

            // 3. Explicitly load the native libraries with System.loadLibrary
            // (use the base names without 'lib' prefix or file extension)
            // Windows: gluegen_rt.dll -> gluegen_rt
            if (isWindows()) {
                loadLib(libPath, "gluegen_rt");
                loadLib(libPath, "jogl_desktop");
                loadLib(libPath, "nativewindow_awt");
                loadLib(libPath, "nativewindow_win32");
            } else if (isLinux()) {
                loadLib(libPath, "gluegen_rt");
                loadLib(libPath, "jogl_desktop");
                loadLib(libPath, "nativewindow_awt");
                loadLib(libPath, "nativewindow_x11");
            }

            System.out.println("Native libraries loaded successfully");

        } catch (Exception e) {
            System.err.println("Failed to initialize JOGL libraries: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void addLibraryPath(String libPath) throws Exception {
        // Add the specified path to java.library.path
        String existingPath = System.getProperty("java.library.path", "");
        if (!existingPath.contains(libPath)) {
            if (existingPath.isEmpty()) {
                System.setProperty("java.library.path", libPath);
            } else {
                System.setProperty("java.library.path", libPath + File.pathSeparator + existingPath);
            }

            // Force Java to reload the library paths
            try {
                Field field = ClassLoader.class.getDeclaredField("sys_paths");
                field.setAccessible(true);
                field.set(null, null);
                System.out.println("Library path updated: " + System.getProperty("java.library.path"));
            } catch (Exception e) {
                System.err.println("Failed to reset library path: " + e.getMessage());
                // Continue anyway as System.load with absolute path may still work
            }
        }
    }

    private static void loadLib(String libPath, String libName) {
        try {
            if (isWindows()) {
                String fullPath = libPath + File.separator + libName + ".dll";
                if (new File(fullPath).exists()) {
                    System.load(fullPath);
                    System.out.println("Loaded library: " + fullPath);
                } else {
                    System.err.println("Library file not found: " + fullPath);
                }
            } else if (isLinux()) {
                String fullPath = libPath + File.separator + "lib" + libName + ".so";
                if (new File(fullPath).exists()) {
                    System.load(fullPath);
                    System.out.println("Loaded library: " + fullPath);
                } else {
                    System.err.println("Library file not found: " + fullPath);
                }
            }
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load library " + libName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}