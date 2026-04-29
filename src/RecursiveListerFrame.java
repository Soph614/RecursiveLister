import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class RecursiveListerFrame extends JFrame {
    JPanel mainPnl;
    JPanel displayPnl;
    JPanel buttonPnl;

    JLabel listerLbl;
    JTextArea listerTA;
    JScrollPane listerScrollpane;

    JButton startBtn;
    JButton quitBtn;
    JFileChooser fileChooser;

    public RecursiveListerFrame() {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        createDisplayPnl();
        mainPnl.add(displayPnl, BorderLayout.NORTH);

        createButtonPnl();
        mainPnl.add(buttonPnl, BorderLayout.CENTER);

        add(mainPnl);
        {
            // SET SIZE AND LOCATION...CODE ADAPTED FROM CAY HORSTMANN
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            int screenHeight = screenSize.height;
            int screenWidth = screenSize.width;

            double onePointFive = 1.5;
            setSize(screenWidth / 3, (int) (screenHeight / onePointFive));
            setLocation(screenWidth / 3, screenHeight / 4);

            setTitle("Recursive File Lister");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }

    public void createButtonPnl() {
        buttonPnl = new JPanel();

        startBtn = new JButton("Start");
        startBtn.addActionListener((ActionEvent ae) -> {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fileChooser.showOpenDialog((Component)ae.getSource());
            if(returnVal==JFileChooser.APPROVE_OPTION) {
                try {
                    listFilesRecursively(fileChooser.getSelectedFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "You have to choose a directory to continue.");
            }
        });

        quitBtn = new JButton("Quit");
        quitBtn.addActionListener((ActionEvent ae) -> {
            System.exit(0);
        });

        buttonPnl.add(startBtn);
        buttonPnl.add(quitBtn);
    }

    public void createDisplayPnl() {
        displayPnl = new JPanel();
        displayPnl.setLayout(new BorderLayout());
        displayPnl.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));

        listerLbl = new JLabel("FILES IN DIRECTORY:");

        listerTA = new JTextArea(20, 40);

        listerScrollpane = new JScrollPane();
        listerScrollpane = new JScrollPane(listerTA);

        displayPnl.add(listerLbl, BorderLayout.NORTH);
        displayPnl.add(listerScrollpane, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        JFrame frame = new RecursiveListerFrame();
        frame.pack();
    }

    public void listFilesRecursively(File file) throws IOException {
        if(file == null) {
            return;
        }
        if(file.getCanonicalPath().equals(file.getAbsolutePath())) {
            if(file.isFile()) {
                listerTA.append(file.getAbsolutePath() + "\n");
            }
            if (file.isDirectory()) {
                File[] directoryChildren = file.listFiles();
                for(File dirChild : directoryChildren) {
                    listFilesRecursively(dirChild);
                }
            }
        }
    }
}