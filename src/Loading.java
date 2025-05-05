import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class
Loading extends JFrame {
    private JPanel panel1;
    private JProgressBar progressBar1;

    public Loading() {
        super("Loading");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel1);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize progress bar
        progressBar1.setMinimum(0);
        progressBar1.setMaximum(100);

        // Simulate loading process
        simulateLoading();
    }

    private void simulateLoading() {
        Timer timer = new Timer(100, new ActionListener() {
            int progress = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                progressBar1.setValue(progress);

                // Check if loading is complete
                if (progress == 100) {
                    ((Timer) e.getSource()).stop();
                    dispose();
                    openMainUI();
                }

                // Increment progress
                progress += 2;
            }
        });
        timer.start();
    }


    private void openMainUI() {
        Registration login = new Registration();
        login.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Loading ld = new Loading();
                ld.setVisible(true);
            }
        });
    }
}