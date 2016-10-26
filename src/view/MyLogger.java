package view;

import javax.swing.JTextArea;

/**
 *
 * @author MGolosz
 */
public class MyLogger {

    private static JTextArea logger;

    public static void init(JTextArea logger) {
        if (logger != null) {
            MyLogger.logger = logger;
        }
    }

    public static void log(String text) {
        logger.append(text + "\n");
    }
}
