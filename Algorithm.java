package encryptdecrypt;

abstract class Algorithm {
    abstract void decrypt(String message, int key, boolean toFile, String _fileName);
        abstract void encrypt(String message, int key, boolean toFile, String _fileName);
}
