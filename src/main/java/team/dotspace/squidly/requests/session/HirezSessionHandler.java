/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.session;

import org.apache.commons.codec.digest.DigestUtils;
import team.dotspace.squidly.HirezCredentialPair;
import team.dotspace.squidly.SquidlyBot;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class HirezSessionHandler {

  private final HirezCredentialPair credentials = SquidlyBot.getInstance().getHirezCredentialPair();
  private Timer timer = new Timer();
  private String currentSession;

  public HirezSessionHandler() {
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        //TODO request new session
      }
    }, 1000 * 2, 1000 * 60 * 15);

  }

  private String getSignature(String command) {
    var s = this.credentials.devID() + command + this.credentials.authKEY() + getTimestamp();
    return DigestUtils.md5Hex(s).toLowerCase();
  }

  private String getTimestamp() {
    var time = ZonedDateTime.now(ZoneId.of("UTC"));
    return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(time);
  }

  public String getCurrentSession() {
    return currentSession;
  }
}
