package com.example.STKpush.service;

import com.example.STKpush.config.MpesaConfigurations;
import com.example.STKpush.dtos.AccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import okhttp3.OkHttpClient;
import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import okhttp3.Response;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DarajaServiceImplTest {

    @Mock
    MpesaConfigurations configs;

    @InjectMocks
    static DarajaServiceImpl service;

    @Spy
    static ObjectMapper objectMapper;

    @Spy
    static OkHttpClient okHttpClient;

    private static WireMockServer server = null;
    private static int port = 8090;
    private static String host = "localhost";



    @BeforeAll
    public static void startServer() {

        objectMapper = new ObjectMapper();
        okHttpClient = new OkHttpClient();
        service = new DarajaServiceImpl();
        server = new WireMockServer(wireMockConfig().bindAddress(host).port(port));
        server.start();
        WireMock.configureFor(host,port);
        setUpServer();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }


    static void  setUpServer(){
        stubFor(get(urlEqualTo("/auth?grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"access_token\": \"WERTYUIOPLKVBN\",\"expires_in\": \"56\"}")));




    }

    @Test
    void timeDiffrenceTest(){
        LocalDateTime start = LocalDateTime.now();

        when(configs.getConsumerKey()).thenReturn("kAg5pZcWJVfuFihVaSIXNwN170IeBIYY");
        when(configs.getConsumerSecret()).thenReturn("ELLQw8YWcnvnwYVj");
        when(configs.getOauthEndpoint()).thenReturn("http://localhost:8090/auth");
        when(configs.getGrantType()).thenReturn("client_credentials");
        System.out.println();


        AccessToken sample = service.getAccess();
        AccessToken exected = service.getAccess();

        assertEquals(exected,sample);

    }

    @Test
    void getAccess() throws IOException {


        AccessToken expected  = new AccessToken();
        expected.setAccessToken("WERTYUIOPLKVBN");
        expected.setExpiresIn("56");

        when(configs.getConsumerKey()).thenReturn("kAg5pZcWJVfuFihVaSIXNwN170IeBIYY");
        when(configs.getConsumerSecret()).thenReturn("ELLQw8YWcnvnwYVj");
        when(configs.getOauthEndpoint()).thenReturn("http://localhost:8090/auth");
        when(configs.getGrantType()).thenReturn("client_credentials");

        AccessToken actual = service.getAccess();
        expected.setAcquiredAt(actual.getAcquiredAt());
        assertEquals(expected,actual);

    }



    @Test
    void generateSTKPush() {
    }

    @Test
    void stk_request() {
        /**
         * Accepts external req and sends stk
         *
         * */



    }
}