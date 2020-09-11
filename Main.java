package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

abstract class Algorithm {

    abstract void decrypt(String message, int key, boolean toFile, String _fileName);
    abstract void encrypt(String message, int key, boolean toFile, String _fileName);
}

class Shift extends Algorithm {
    @Override
    void decrypt(String message, int key, boolean toFile, String _fileName) {
        if (message == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();

        for (char character : message.toCharArray()) {
            if ((character < 'a' || character > 'z') &&
                    (character < 'A' || character > 'Z')) {
                sb.append(character);
                continue;
            } else  if (character >= 'A' && character <= 'Z') {
                if (character - key < 'A') {
                    character = (char) ('Z' - (key - (character - 'A')) + 1);
                } else {
                    character -= key;
                }
            } else if (character >= 'a' && character <= 'z') {
                if (character - key < 'a') {
                    character = (char) ('z' - (key - (character - 'a')) + 1);
                } else {
                    character -= key;
                }
            } else {
                character -= key;
            }
            sb.append(character);
        }

        message = sb.toString();
        if (toFile &&(!_fileName.equals(""))) {
            try {
                FileWriter fileWriter = new FileWriter(_fileName);
                fileWriter.write(message);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.print(message);
        }
    }

    @Override
    void encrypt(String message, int key, boolean toFile, String _fileName) {
        if (message == null) {

            return;
        }
        StringBuilder sb = new StringBuilder();
        for (char character : message.toCharArray()) {
            if ((character < 'a' || character > 'z') &&
                    (character < 'A' || character > 'Z')) {
                sb.append(character);
                continue;
            }
            if (character >= 'A' && character <= 'Z') {
                if (character + key > 'Z') {
                    character = (char) ('A' + ((character + key) - 'Z') - 1);
                } else {
                    character += key;
                }
            } else if (character >= 'a' && character <= 'z') {
                if (character + key > 'z') {
                    character = (char) ('a'  + ((character + key) - 'z') - 1);
                } else {
                    character += key;
                }
            } else {
                character += key;
            }
            sb.append(character);
        }

        message = sb.toString();
        if (toFile &&(!_fileName.equals(""))) {
            try {
                FileWriter fileWriter = new FileWriter(_fileName);
                fileWriter.write(message);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.print(message);
        }
    }
}

class Unicode extends Algorithm {
    @Override
    void decrypt(String message, int key, boolean toFile, String _fileName) {
        StringBuilder sb = new StringBuilder();
        for (char character : message.toCharArray()) {
            character -= key;
            sb.append(character);
        }
        message = sb.toString();
        if (toFile &&(!_fileName.equals(""))) {
            try {
                FileWriter fileWriter = new FileWriter(_fileName);
                fileWriter.write(message);
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.print(message);
        }
    }

    @Override
    void encrypt(String message, int key, boolean toFile, String _fileName) {
        if (message == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (char character : message.toCharArray()) {
            character += key;
            sb.append(character);
        }
        message = sb.toString();
        if (toFile &&(!_fileName.equals(""))) {
            try (FileWriter fileWriter = new FileWriter(_fileName)) {
                fileWriter.write(message);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.print(message);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        //The following will hold the values that come in as command line arguments
        String mode = "enc";
        int key = 0;
        String data = "";
        boolean toFile = false;
        String type = "shift";
        String inFile = "";
        String outFile = "";
        //Need to track whether data is being passed in.
        //If -data and -in are both present, -data is preferred, and -in is ignored
        boolean isData = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().equals("-mode")) {
                if (args[i + 1].toLowerCase().equals("dec")) {
                    mode = "dec";
                }//else default: mode = "enc"
            } else if (args[i].toLowerCase().equals("-data")) {
                data = args[i + 1];//else default: data = ""
                isData = true;
            } else if (args[i].toLowerCase().equals("-key")) {
                try {
                    key = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + e.getMessage());
                    //default: key = 0
                }
            } else if (args[i].equals("-in") && (!(isData))) {
                inFile = args[i + 1];
                File file = new File(inFile);
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNext()) {
                        data = scanner.nextLine();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }

            } else if (args[i].equals("-out")) {
                toFile = true;
                outFile = args[i + 1];
            } else if (args[i].equals("-alg")) {
                switch (args[i + 1]) {
                    case "unicode":
                        type = "unicode";
                        break;
                    default:
                        type = "shift";
                }
            }
        }

        Algorithm algorithm;
        if (type.equals("unicode")) {
            algorithm = new Unicode();
        } else {
            algorithm = new Shift();
        }
        if (mode.equals("dec")) {
            algorithm.decrypt(data, key, toFile, outFile);
        } else {
            algorithm.encrypt(data, key, toFile, outFile);
        }
    }
}
