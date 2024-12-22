package com.cwc.certificate.utility;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/13
 */
public class CustomIdGenerator {
    private static int currentId = 1001;
    public static synchronized int getNextId() {
        return currentId++;
    }
    public static synchronized void resetId() {
        currentId = 1001;
    }


}
