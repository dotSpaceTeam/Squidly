/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.dotspace.squidly.SquidlyBot;
import team.dotspace.squidly.requests.APIResponse;
import team.dotspace.squidly.requests.codes.ErrorCode;
import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.command.HirezCommandType;

import java.util.Objects;
import java.util.function.Consumer;

public record ResponseData(ErrorCode statusCode, HirezCommandType commandType, HirezEndpoint endpoint,
                           APIResponse apiResponse) {

  public ResponseData(@Nullable ErrorCode statusCode, @Nullable HirezCommandType commandType, @NotNull HirezEndpoint endpoint, @NotNull APIResponse apiResponse) {
    this.statusCode = Objects.requireNonNullElse(statusCode, ErrorCode.UNKNOWN);
    this.commandType = commandType;
    this.apiResponse = apiResponse;
    this.endpoint = endpoint;

    if (this.statusCode != ErrorCode.SUCCESS)
      SquidlyBot.getInstance().getLogger()
          .error(
              "Error {}: {} after executing {} on endpoint {}. json={}",
              this.statusCode.name(),
              this.apiResponse.statusText(),
              Objects.requireNonNullElse(this.commandType.name(), "Nill"),
              this.endpoint.name(),
              this.apiResponse.jsonNode().toPrettyString());
  }

  public ResponseData(@NotNull HirezCommandType commandType, @NotNull HirezEndpoint endpoint, @NotNull APIResponse apiResponse) {
    this(null, commandType, endpoint, apiResponse);
  }

  public boolean isSuccess() {
    return this.statusCode == ErrorCode.SUCCESS;
  }

  public boolean isFailure() {
    return !isSuccess();
  }

  public ResponseData ifSuccess(Consumer<ResponseData> responseConsumer) {
    if (isSuccess())
      responseConsumer.accept(this);

    return this;
  }

  public ResponseData ifFailure(Consumer<ResponseData> responseConsumer) {
    if (isFailure())
      responseConsumer.accept(this);

    return this;
  }
}
