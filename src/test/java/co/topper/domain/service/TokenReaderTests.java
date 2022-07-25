package co.topper.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest
class TokenReaderTests {

    final Set<String> definedUserEmails = Set.of("gr@yahoo.com", "mateusreis@yahoo.com", "cr7@yahoo.com");

    @Autowired
    ObjectMapper objectMapper;

    TokenReader tokenReader;

    @BeforeEach
    void setup() {
        tokenReader = new TokenReader(objectMapper);
    }

    @ParameterizedTest
    @MethodSource("userTokens")
    void testGetUserEmailIds(String userToken) {
        String userEmail = tokenReader.getUserEmail(userToken);

        Assertions.assertTrue(definedUserEmails.contains(userEmail));
    }

    static Stream<Arguments> userTokens() {
        return Stream.of(
                Arguments.of("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidG9wcGVyLWFwcGxpY2F0aW9" +
                            "uLWlkIl0sInVzZXJfbmFtZSI6ImdyQHlhaG9vLmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE2N" +
                            "Tg4MjQzMjYsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiLUdWYXlzUWJsOTdQbHg3Qjd2Vjc2d2FXQkVJIiwiY2x" +
                            "pZW50X2lkIjoidG9wcGVyLWFwcCJ9.b9xDRatinjNIzpUhsZYPFZtMOX5y3p-r8eIckv4No70t5SJ7Wx4Bdk77JnFmXLrdVEp4" +
                            "jvfRaGoX-_sCFeUDB-hAeczFmIPSiF3O3-Gy6npsOUYxdxtCeBFcgr5VqReJ1Jc1D20b6ncA9Y8b1GlUUngXL68A5SfrOaztP_" +
                            "JUzRcV_Ulf79PuVQMgdqjx7STGdZbgQbq791OYcKaPspKirRq47GZLhQOhscoyMAvrzM8SBpfxDYU9iP2qnmfjQ_TFUOX-bzCR" +
                            "XC-2DlyGPDbPYvk-jol-GZ3CfjJb68PjsLoAB2xvh77KL7YdcmgK2xkF__7BgSTEXHPYh4VrJCtCSA"),

                Arguments.of("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidG9wcGVyLWFwcGxpY2F0aW9uLWlkIl0sInVzZXJfbmFtZSI" +
                        "6Im1hdGV1c3JlaXNAeWFob28uY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTY1ODgyNDQxNywiYXV0aG9yaXRpZXMiOlsiVV" +
                        "NFUiJdLCJqdGkiOiJvdjNJc0ZxcGFUa21LNUZFb3FFUGVlNHRla0EiLCJjbGllbnRfaWQiOiJ0b3BwZXItYXBwIn0.hnxeRiByofQOk74HfCFQv" +
                        "zLAJ92AQ1oz58qIQcXC__bv7FoKHbctZdxtbnKa8-7E5lx5urdjCk4I_SVwSm9Xhe8yYedtH8Lglt0c3qbdaDvq-tPfooZqtOgQuRocDYHZHQaIw" +
                        "28wFgOHe5Rrs7apM9p-nuV-TVBjnRNBdFzDlRQiYWsOwvrt5NlPDke_BJ1ho_DcYhSo23YyaeToqqFPBeGV26pF_5KWiACAcFTSPt03x4twv" +
                        "EGgnmO_GzPx5a8eacmzGC1pno_UofLP-edaOT2gdz5NtTjUlqdCDqbUfHAGdZFOkYZj-zbTRWW9Jx3Te2qeeYZX4IKvh21Eitf2cA"),

                Arguments.of("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidG9wcGVyLWFwcGxpY2F0aW9uLWlkIl0sInVzZXJfbmFtZSI" +
                        "6ImNyN0B5YWhvby5jb20iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNjU4ODI0OTk3LCJhdXRob3JpdGllcyI6WyJVU0VSIl0sIm" +
                        "p0aSI6Ik1JWDBiQ2ZTN0xwYzN2aS0wanp2dk1fQUhBRSIsImNsaWVudF9pZCI6InRvcHBlci1hcHAifQ.bWarQ68-BPaKjko1rueiw9tZWFPQ9" +
                        "ud4Gy9ZaK9H-NApxlVPo5PVq4Im-q9Z54cFH6Et13DV3q66AJC1_NjV9aceIH-C6x1IRBriwt6q9YP5csfaTns3YClOzqP0Rbhw-5UuOZvXODq" +
                        "CGa032hz4WIHmA7sxKYRZp2njMPmbMz3S1bK1Th5vP4ytWlTMCDi-EKroAgjWyanMpl5CY4FX16UTNijYTbnYQx3CK3G2zN54Nxx2njzoMURwn" +
                        "MoSKAMNj_99xK519RhbZVW6YvuMzQSw9PF5cNtioeWwYCmCypqkmb09uOy_ZYFwnRshplvWuk2QEFVmY3bSrTgcVsdtbw")
        );
    }

}
