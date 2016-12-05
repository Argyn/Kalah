package MKAgent;

import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public enum Logger {

  INSTANCE;

  private java.util.logging.Logger logger;

  private Logger() {
    logger = java.util.logging.Logger.getLogger("MKAgent");
    try {
      logger.setUseParentHandlers(false);
      String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
      String path = String.format("%s/%s", currentPath, "MKAgent.log");
      FileHandler fh = new FileHandler(path);
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void info(String message) {
    logger.info(message);
  }

  public void severe(String message) {
    logger.severe(message);
  }
}
