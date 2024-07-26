package ru.raccoon.netologydiploma.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthToken {

    @JsonProperty("auth-token")
    private String token;
}
