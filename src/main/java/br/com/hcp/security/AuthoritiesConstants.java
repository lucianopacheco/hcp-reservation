package br.com.hcp.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
    
    public static final String PASSENGER = "ROLE_PASSENGER";
    
    public static final String DRIVER = "ROLE_DRIVER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
