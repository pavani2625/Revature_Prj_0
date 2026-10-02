package com.movieticket.util;

import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {

    public static void configure() {
        String format = "%5$s%6$s%n";
        System.setProperty("java.util.logging.SimpleFormatter.format", format);

        Logger rootLogger = LogManager.getLogManager().getLogger("");

        for (Handler handler : rootLogger.getHandlers()) {
            handler.setFormatter(new SimpleFormatter());
        }
    }
}