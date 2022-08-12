package co.topper;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles(value = "test")
public abstract class AbstractionIntegrationTests {

    @LocalServerPort
    protected int port;

    /*protected ValidatableResponse get(String path, ResponseSpecification responseSpec) {
        try {
            final URL url = new URL("http://localhost:" + port + "/");
            return given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                    .baseUri(url.toString())
                    .when().get(path)
                    .then().spec(responseSpec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException();
        }
    }*/

    protected ValidatableResponse post(String path, Headers headers, ResponseSpecification responseSpec) {
        try {
            final URL url = new URL("http://localhost:" + port + "/");
            return given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter())
                    .baseUri(url.toString()).headers(headers)
                    .when().post(path)
                    .then().spec(responseSpec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException();
        }
    }

    protected ResponseSpecification responseOk() {
        return new ResponseSpecBuilder()
                .expectBody(not(emptyOrNullString()))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(HttpStatus.OK.value())
                .build();
    }

}
