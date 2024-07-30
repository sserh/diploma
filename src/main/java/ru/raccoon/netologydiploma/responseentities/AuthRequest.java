package ru.raccoon.netologydiploma.responseentities;

import lombok.Data;

@Data
public class AuthRequest {
    private String login, password;
}
