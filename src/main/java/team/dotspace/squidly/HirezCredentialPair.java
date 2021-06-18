/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HirezCredentialPair(
    @JsonProperty("devID") String devID,
    @JsonProperty("authKEY") String authKEY) {
}
