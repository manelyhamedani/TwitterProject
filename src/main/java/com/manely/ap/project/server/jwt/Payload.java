package com.manely.ap.project.server.jwt;


public record Payload (String sub, int id, String aud, long iat, long exp) {
    public String getSub() {
        return sub;
    }

    public long getExp() {
        return exp;
    }

}
