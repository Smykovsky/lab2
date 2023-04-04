package pl.javastart.bookshop.MySql;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {


    public static void info(final String logs) {
        log.info(logs);
    }

    public static void debug(final String logs) {
        log.debug(logs);
    }

    public static void error(final String logs) {
        log.warn(logs);
    }

    public static void warning(final String logs) {
        log.warn(logs);
    }
}