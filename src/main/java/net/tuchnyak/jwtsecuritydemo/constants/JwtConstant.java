package net.tuchnyak.jwtsecuritydemo.constants;

public interface JwtConstant {

    String HEADER_AUTHORIZATION_NAME = "Authorization";
    String HEADER_AUTHORIZATION_BEARER_PREFIX = "Bearer ";
    long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
}
