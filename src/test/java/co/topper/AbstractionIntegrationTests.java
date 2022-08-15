package co.topper;

import co.topper.util.TokenObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles(value = "test")
@Profile("test")
@PropertySource({"classpath:credentials.properties"})
public abstract class AbstractionIntegrationTests {

    @LocalServerPort
    protected int port;

    @Value("${app.secret}")
    private String secret;

    @Autowired
    ObjectMapper mapper;

    protected ValidatableResponse get(String path, Headers headers, ResponseSpecification responseSpec) {
        if (Objects.isNull(headers)) {
            headers = new Headers(new Header("name", "value"));
        }

        try {
            final URL url = new URL("http://localhost:" + port + "/");
            return given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                    .baseUri(url.toString()).headers(headers)
                    .when().get(path)
                    .then().spec(responseSpec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException();
        }
    }

    protected ValidatableResponse post(String path, Headers headers,
                                       String bodyJson, ResponseSpecification responseSpec) {
        if (Objects.isNull(headers)) {
            headers = new Headers(new Header("name", "value"));
        }

        if (Objects.isNull(bodyJson)) {
            bodyJson = "";
        }

        try {
            final URL url = new URL("http://localhost:" + port + "/");
            return given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                    .baseUri(url.toString()).headers(headers)
                    .body(bodyJson)
                    .contentType(ContentType.JSON)
                    .when().post(path)
                    .then().spec(responseSpec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException();
        }
    }

    protected ValidatableResponse put(String path, Headers headers,
                                       String bodyJson, ResponseSpecification responseSpec) {
        if (Objects.isNull(headers)) {
            headers = new Headers(new Header("name", "value"));
        }

        if (Objects.isNull(bodyJson)) {
            bodyJson = "";
        }

        try {
            final URL url = new URL("http://localhost:" + port + "/");
            return given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                    .baseUri(url.toString()).headers(headers)
                    .body(bodyJson)
                    .contentType(ContentType.JSON)
                    .when().put(path)
                    .then().spec(responseSpec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException();
        }
    }

    protected ValidatableResponse delete(String path, Headers headers,
                                      ResponseSpecification responseSpec) {
        if (Objects.isNull(headers)) {
            headers = new Headers(new Header("name", "value"));
        }

        try {
            final URL url = new URL("http://localhost:" + port + "/");
            return given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                    .baseUri(url.toString()).headers(headers)
                    .when().delete(path)
                    .then().spec(responseSpec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException();
        }
    }

    protected ValidatableResponse login(String emailId, String password,
                                        ResponseSpecification responseSpec) {

        try {
            final URL url = new URL("http://localhost:" + port + "/");
            return given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                    .baseUri(url.toString())
                    .auth().basic("topper-app", secret)
                    .queryParam("username", emailId)
                    .queryParam("password", password)
                    .queryParam("grant_type", "password")
                    .queryParam("scopes", "read write")
                    .contentType(ContentType.JSON)
                    .when().post("/oauth/token")
                    .then().spec(responseSpec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException();
        }
    }

    protected String getToken(String emailId, String password) throws Exception {
        String responseJson = login(emailId, password, responseOk()).extract().asString();

        TokenObject tokenObject = mapper.readValue(responseJson, TokenObject.class);

        return tokenObject.getAccessToken();
    }

    protected ResponseSpecification responseOk() {
        return new ResponseSpecBuilder()
                .expectBody(not(emptyOrNullString()))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(HttpStatus.OK.value())
                .build();
    }

    protected ResponseSpecification withStatus(HttpStatus status) {
        return new ResponseSpecBuilder()
                .expectBody(not(emptyOrNullString()))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(status.value())
                .build();
    }

    protected ResponseSpecification okStatus() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.OK.value())
                .build();
    }

}
