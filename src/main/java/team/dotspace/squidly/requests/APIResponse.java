/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import kong.unirest.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.command.HirezCommandType;

import java.util.Objects;
import java.util.function.Consumer;

public record APIResponse(int statusCode, String statusText, HirezCommandType commandType, HirezEndpoint endpoint,
                          JsonNode jsonNode) {

  public APIResponse(int statusCode, @NotNull String statusText, @NotNull HirezCommandType commandType, @NotNull HirezEndpoint endpoint, @Nullable JsonNode jsonNode) {
    this.statusCode = statusCode;
    this.statusText = statusText;
    this.commandType = commandType;
    this.endpoint = endpoint;
    this.jsonNode = Objects.requireNonNullElse(jsonNode, new JsonNode("{}"));
  }

  public boolean isSuccess() {
    return !isFailure();
  }

  public boolean isFailure() {
    if (this.statusCode != 200)
      return true;

    if (this.jsonNode.isArray() && (this.jsonNode.getArray() == null || this.jsonNode.getArray().length() <= 0))
      return true;

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
}
