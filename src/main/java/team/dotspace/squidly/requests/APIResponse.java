/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import team.dotspace.squidly.requests.command.HirezCommandType;

import java.util.function.Consumer;

public record APIResponse(int statusCode, String statusText, HirezCommandType commandType,
                          JsonNode jsonNode) {

  public APIResponse(@NotNull HttpResponse<JsonNode> httpResponse, @NotNull HirezCommandType commandType) {
    this(
        httpResponse.getStatus(),
        httpResponse.getStatusText(),
        commandType,
        httpResponse.getBody());
  }

  public boolean isSuccess() {
    return !isFailure();
  }

  public boolean isFailure() {
    if (this.statusCode != 200)
      return true;

    if (this.jsonNode.isArray()) {

      if (this.jsonNode.getArray() == null || this.jsonNode.getArray().length() == 0)
        return true;

      if (this.jsonNode.getArray() != null && this.jsonNode.getArray().get(0) != null)
        if (((JSONObject) this.jsonNode.getArray().get(0)).get("ret_msg") != null)
          return true;
    }

    if (!this.jsonNode.isArray() && (this.jsonNode.getObject() == null || this.jsonNode.getObject().getString("ret_msg").equals("Approved")))
      return true;

    return false;
  }

  public APIResponse then(Consumer<APIResponse> responseConsumer) {
    if (this.isSuccess())
      responseConsumer.accept(this);

    return this;
  }

  public APIResponse error(Consumer<APIResponse> responseConsumer) {
    if (!this.isSuccess())
      responseConsumer.accept(this);

    return this;
  }

  @Override
  public String toString() {
    return "APIResponse{" +
        "statusCode=" + statusCode +
        ", statusText='" + statusText + '\'' +
        ", commandType=" + commandType +
        ", jsonNode=" + jsonNode +
        '}';
  }
}
