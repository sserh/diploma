package ru.raccoon.netologydiploma.component;

import lombok.Data;

@Data
public class AuthRequest {
    private String login, password;
}
