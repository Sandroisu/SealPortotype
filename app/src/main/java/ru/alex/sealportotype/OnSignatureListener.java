package ru.alex.sealportotype;

public interface OnSignatureListener {
    String MODE_NAME = "mode";
    String TITLE = "title";
    String IMAGE = "image";

    int ADD = 0;

    int UPDATE = 1;

    int REMOVE = 2;

    /**
     * handler for adding or editing a signature
     * @param mode mode - one of the above integers
     */
    void onClickSignature(int mode, String signature);
}
