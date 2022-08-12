package co.topper.domain.message.data;

import co.topper.domain.data.converter.InstantConvert;
import co.topper.domain.data.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Message implements Serializable {

    private static final long serialId = 12345L;

    private static final String PROPERTY_EMAIL_ID = "emailId";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_REGISTER_DATE = "registerDate";

    private final String emailId;
    private final String username;

    @InstantConvert
    private final Instant registerDate;

    @JsonCreator
    public Message(@JsonProperty(PROPERTY_EMAIL_ID) String emailId,
                   @JsonProperty(PROPERTY_USERNAME) String username,
                   @JsonProperty(PROPERTY_REGISTER_DATE) Instant registerDate) {
        this.emailId = emailId;
        this.username = username;
        this.registerDate = registerDate;
    }

    public static Message fromUser(UserEntity userEntity) {
        return new Message(
                userEntity.getEmailId(),
                userEntity.getUsername(),
                Instant.now()
        );
    }

    @JsonProperty(PROPERTY_EMAIL_ID)
    public String getEmailId() {
        return emailId;
    }

    @JsonProperty(PROPERTY_USERNAME)
    public String getUsername() {
        return username;
    }

    @JsonProperty(PROPERTY_REGISTER_DATE)
    public Instant getRegisterDate() {
        return registerDate;
    }

    @Override
    public String toString() {
        return "Message{" +
                "emailId='" + emailId + '\'' +
                ", username='" + username + '\'' +
                ", registerDate=" + registerDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return emailId.equals(message.emailId) && username.equals(message.username) && registerDate.equals(message.registerDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailId, username, registerDate);
    }

}
