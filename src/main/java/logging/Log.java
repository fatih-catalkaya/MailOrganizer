package logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Log {
    private static Log instance;
    private final Logger logger;

    public static Log getInstance(){
        if(instance == null){
            instance = new Log();
        }
        return instance;
    }

    private Log(){
        this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        this.logger.addHandler(new ConsoleHandler());
        this.logger.setUseParentHandlers(false);
    }

    public void info(final String msg){
        this.logger.info(msg);
    }

    public void severe(final String msg){
        this.logger.severe(msg);
    }
}
