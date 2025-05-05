import java.io.File;

public class TestLibraryLoad {
    public static void main(String[] args) {
        // Print the library path and working directory
        String libraryPath = System.getProperty("java.library.path");
        System.out.println("Java Library Path: " + libraryPath);
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        // Check if the gluegen-rt library file exists in the specified path
        String libraryFolderPath = "C:/Users/athth/Downloads/main-assessment-group-36-main/natives/windows-amd64";
        File gluegenFile = new File(libraryFolderPath, "gluegen-rt.dll");

        if (gluegenFile.exists()) {
            System.out.println("Found gluegen-rt.dll at: " + gluegenFile.getAbsolutePath());
        } else {
            System.out.println("gluegen-rt.dll not found in " + libraryFolderPath);
        }

        // Try to load the library explicitly
        try {
            System.loadLibrary("gluegen-rt");
            System.out.println("Successfully loaded gluegen-rt");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Failed to load gluegen-rt: " + e.getMessage());
        }
    }
}
