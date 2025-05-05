import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utility class to extract native libraries from JAR files
 */
public class NativeLibraryExtractor {

    /**
     * Extract all DLL files from a JAR file
     * @param jarPath Path to the JAR file containing native libraries
     * @param outputDir Directory to extract the libraries to
     * @return true if extraction was successful
     */
    public static boolean extractNativeLibraries(String jarPath, String outputDir) {
        File jarFile = new File(jarPath);
        File outDir = new File(outputDir);

        if (!jarFile.exists() || !jarFile.isFile()) {
            System.err.println("JAR file does not exist: " + jarPath);
            return false;
        }

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try (ZipFile zip = new ZipFile(jarFile)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                // Only extract DLL files (or .so files for Linux, .jnilib for Mac)
                if (name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".jnilib")) {
                    // Extract just the filename without path
                    String fileName = new File(name).getName();
                    File outFile = new File(outDir, fileName);

                    System.out.println("Extracting: " + fileName + " to " + outFile.getAbsolutePath());

                    try (InputStream in = zip.getInputStream(entry);
                         FileOutputStream out = new FileOutputStream(outFile)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error extracting native libraries: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Process all native library JARs in a directory
     * @param libDirPath Path to the directory containing library JAR files
     * @return true if processing was successful
     */
    public static boolean processLibraryDirectory(String libDirPath) {
        File libDir = new File(libDirPath);

        if (!libDir.exists() || !libDir.isDirectory()) {
            System.err.println("Library directory does not exist: " + libDirPath);
            return false;
        }

        // Create a natives directory at the same level as the lib directory
        File nativesDir = new File(libDir.getParentFile(), "natives");
        if (!nativesDir.exists()) {
            nativesDir.mkdirs();
        }

        System.out.println("Extracting native libraries to: " + nativesDir.getAbsolutePath());

        // Get all JAR files in the lib directory
        File[] jarFiles = libDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (jarFiles == null || jarFiles.length == 0) {
            System.err.println("No JAR files found in: " + libDirPath);
            return false;
        }

        boolean success = true;

        // Process each JAR file
        for (File jarFile : jarFiles) {
            if (jarFile.getName().contains("natives")) {
                System.out.println("Processing: " + jarFile.getName());

                if (!extractNativeLibraries(jarFile.getAbsolutePath(), nativesDir.getAbsolutePath())) {
                    success = false;
                }
            }
        }

        return success;
    }

    /**
     * Main method to run the extractor
     */
    public static void main(String[] args) {
        // Get the current project directory
        String projectDir = System.getProperty("user.dir");
        String libDirPath = projectDir + File.separator + "lib";

        System.out.println("Processing library directory: " + libDirPath);

        if (processLibraryDirectory(libDirPath)) {
            System.out.println("Native library extraction completed successfully");
        } else {
            System.err.println("Native library extraction failed");
        }
    }
}