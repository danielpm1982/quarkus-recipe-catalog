package com.danielpm1982;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;
import java.util.Arrays;
import java.util.HashSet;

public class GenerateToken {
    public static void main(String[] args) {
        String token = Jwt.issuer("https://danielpm1982.com")
                .upn("daniel@danielpm1982.com")
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                .claim(Claims.birthdate.name(), "1900-01-01")
                .sign();
        System.out.println(token);
        System.exit(0);
    }
}

/*
This will generate a JWT Bearer token, based on the RSA-256 encrypted public and private-key pair, whose path is set at
the application.properties. The token will be generated taking into account the info above, and will validate only for
the right issuer. If you choose to change the issuer value above, you also gotta change it at the application.properties.
If you change the claims or the groups above, you also gotta change them at the secured endpoints' configuration. Each
time you change anything, regarding the info contained at the token, you gotta generate the token again. In this sample
app, only the TokenSecureResource has endpoints secured by these public/private RSA-key encryption. We could secure the
other endpoints, at the other Resource classes, by adding the same configuration at them.
*/
