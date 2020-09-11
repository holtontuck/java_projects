package encryptdecrypt;

import java.io.FileWriter;
import java.io.IOException;

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