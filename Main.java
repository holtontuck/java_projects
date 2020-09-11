package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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