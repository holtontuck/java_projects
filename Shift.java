package encryptdecrypt;

import java.io.FileWriter;
import java.io.IOException;

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
