package co.topper.domain.data.converter;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = InstantDeserializer.class)
@JsonSerialize(using = InstantSerializer.class)
public @interface InstantConvert {
}

class InstantSerializer extends JsonSerializer<Instant> {

    @Override
    public void serialize(Instant value, JsonGenerator jsonGenerator, SerializerProvider provider) {
        try {
            jsonGenerator.writeString(DateTimeFormatter.ISO_INSTANT.format(value));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class InstantDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext context) {
        try {
            return Instant.parse(jsonParser.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
