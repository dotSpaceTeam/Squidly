/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.dotspace.squidly.HirezCredentialPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StartUpValidation {

  final static Logger logger = LoggerFactory.getLogger(StartUpValidation.class);
  private static String TOKEN;
  private static HirezCredentialPair CREDENTIALS;

  public static boolean validate(String[] args) throws IOException {

    switch (checkEnvirnomentVariables()) {
      case 0 -> {

        if (args.length < 2)
          return printArgumentError();

        TOKEN = readArgument(args[0]);

        if (isArgumentValidJson(args[1])) {
          CREDENTIALS = new ObjectMapper().readValue(new File(args[1]), HirezCredentialPair.class);
        } else {
          if (args.length < 3)
            return printArgumentError();

          CREDENTIALS = new HirezCredentialPair(args[1], args[2]);
        }

      }
      case 1 -> TOKEN = readArgument(args[0]);
      case 2 -> {
        int i = 1;

        if (args.length < 1) {
          logger.error("You need to specify the needed arguments to run this application.");
          return false;
        } else if (args.length == 1 || args.length == 2) {
          i = 0;
        }

        if (isArgumentValidJson(args[i]))
          CREDENTIALS = new ObjectMapper().readValue(new File(args[i]), HirezCredentialPair.class);
        else
          CREDENTIALS = new HirezCredentialPair(args[i], args[i + 1]);

      }
      case 3 -> {
        return true;
      }
    }

    logger.info("Validation Passed! Login in now..");
    return true;
  }

  public static String getTOKEN() {
    return TOKEN;
  }

  public static HirezCredentialPair getCREDENTIALS() {
    return CREDENTIALS;
  }

  private static int checkEnvirnomentVariables() {
    var env = System.getenv();
    var tokenENV = env.getOrDefault("SQUIDLY_BOTTOKEN", "");
    var devidENV = env.getOrDefault("SQUIDLY_DEVID", "");
    var authkeyENV = env.getOrDefault("SQUIDLY_AUTHKEY", "");

    boolean hasToken = !tokenENV.isEmpty();
    boolean hasCredentials = !devidENV.isEmpty() && !authkeyENV.isEmpty();

    if (hasToken)
      TOKEN = tokenENV;

    if (hasCredentials)
      CREDENTIALS = new HirezCredentialPair(devidENV, authkeyENV);

    if (hasToken && hasCredentials)
      return 3;
    if (hasToken)
      return 2;
    if (hasCredentials)
      return 1;

    return 0;
  }

  /**
   * @return the file content or if not a file-path, the argument itself.
   */
  private static String readArgument(String arg) {
    var file = new File(arg);
    try (var reader = new BufferedReader(new FileReader(file))) {
      return reader.readLine();
    } catch (Exception ignored) {
      return arg;
    }
  }

  private static boolean isArgumentValidJson(String arg) {
    var file = new File(arg);
    try {
      return new ObjectMapper().readValue(file, HirezCredentialPair.class) != null;
    } catch (IOException ignored) {
      return false;
    }

  }

  private static boolean printArgumentError() {
    logger.error("Please check the documentation how to add arguments!");
    return false;
  }

}
