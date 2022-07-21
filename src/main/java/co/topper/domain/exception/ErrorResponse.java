package co.topper.domain.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class ErrorResponse {

    private static final String PROPERTY_CODE = "code";
    private static final String PROPERTY_REASON = "reason";
    private static final String PROPERTY_MESSAGE = "message";

    private final Integer code;
    private final String reason;
    private final String message;

    @JsonCreator
    public ErrorResponse(@JsonProperty(PROPERTY_CODE) Integer code,
                         @JsonProperty(PROPERTY_REASON) String reason,
                         @JsonProperty(PROPERTY_MESSAGE) String message) {
        this.code = code;
        this.reason = reason;
        this.message = message;
    }

    public static ErrorResponse of(Integer code, String reason, String message) {
        return new ErrorResponse(code, reason, message);
    }

    @Override
    public String toString() {
        return "TailoredResponse{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @JsonProperty(PROPERTY_CODE)
    public Integer getCode() {
        return code;
    }

    @JsonProperty(PROPERTY_REASON)
    public String getReason() {
        return reason;
    }

    @JsonProperty(PROPERTY_MESSAGE)
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorResponse that = (ErrorResponse) o;
        return code.equals(that.code) && reason.equals(that.reason) && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, reason, message);
    }

}
