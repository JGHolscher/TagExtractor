import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExtractorFrame extends JFrame {
    JButton quitBtn, chooseBtn, saveBtn;

    JTextArea displayWordsTA;
    JScrollPane scroller;

    JPanel mainPnl, titlePnl, displayWordsPnl, btnPnl;
    JLabel titleLbl;

    public ExtractorFrame() //DONE
    {
        setTitle("Tag Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;

        setSize((screenWidth / 4) * 3, screenHeight);
        setLocationRelativeTo(null); //centers

        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        add(mainPnl);
        createTitlePanel();
        createDisplayPanel();
        createButtonPanel();

        setVisible(true);
    }


    private void createTitlePanel() {
        titlePnl = new JPanel();
        titleLbl = new JLabel("Tag Extractor", JLabel.CENTER);
        titleLbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
        titleLbl.setVerticalTextPosition(JLabel.BOTTOM);
        titleLbl.setHorizontalTextPosition(JLabel.CENTER);
        titlePnl.add(titleLbl);
        mainPnl.add(titlePnl, BorderLayout.NORTH);
    }

    private void createDisplayPanel()// done
    {
        displayWordsPnl = new JPanel();

        displayWordsTA =  new JTextArea(20, 30);
        scroller = new JScrollPane(displayWordsTA);
        displayWordsTA.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));

        displayWordsTA.setEditable(false);
        displayWordsPnl.add(scroller);

        mainPnl.add(displayWordsPnl, BorderLayout.CENTER);
    }



    private void createButtonPanel() {
        btnPnl = new JPanel();
        btnPnl.setLayout(new GridLayout(1, 2));

        chooseBtn = new JButton("Choose File");
        chooseBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
        quitBtn = new JButton("Quit");
        quitBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
        saveBtn = new JButton("Save Tags to File");
        saveBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));

        btnPnl.add(chooseBtn);
        btnPnl.add(saveBtn);
        btnPnl.add(quitBtn);

        mainPnl.add(btnPnl, BorderLayout.SOUTH);

        //Quit Button -DONE
        quitBtn.addActionListener(new ActionListener() {
            JOptionPane pane = new JOptionPane();

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(pane, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        });

        //choose File Button

        //save words to file button
    }
}
