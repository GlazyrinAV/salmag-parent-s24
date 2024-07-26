package ru.avg.feedbackservice.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocBeans {

    @Configuration
    @SecurityScheme(
            name = "keycloak",
            type = SecuritySchemeType.OAUTH2,
            flows = @OAuthFlows(
                    authorizationCode = @OAuthFlow(
                            authorizationUrl = "${keycloak.uri}/realms/selmag/protocol/openid-connect/auth",
                            tokenUrl = "${keycloak.uri}/realms/selmag/protocol/openid-connect/token",
                            scopes = {
                                    @OAuthScope(name = "openid"),
                                    @OAuthScope(name = "microprofile-jwt")
                            }
                    ))
    )
    public static class SpringDocSecurity {
    }
}