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
import team.dotspace.squidly.requests.command.HirezCommandType;
import team.dotspace.squidly.requests.command.RequestParameterMap;
import team.dotspace.squidly.requests.command.RequestParameterType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class APIRequestBuilder {

  private final HirezCredentialPair credentialPair = SquidlyBot.getInstance().getHirezCredentialPair();

  private final HirezCommandType commandType;
  private HirezEndpoint endpoint = HirezEndpoint.PALADINS;
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

  public APIRequestBuilder addParameter(RequestParameterType parameterType, String value) {
    this.parameterMap.put(parameterType, value);
    return this;
  }

  public HttpResponse<JsonNode> build() {
    this.parameterMap.put(RequestParameterType.DEVELOPER_ID, this.credentialPair.devID());
    this.parameterMap.put(RequestParameterType.SIGNATURE, getSignature());
    this.parameterMap.put(RequestParameterType.TIMESTAMP, getTimestamp());
    if (Arrays.asList(this.commandType.getRequiredTypes()).contains(RequestParameterType.SESSION))
      this.parameterMap.put(RequestParameterType.SESSION, getSession());

    var url = Arrays
        .stream(commandType.getRequiredTypes())
        .map(type -> parameterMap.get(type))
        .collect(Collectors.joining("/", this.endpoint.getUrl() + this.commandType.name() + "json/", ""));

    return Unirest.get(url).asJson();
  }

  private String getSession() {
    return SquidlyBot.getInstance().getSessionHandler().getSession();
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
