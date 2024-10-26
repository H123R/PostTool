package com.jax.PostTool.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtils {

    public static DecodedJWT decode(String token) {
        return JWT.decode(token);
    }

}
