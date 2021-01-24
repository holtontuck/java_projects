package editor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame{

        //variables section
        JPanel upperPanel=new JPanel();
        JPanel lowerPanel=new JPanel();
        JPanel searchPanel=new JPanel();

        JTextField filenameField=new JTextField(20);
        JTextField searchField=new JTextField(20);
        JTextArea textArea=new JTextArea(50,50);

        //Images and button setup
        Image loadButtonImg=new ImageIcon("//home//user//Desktop//open-file.jpeg").getImage().getScaledInstance(15,15,Image.SCALE_DEFAULT);
        Image saveButtonImg=new ImageIcon("//home//user//Desktop//save-icon.png").getImage().getScaledInstance(15,15,Image.SCALE_DEFAULT);
        Image searchButtonImg=new ImageIcon("//home//user//Desktop//search-icon.png").getImage().getScaledInstance(15,15,Image.SCALE_DEFAULT);
        Image searchNextButtonImg=new ImageIcon("//home//user//Desktop//next-icon.png").getImage().getScaledInstance(15,15,Image.SCALE_DEFAULT);
        Image searchPrevButtonImg=new ImageIcon("//home//user//Desktop//prev-icon.png").getImage().getScaledInstance(15,15,Image.SCALE_DEFAULT);

        ImageIcon loadBtnIcon=new ImageIcon(loadButtonImg);
        ImageIcon saveBtnIcon=new ImageIcon(saveButtonImg);
        ImageIcon searchBtnIcon=new ImageIcon(searchButtonImg);
        ImageIcon nextIcon=new ImageIcon(searchNextButtonImg);
        ImageIcon prevIcon=new ImageIcon(searchPrevButtonImg);

        JButton loadBtn=new JButton(loadBtnIcon);
        JButton saveBtn=new JButton(saveBtnIcon);
        JButton searchBtn=new JButton(searchBtnIcon);
        JButton searchNextBtn=new JButton(nextIcon);
        JButton searchPrevBtn=new JButton(prevIcon);

        JCheckBox useRegExCB=new JCheckBox("Use regex");

        //FileChooser setup
        JFileChooser fileChooser=new JFileChooser();
        FileNameExtensionFilter filter=new FileNameExtensionFilter("documents","doc","txt");

        //This will hold the indices of the start and stop of each found match
        static class MatchIndexes {
                int start;
                int stop;

                MatchIndexes(int start, int stop) {
                        this.start = start;
                        this.stop = stop;
                }
        }


        ArrayList<MatchIndexes> foundIndices=new ArrayList<>();

        //Index counter for search matches;
        int matchIdx=0;
        int numMatches=0;//How many matches were found?



        //Constructor
        public TextEditor(){
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setSize(300,300);
                this.setVisible(true);
                this.setTitle("Text Editor");

                //Set the names for the various JComponents
                textArea.setName("TextArea");
                filenameField.setName("FilenameField");
                saveBtn.setName("SaveButton");
                loadBtn.setName("OpenButton");
                searchPrevBtn.setName("PreviousMatchButton");
                searchNextBtn.setName("NextMatchButton");
                searchBtn.setName("StartSearchButton");
                searchField.setName("SearchField");
                useRegExCB.setName("UseRegExCheckbox");
                fileChooser.setName("FileChooser");

                //set up the fileChooser
                fileChooser.setFileFilter(filter);
                fileChooser.setDialogTitle("file search");
                fileChooser.setVisible(false);
                //Add fileChooser to the main JFrame
                this.add(fileChooser);

                filenameField.setBounds(0,0,200,20);

                //Add the file buttons to upperPanel
                upperPanel.add(loadBtn);
                upperPanel.add(saveBtn);
                upperPanel.add(filenameField);

                //Add the search buttons to the searchPanel
                searchPanel.add(searchField);
                searchPanel.add(searchBtn);
                searchPanel.add(searchPrevBtn);
                searchPanel.add(searchNextBtn);
                searchPanel.add(useRegExCB);

                //Add the panels to the main JFrame
                this.add(upperPanel,BorderLayout.NORTH);
                this.add(searchPanel,BorderLayout.NORTH);

                textArea.setText(null);
                lowerPanel.add(textArea);

                //ScrollPane for the text editor work area
                JScrollPane scrollPane=new JScrollPane(lowerPanel);
                scrollPane.setName("ScrollPane");
                this.add(scrollPane,BorderLayout.CENTER);

                //Action listener for load button
                loadBtn.addActionListener(actionEvent->{
                        fileChooser.setVisible(true);
                        fileChooser.showOpenDialog(null);
                        File file=fileChooser.getSelectedFile();
                        String filename=file.getAbsolutePath();

                        StringBuilder sb=new StringBuilder();
                        int _byte;
                        char character;

                        if(filename.length()>0){
                                try(BufferedReader in=new BufferedReader(new FileReader(filename))){
                                        while((_byte=in.read())!=-1){
                                                character=(char)_byte;
                                                sb.append(character);
                                        }
                                        String fileContents=sb.toString();
                                        textArea.setText(fileContents);
                                } catch(FileNotFoundException fnfException){
                                        textArea.setText("");
                                        System.out.println("ERROR: "+fnfException.getMessage());
                                } catch(IOException ioe){
                                        System.out.println("ERROR: "+ioe);
                                }
                        }else{
                                System.out.println("Invalid filename!");
                        }
                        fileChooser.setVisible(false);
                });

                //Action listener for save button
                saveBtn.addActionListener(actionEvent->{
                        String filename=filenameField.getText();
                        if(filename.length()>0){
                                try(BufferedWriter out=new BufferedWriter(new FileWriter(filename))){
                                        out.write(textArea.getText());
                                }catch(FileNotFoundException fnfException){
                                        textArea.setText("");
                                        System.out.println("ERROR: "+fnfException.getMessage());
                                }catch(IOException ioe){
                                        System.out.println("ERROR: "+ioe);
                                }
                        }else{
                                System.out.println("Invalid filename");
                        }
                });

                //Code to add the MenuBar and Menu
                JMenuBar menuBar=new JMenuBar();
                JMenu fileMenu=new JMenu("File");
                fileMenu.setMnemonic(KeyEvent.VK_F);
                fileMenu.setName("MenuFile");

                JMenu searchMenu=new JMenu("Search");
                searchMenu.setName("MenuSearch");

                JMenuItem startSearch=new JMenuItem("Start search");
                startSearch.setName("MenuStartSearch");
                JMenuItem menuPreviousMatch =new JMenuItem("Previous search");
                menuPreviousMatch .setName("MenuPreviousMatch");
                JMenuItem menuNextMatch=new JMenuItem("Next match");
                menuNextMatch.setName("MenuNextMatch");
                JMenuItem useRegEx=new JMenuItem("User Regular expressions");
                useRegEx.setName("MenuUseRegExp");


                //Action listener to select the JCheckbox for userRegEx from the userRegEx menu option
                useRegEx.addActionListener(actionEvent -> useRegExCB.doClick());


                //Action listener for Search Button and Search Menu
                searchBtn.addActionListener(actionEvent->{
                        matchIdx=0;//Reset the match index (for the list of matches to zero (0)
                        foundIndices.clear();//Clear the foundIndices list every time a new search is requested
                        Searcher searcher=new Searcher(textArea,foundIndices);
                        searcher.execute();
                });


                //Action listener for search option in menu
                startSearch.addActionListener(actionEvent->{
                        matchIdx=0;//Reset the match index (for the list of matches to zero (0)
                        foundIndices.clear();//Clear the foundIndices list every time a new search is requested
                        Searcher searcher=new Searcher(textArea,foundIndices);
                        searcher.execute();
                });

                searchMenu.add(startSearch);
                searchMenu.add(menuPreviousMatch );
                searchMenu.add(menuNextMatch);
                searchMenu.add(useRegEx);


                //Action listener for menuNextButton (menuNextMatch)
                menuNextMatch.addActionListener(actionEvent->{
                        int startIdx, stopIdx;
                        //If we've hit the last match, wrap back around to the first
                        if(matchIdx>= numMatches){
                                matchIdx=0;
                        }
                        int searchStringLen=searchField.getText().length();
                        MatchIndexes indices =foundIndices.get(matchIdx);
                        startIdx = indices.start;
                        stopIdx = indices.stop;
                        this.textArea.setCaretPosition(startIdx+searchStringLen);
                        this.textArea.select(startIdx,stopIdx);
                        this.textArea.grabFocus();
                        matchIdx++;
                });


                //Action listener for searchNextButton (nextMatchButton)
                searchNextBtn.addActionListener(actionEvent->{
                        int startIdx, stopIdx;
                        //If we've hit the last match, wrap back around to the first
                        if(matchIdx>= numMatches){
                                matchIdx=0;
                        }
                        int searchStringLen=searchField.getText().length();
                        MatchIndexes indices =foundIndices.get(matchIdx);
                        startIdx = indices.start;
                        stopIdx = indices.stop;
                        this.textArea.setCaretPosition(startIdx+searchStringLen);
                        this.textArea.select(startIdx, stopIdx);
                        this.textArea.grabFocus();
                        matchIdx++;
                });

                JMenuItem loadFileMenuItem=new JMenuItem();
                loadFileMenuItem.setName("MenuOpen");

                //Action listener for searchPrevButton
                searchPrevBtn.addActionListener(actionEvent->{
                        int startIdx, stopIdx;
                        //If we've hit the first match, wrap back around to the last
                        matchIdx--;
                        if(matchIdx == 0){
                                matchIdx=numMatches-1;
                        }

                        int searchStringLen=searchField.getText().length();
                        MatchIndexes indices =foundIndices.get(matchIdx);
                        startIdx = indices.start;
                        stopIdx = indices.stop;
                        this.textArea.setCaretPosition(startIdx+searchStringLen);
                        this.textArea.select(startIdx, stopIdx);
                        this.textArea.grabFocus();
                });


                menuPreviousMatch.addActionListener(actionEvent->{
                        //If we've hit the first match, wrap back around to the last
                        int startIdx, stopIdx;
                        matchIdx--;
                        if(matchIdx == 0){
                                matchIdx=numMatches-1;
                        }

                        int searchStringLen=searchField.getText().length();
                        MatchIndexes indices =foundIndices.get(matchIdx);
                        startIdx = indices.start;
                        stopIdx = indices.stop;
                        this.textArea.setCaretPosition(startIdx+searchStringLen);
                        this.textArea.select(startIdx,stopIdx);
                        this.textArea.grabFocus();
                });

                //Action listener for the load menuItem
                loadFileMenuItem.addActionListener(actionEvent->{
                        String filename=filenameField.getText();
                        StringBuilder sb=new StringBuilder();
                        int _byte;
                        char character;

                        if(filename.length()>0){
                                try(BufferedReader in=new BufferedReader(new FileReader(filename))){
                                        while((_byte=in.read())!=-1){
                                                character=(char)_byte;
                                                sb.append(character);
                                        }
                                        String fileContents=sb.toString();
                                        textArea.setText(fileContents);
                                }catch(FileNotFoundException fnfException){
                                        textArea.setText("");
                                        System.out.println("ERROR: "+fnfException.getMessage());
                                }catch(IOException ioe){
                                        System.out.println("ERROR: "+ioe);
                                }
                        }else{
                                System.out.println("Invalid filename!");
                        }
                });

                JMenuItem saveFileMenuItem=new JMenuItem();
                saveFileMenuItem.setName("MenuSave");

                JMenuItem exitMenuItem=new JMenuItem();
                exitMenuItem.setName("MenuExit");

                fileMenu.add(loadFileMenuItem);
                fileMenu.add(saveFileMenuItem);
                fileMenu.add(exitMenuItem);

                //Action listener for the save menuItem
                saveFileMenuItem.addActionListener(actionEvent->{
                        String filename=filenameField.getText();
                        if(filename.length()>0){
                                try(BufferedWriter out=new BufferedWriter(new FileWriter(filename))){
                                        out.write(textArea.getText());
                                }catch(FileNotFoundException fnfException){
                                        textArea.setText("");
                                        System.out.println("ERROR: "+fnfException.getMessage());
                                }catch(IOException ioe){
                                        System.out.println("ERROR: "+ioe);
                                }
                        }else{
                                System.out.println("Invalid filename");
                        }
                });

                //Add the menus to the menuBar
                menuBar.add(fileMenu);
                menuBar.add(searchMenu);

                //Set the JFrame's menuBar to be "menuBar"
                this.setJMenuBar(menuBar);
        }


        class Searcher extends SwingWorker<Matcher, Object> {
                JTextArea textArea;
                String toSearchFor;
                ArrayList<MatchIndexes> foundIndices;

                Searcher(JTextArea textArea, ArrayList<MatchIndexes> foundIndices) {
                        this.textArea = textArea;
                        this.foundIndices = foundIndices;
                }

                @Override
                protected Matcher doInBackground() {
                        Pattern pattern;
                        toSearchFor = searchField.getText();
                        if (useRegExCB.isSelected()) {
                                System.out.println("USING REGEX");
//                        pattern = Pattern.compile(toSearchFor, Pattern.CASE_INSENSITIVE);
                                pattern = Pattern.compile(toSearchFor);

                        } else {
                                pattern = Pattern.compile("\\b" + toSearchFor + "\\b", Pattern.CASE_INSENSITIVE);
                        }
                        return pattern.matcher(textArea.getText());
                }

                @Override
                protected void done() {
                        Matcher matcher = null;
                        try {
                                matcher = get();
                        } catch (Exception e) {
                                System.out.println("ERROR: " + e.getMessage());
                        }
                        if (matcher != null) {
                                while (matcher.find()) {
                                        foundIndices.add(new MatchIndexes(matcher.start(), matcher.end()));
                                }
                                numMatches = foundIndices.size();
                                matcher.reset();
                                if (matcher.find()) {
                                        this.textArea.setCaretPosition(matcher.start() + toSearchFor.length());
                                        this.textArea.select(matcher.start(), matcher.end());
                                        this.textArea.grabFocus();
                                }
                                matchIdx++;
                        }
                }
        }
}