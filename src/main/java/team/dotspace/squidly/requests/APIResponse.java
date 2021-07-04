/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import kong.unirest.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public record APIResponse(int statusCode, String statusText, JsonNode jsonNode) {

  public APIResponse(int statusCode, @NotNull String statusText, @Nullable JsonNode jsonNode) {
    this.statusCode = statusCode;
    this.statusText = statusText;
    this.jsonNode = Objects.requireNonNullElse(jsonNode, new JsonNode("{}"));
  }

  public boolean isSuccess() {
    return statusCode == 200 &&
        ((!this.jsonNode.isArray() && this.jsonNode.getObject().getString("ret_msg").equals("Approved") ||
            this.jsonNode.isArray() && this.jsonNode.getArray() != null));
  }

  public boolean isFailure() {
    return !this.isSuccess();
  }

  public APIResponse ifSuccess(Consumer<APIResponse> responseConsumer) {
    if (this.isSuccess())
      responseConsumer.accept(this);

    return this;
  }

  public APIResponse ifFailure(Consumer<APIResponse> responseConsumer) {
    if (!this.isSuccess())
      responseConsumer.accept(this);

    return this;
  }
}
