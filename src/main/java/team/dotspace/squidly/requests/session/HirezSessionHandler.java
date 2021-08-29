/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.dotspace.squidly.requests.APIRequestBuilder;
import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.command.HirezCommandType;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class HirezSessionHandler {

  private final Logger logger = LoggerFactory.getLogger(HirezSessionHandler.class);
  private final Timer cleanupTimerPaladins = new Timer("Session-Timer");
  private String session;

  public HirezSessionHandler() {
    this.session = "";
  }

  public String getSession() {
    AtomicReference<String> session = new AtomicReference<>("");

    if (this.session.isEmpty()) {
      requestSession().ifPresent(session::set);
      this.scheduleSessionCleanup();
    } else {
      return this.session;
    }

    return session.get();

  }

  private void scheduleSessionCleanup() {
    cleanupTimerPaladins.schedule(new TimerTask() {
      @Override
      public void run() {
        session = "";
      }
    }, 1000 * 60 * 15);
  }

  private Optional<String> requestSession() {
    var response = new APIRequestBuilder(HirezCommandType.createsession)
        .build();

    if (response.isSuccess()) {

      if (response.getBody().getObject().getString("ret_msg").equals("Approved")) {
        var sessionId = response.getBody().getObject().getString("session_id");
        logger.info("Successfully retrieved new Session! ({})", sessionId);
        this.session = sessionId;


        return Optional.ofNullable(sessionId);
      }
      logger.error("There was a problem requesting a new Session! Msg: {}", response.getBody().getObject().getString("ret_msg"));

    } else {

      var s = "ret_msg not found!";

      if (response.getBody() != null)
        if (response.getBody().getObject() != null)
          if (response.getBody().getObject().getString("ret_msg") != null)
            s = response.getBody().getObject().getString("ret_msg");

      logger.error("{}:{}! Msg: {}", response.getStatus(), response.getStatusText(), s);
    }

    return Optional.empty();
  }
}
