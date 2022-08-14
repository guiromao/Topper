package co.topper.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class TokenObject {

    private static final String PROPERTY_ACCESS_TOKEN = "access_token";
    private static final String PROPERTY_TOKEN_TYPE = "token_type";
    private static final String PROPERTY_REFRESH_TOKEN = "refresh_token";
    private static final String PROPERTY_EXPIRES_IN = "expiresIn";
    private static final String PROPERTY_SCOPE = "scope";
    private static final String PROPERTY_JTI = "jti";

    private final String accessToken;
    private final String tokenType;
    private final String refreshToken;
    private final Long expiresIn;
    private final String scope;
    private final String jti;

    @JsonCreator
    public TokenObject(@JsonProperty(PROPERTY_ACCESS_TOKEN) String accessToken,
                       @JsonProperty(PROPERTY_TOKEN_TYPE) String tokenType,
                       @JsonProperty(PROPERTY_REFRESH_TOKEN) String refreshToken,
                       @JsonProperty(PROPERTY_EXPIRES_IN) Long expiresIn,
                       @JsonProperty(PROPERTY_SCOPE) String scope,
                       @JsonProperty(PROPERTY_JTI) String jti) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.jti = jti;
    }

    @JsonProperty(PROPERTY_ACCESS_TOKEN)
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty(PROPERTY_TOKEN_TYPE)
    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty(PROPERTY_REFRESH_TOKEN)
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty(PROPERTY_EXPIRES_IN)
    public Long getScopes() {
        return expiresIn;
    }

    @JsonProperty(PROPERTY_SCOPE)
    public String getScope() {
        return scope;
    }

    @JsonProperty(PROPERTY_JTI)
    public String getJti() {
        return jti;
    }
}
