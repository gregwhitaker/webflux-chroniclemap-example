package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  /**
   * Main entry-point of the application.
   *
   * @param args command line arguments
   * @throws Exception
   */
  public static void main(String... args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}

