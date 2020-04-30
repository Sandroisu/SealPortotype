package ru.alex.sealportotype;

public interface OnSignatureListener {
    String MODE_NAME = "mode";
    String TITLE = "title";
    String IMAGE = "image";

    /**
     * Режим добавления
     */
    int ADD = 0;
    /**
     * Режим обновления
     */
    int UPDATE = 1;

    /**
     * Удаление подписи
     */
    int REMOVE = 2;

    /**
     * обработчик добавления или редактирования подписи
     * @param mode режим. OnSignatureListener.ADD, OnSignatureListener.UPDATE или OnSignatureListener.REMOVE
     */
    void onClickSignature(int mode, String signature);
}
