/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StartUpValidation {

  final static Logger logger = LoggerFactory.getLogger(StartUpValidation.class);
  private static String TOKEN;

  public static boolean validate(String[] args) {
    if (args.length < 1) {
      logger.error("You need to specify the needed arguments to run this application.");
      return false;
    }

    var file = new File(args[0]);

    if (!file.exists() || !file.canRead()) {
      logger.error("The specifed file does not exist, or could not be read.");
      return false;
    }

    try (var reader = new BufferedReader(new FileReader(file))) {
      TOKEN = reader.readLine();
    } catch (IOException ex) {
      logger.error("The specifed file does not exist, or could not be read.");
      return false;
    }

    logger.info("Validation Passed! Login in now..");
    return true;
  }

  public static String getTOKEN() {
    return TOKEN;
  }
}
