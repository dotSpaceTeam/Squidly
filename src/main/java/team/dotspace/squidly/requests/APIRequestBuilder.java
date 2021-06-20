/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.commons.codec.digest.DigestUtils;
import team.dotspace.squidly.HirezCredentialPair;
import team.dotspace.squidly.SquidlyBot;
import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.command.CommandParameterType;
import team.dotspace.squidly.requests.command.HirezCommandType;
import team.dotspace.squidly.requests.command.RequestParameterMap;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class APIRequestBuilder {

  private final HirezCredentialPair credentialPair = SquidlyBot.getInstance().getHirezCredentialPair();

  private final HirezCommandType commandType;
  private HirezEndpoint endpoint;
  private RequestParameterMap parameterMap = new RequestParameterMap();

  public APIRequestBuilder(HirezCommandType commandType, RequestParameterMap parameterMap) {
    this.commandType = commandType;
    this.parameterMap = parameterMap;
    this.endpoint = commandType.getEndpoint();
  }

  public APIRequestBuilder(HirezCommandType commandType) {
    this.commandType = commandType;
    this.endpoint = commandType.getEndpoint();
  }

  public APIRequestBuilder addParameter(CommandParameterType parameterType, String value) {
    this.parameterMap.put(parameterType, value);
    return this;
  }

  public APIRequestBuilder changeEndpoint(HirezEndpoint endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  public String constructURL() {
    this.parameterMap.put(CommandParameterType.DEVELOPER_ID, this.credentialPair.devID());
    this.parameterMap.put(CommandParameterType.SIGNATURE, getSignature());
    this.parameterMap.put(CommandParameterType.TIMESTAMP, getTimestamp());
    this.parameterMap.put(CommandParameterType.SESSION, getSession());

    var stringBuilder = new StringBuilder(this.endpoint.getUrl());
    stringBuilder.append(this.commandType.name()).append("json").append("/");

    for (var type : commandType.getRequiredTypes()) {
      stringBuilder.append(parameterMap.get(type)).append("/");
    }
    var s = stringBuilder.toString();

    System.out.println(s);

    return s.substring(0, s.length() - 1);
  }

  public HttpResponse<JsonNode> build() {
    return Unirest.get(constructURL()).asJson();
  }

  private String getSession() {
    return SquidlyBot.getInstance().getSessionHandler().getSession(this.commandType.getEndpoint());
  }

  private String getSignature() {
    var s = this.credentialPair.devID() + this.commandType.name() + this.credentialPair.authKEY() + getTimestamp();
    return DigestUtils.md5Hex(s).toLowerCase();
  }

  private String getTimestamp() {
    var time = ZonedDateTime.now(ZoneId.of("UTC"));
    return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(time);
  }
}
