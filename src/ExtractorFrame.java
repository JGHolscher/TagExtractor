import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import static java.nio.file.StandardOpenOption.CREATE;

public class ExtractorFrame extends JFrame {
    JButton quitBtn, chooseBtn, saveBtn;
    JTextArea displayWordsTA;
    JScrollPane scroller;
    JPanel mainPnl, titlePnl, displayWordsPnl, btnPnl;
    JLabel titleLbl;
    String fileName;

    ArrayList<String> noise = new ArrayList<>();
    TreeMap<String, Integer> keyWords = new TreeMap<String, Integer>();

    public ExtractorFrame() //DONE
    {
        setTitle("Keyword Extractor");
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
        titleLbl = new JLabel("Story Keyword Extractor", JLabel.CENTER);
        titleLbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
        titleLbl.setVerticalTextPosition(JLabel.BOTTOM);
        titleLbl.setHorizontalTextPosition(JLabel.CENTER);
        titlePnl.add(titleLbl);
        mainPnl.add(titlePnl, BorderLayout.NORTH);
    }

    private void createDisplayPanel()// done
    {
        displayWordsPnl = new JPanel();

        displayWordsTA = new JTextArea(30, 40);
        scroller = new JScrollPane(displayWordsTA);
        displayWordsTA.setFont(new Font("Monospaced", Font.PLAIN, 20));

        displayWordsTA.setEditable(false);
        displayWordsPnl.add(scroller);

        mainPnl.add(displayWordsPnl, BorderLayout.CENTER);
    }


    private void createButtonPanel() {
        btnPnl = new JPanel();
        btnPnl.setLayout(new GridLayout(1, 2));

        chooseBtn = new JButton("Choose File");
        chooseBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
        quitBtn = new JButton("Quit");
        quitBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
        saveBtn = new JButton("Save Keywords to File");
        saveBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));

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
        chooseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readStoryFile();

                for (Map.Entry map : keyWords.entrySet()) {

                    displayWordsTA.append(String.format("%-30s%d\n",map.getKey(), map.getValue()));

                }

            }
        });

        //save words to file button
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //prompts user for file name so I can save multiple times with each run and tell them apart
                String saveFileName = JOptionPane.showInputDialog("Please enter file name");

                File wd = new File(System.getProperty("user.dir"));
                Path file = Paths.get(wd.getPath() + "//src//" + saveFileName + ".txt");

                try {
                    OutputStream out =
                            new BufferedOutputStream(Files.newOutputStream(file, CREATE));
                    BufferedWriter writer =
                            new BufferedWriter(new OutputStreamWriter(out));

                    for (Map.Entry map : keyWords.entrySet()) {
                        writer.write(String.format("%-30s%d\n", map.getKey(), map.getValue()));
                    }
                    writer.close();
                    displayWordsTA.append("\nFile Saved To: " + saveFileName + ".txt");

                }catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });

    }


    //reads all the words on the stop word list
    public void readStopWordFile() {
        try {
            //Filtering the noise words
            File workingDirectory = new File("src/EnglishStopWords.txt");


            Scanner readFile = new Scanner(workingDirectory);

            while (readFile.hasNextLine()) {
                noise.add(readFile.nextLine());
            }
            readFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //reads all the words in the story and name and filters
    private void readStoryFile() {
        JFileChooser chooser = new JFileChooser();
        String line = "";

        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        chooser.setCurrentDirectory(target.toFile());

        readStopWordFile();
        try {
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                target = chooser.getSelectedFile().toPath();

                Scanner inFile = new Scanner(target);
                while (inFile.hasNextLine()) {
                    line = inFile.nextLine().toLowerCase().replaceAll("[^A-Za-z]", " "); //covert to lowercase and replace symbols and nums to spaces
                    fileName = chooser.getSelectedFile().getName();
                    displayWordsTA.setText("File name: " + fileName + "\n\n\nKeyWords and their frequency:\n\n");


                    //filter
                    String word[] = line.split(" ");
                    for (int i = 0; i < word.length; i++) {
                        String currentWord = word[i];

                        //check if already logged for frequency counting
                        if (keyWordFreq(currentWord, keyWords)) {

                        } //check if noise words or not
                        else if (!noiseChecker(currentWord)) {
                            //Put the keywords into keyWords
                            gatherKeyWords(currentWord);

                        }
                    }



                }
                inFile.close();
            } else {
                displayWordsTA.setText("File not chosen. Try again!");
            }
        } catch (IOException i) {
            System.out.println("IOException Error");
            i.printStackTrace();
        }
    }


    //Is this a stop word?? noise checker
    public boolean noiseChecker(String word) {
        for (String noi : noise) {
            if (noi.equals(word)) {
                return true;
            }
        }
        return false;
    }

    //is keyWord present in keyWords?? frequency
    public boolean keyWordFreq(String word, TreeMap<String, Integer> keyWords)
    {
        for (Map.Entry map : keyWords.entrySet()) {
            if (map.getKey().equals(word)) {
               // if (!noise.contains(word)){
                int freq = Integer.parseInt(map.getValue().toString()) + 1;
                map.setValue(freq);

                return true;
            }
        }

        return false;
    }

    //Finally put keywords into the keyWords Officially
    public void gatherKeyWords(String word) {
        if (word != null && !"".equals(word)) {
            keyWords.put(word, 1);
        }
    }
}

