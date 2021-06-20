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

import java.util.Timer;
import java.util.TimerTask;

public class HirezSessionHandler {

  private final Logger logger = LoggerFactory.getLogger(HirezSessionHandler.class);
  private final Timer timer = new Timer();
  private String paladinsSession;
  private String smiteSession;

  public HirezSessionHandler() {
    this.paladinsSession = "paladinsSession";
    this.smiteSession = "smiteSession";
    this.scheduleTimer();
  }

  private void scheduleTimer() {
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        requestSession(HirezEndpoint.PALADINS);
        requestSession(HirezEndpoint.SMITE);
      }
    }, 1000 * 2, 1000 * 60 * 15);
  }

  public String getSession(HirezEndpoint endpoint) {
    if (endpoint.equals(HirezEndpoint.SMITE)) return smiteSession;
    else return paladinsSession;
  }

  private void requestSession(HirezEndpoint endpoint) {
    var httpResponse = new APIRequestBuilder(HirezCommandType.createsession)
        .changeEndpoint(endpoint)
        .build();

    httpResponse.ifSuccess(response -> {
      if (response.getBody().getObject().getString("ret_msg").equals("Approved")) {
        var sessionId = response.getBody().getObject().getString("session_id");
        logger.info("Successfully retrieved new " + endpoint.name() + " Session! ({})", sessionId);

        if (endpoint == HirezEndpoint.PALADINS)
          this.paladinsSession = sessionId;
        else if (endpoint == HirezEndpoint.SMITE)
          this.smiteSession = sessionId;

        return;
      }
      logger.error("There was a problem requesting a new " + endpoint.name() + " Session! Msg: {}", response.getBody().getObject().getString("ret_msg"));
    });

    httpResponse.ifFailure(response -> {
      var s = "ret_msg not found!";

      if (response.getBody() != null)
        if (response.getBody().getObject() != null)
          if (response.getBody().getObject().getString("ret_msg") != null)
            s = response.getBody().getObject().getString("ret_msg");

      logger.error("{}:{}! Msg: {}", response.getStatus(), response.getStatusText(), s);
    });
  }
}
