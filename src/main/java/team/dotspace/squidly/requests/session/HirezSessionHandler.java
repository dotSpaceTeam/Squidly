/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.session;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.dotspace.squidly.requests.APIRequestBuilder;
import team.dotspace.squidly.requests.command.HirezCommandType;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class HirezSessionHandler {

  private final Logger logger = LoggerFactory.getLogger(HirezSessionHandler.class);
  private final Cache<Integer, String> sessionCache;

  public HirezSessionHandler() {
    this.sessionCache = Cache2kBuilder.of(Integer.class, String.class)
        .eternal(false)
        .expireAfterWrite(15, TimeUnit.MINUTES)
        .permitNullValues(true)
        .entryCapacity(1)
        .build();
  }

  public String getSession() {
    AtomicReference<String> session = new AtomicReference<>(null);

    if (this.sessionCache.get(0) == null) {
      requestSession().ifPresent(session::set);
    } else {
      return sessionCache.get(0);
    }

    return session.get();
  }

  private Optional<String> requestSession() {
    var response = new APIRequestBuilder(HirezCommandType.createsession)
        .build();

    if (response.isSuccess()) {

      if (response.getBody().getObject().getString("ret_msg").equals("Approved")) {
        var sessionId = response.getBody().getObject().getString("session_id");
        logger.info("Successfully retrieved new Session! ({})", sessionId);
        this.sessionCache.put(0, sessionId);

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
