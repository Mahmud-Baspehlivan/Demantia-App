package com.demantia.demantia.security;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String token;
    private final String role;
    private final String id;
    private final String type = "Bearer";

    public JwtResponse(String token, String role, String id) {
        this.token = token;
        this.role = role;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return type;
    }

    public String getRole() {
        return role;
    }

    public String getId() {
        return id;
    }
}
