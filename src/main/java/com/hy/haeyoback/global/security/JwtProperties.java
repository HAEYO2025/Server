package com.hy.haeyoback.global.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long expireTime;
    private long refreshExpireTime;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getRefreshExpireTime() {
        return refreshExpireTime;
    }

    public void setRefreshExpireTime(long refreshExpireTime) {
        this.refreshExpireTime = refreshExpireTime;
    }
}
