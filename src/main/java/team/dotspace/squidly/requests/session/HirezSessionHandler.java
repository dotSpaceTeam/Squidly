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
  private final Timer cleanupTimerPaladins = new Timer("Paladins-Session-Timer");
  private final Timer cleanupTimerSmite = new Timer("Smite-Session-Timer");
  private String paladinsSession;
  private String smiteSession;

  public HirezSessionHandler() {
    this.paladinsSession = "";
    this.smiteSession = "";
  }

  public String getSession(HirezEndpoint endpoint) {
    AtomicReference<String> session = new AtomicReference<>("");
    return switch (endpoint) {
      case PALADINS -> {

        if (paladinsSession.isEmpty()) {
          requestSession(endpoint).ifPresent(session::set);
          this.scheduleSessionCleanup(endpoint);
        } else {
          yield paladinsSession;
        }

        yield session.get();
      }
      case SMITE -> {
        if (smiteSession.isEmpty()) {
          requestSession(endpoint).ifPresent(session::set);
        } else {
          yield smiteSession;
        }

        yield session.get();
      }
      case ANY -> this.getSession(HirezEndpoint.PALADINS);
    };

  }

  private void scheduleSessionCleanup(HirezEndpoint endpoint) {
    switch (endpoint) {
      case PALADINS -> cleanupTimerPaladins.schedule(new TimerTask() {
        @Override
        public void run() {
          paladinsSession = "";
        }
      }, 1000 * 60 * 15);
      case SMITE -> cleanupTimerSmite.schedule(new TimerTask() {
        @Override
        public void run() {
          smiteSession = "";
        }
      }, 1000 * 60 * 15);
      case ANY -> this.scheduleSessionCleanup(HirezEndpoint.PALADINS);
    }

  }

  private Optional<String> requestSession(HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.createsession)
        .changeEndpoint(endpoint)
        .build();

    if (response.isSuccess()) {

      if (response.getBody().getObject().getString("ret_msg").equals("Approved")) {
        var sessionId = response.getBody().getObject().getString("session_id");
        logger.info("Successfully retrieved new " + endpoint.name() + " Session! ({})", sessionId);

        if (endpoint == HirezEndpoint.PALADINS)
          this.paladinsSession = sessionId;
        else if (endpoint == HirezEndpoint.SMITE)
          this.smiteSession = sessionId;

        return Optional.ofNullable(sessionId);
      }
      logger.error("There was a problem requesting a new " + endpoint.name() + " Session! Msg: {}", response.getBody().getObject().getString("ret_msg"));

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
