import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import javax.swing.JOptionPane;

/**
 * Utility class to extract and set up native libraries for JOGL
 */
public class LibrarySetup {

    public static void main(String[] args) {
        try {
            setupNativeLibraries();
            System.out.println("Process finished with exit code 0");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to set up native libraries: " + e.getMessage(),
                    "Setup Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void setupNativeLibraries() throws Exception {
        // Get project directory
        String projectDir = System.getProperty("user.dir");
        System.out.println("Project directory: " + projectDir);

        // Detect OS and architecture
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        System.out.println("OS: " + os + ", Architecture: " + arch);

        // Get lib directory
        File libDir = new File(projectDir, "lib");
        if (!libDir.exists()) {
            libDir.mkdirs();
        }

        // Create natives directory specific to this OS/architecture
        String nativesDir = projectDir + File.separator + "natives";
        String osArchDir = nativesDir + File.separator;

        if (os.contains("windows")) {
            osArchDir += "windows-" + (arch.contains("64") ? "amd64" : "x86");
        } else if (os.contains("linux")) {
            osArchDir += "linux-" + (arch.contains("64") ? "amd64" : "x86");
        } else if (os.contains("mac")) {
            osArchDir += "macosx-" + (arch.contains("64") ? "universal" : "universal");
        }

        File osArchDirFile = new File(osArchDir);
        if (!osArchDirFile.exists()) {
            osArchDirFile.mkdirs();
        }

        // Set java.library.path
        String libraryPath = libDir.getAbsolutePath() + File.pathSeparator + osArchDir;
        System.setProperty("java.library.path", libraryPath);
        System.out.println("Set java.library.path to: " + libraryPath);

        // Find and extract native libraries from JAR files
        File[] jarFiles = libDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
        List<String> nativeJars = new ArrayList<>();

        if (jarFiles != null) {
            System.out.println("Found native library JAR files:");
            for (File jarFile : jarFiles) {
                if (jarFile.getName().contains("natives")) {
                    nativeJars.add(jarFile.getName());
                    System.out.println("  " + jarFile.getName());

                    // Optionally extract native libraries from these JARs
                    extractNativesFromJar(jarFile, libDir);
                }
            }
        }

        // Check if the critical DLL files are present
        verifyNativeLibraries(libDir);
    }

    private static void extractNativesFromJar(File jarFile, File targetDir) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                // Only extract DLL, SO, or DYLIB files (native libraries)
                if (name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib") ||
                        name.endsWith(".jnilib")) {

                    File targetFile = new File(targetDir, new File(name).getName());

                    // Don't overwrite existing files
                    if (!targetFile.exists()) {
                        try (InputStream in = jar.getInputStream(entry);
                             FileOutputStream out = new FileOutputStream(targetFile)) {

                            byte[] buffer = new byte[4096];
                            int read;
                            while ((read = in.read(buffer)) != -1) {
                                out.write(buffer, 0, read);
                            }
                            out.flush();

                            // Make the file executable on Unix systems
                            targetFile.setExecutable(true);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error extracting from JAR " + jarFile.getName() + ": " + e.getMessage());
        }
    }

    private static void verifyNativeLibraries(File libDir) {
        // Key native libraries to check
        String[] requiredLibs = {
                "gluegen_rt.dll", "jogl_desktop.dll"  // For Windows
                // Add equivalents for other platforms if needed
        };

        for (String lib : requiredLibs) {
            File libFile = new File(libDir, lib);
            if (libFile.exists()) {
                System.out.println("Found " + libFile.getAbsolutePath());
            } else {
                System.out.println("Missing " + lib + " - extraction may have failed");
            }
        }
    }
}