/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    if (args.length < 2) {
      logger.error("You need to specify the needed arguments to run this application.");
      return false;
    }

    var tokenFile = new File(args[0]);
    var credentialFile = new File(args[1]);

    if (!tokenFile.exists() || !tokenFile.canRead() || !credentialFile.exists() || !credentialFile.canRead()) {
      logger.error("One of the specifed files does not exist, or could not be read.");
      return false;
    }

    try (var reader = new BufferedReader(new FileReader(tokenFile))) {
      TOKEN = reader.readLine();
    } catch (IOException ex) {
      logger.error("One of the specifed files does not exist, or could not be read.");
      return false;
    }

    var mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);

    CREDENTIALS = mapper.readValue(credentialFile, HirezCredentialPair.class);

    logger.info("Validation Passed! Login in now..");
    return true;
  }

  public static String getTOKEN() {
    return TOKEN;
  }

  public static HirezCredentialPair getCREDENTIALS() {
    return CREDENTIALS;
  }
}
