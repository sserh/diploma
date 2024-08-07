package ru.raccoon.netologydiploma;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.RequestBuilder;
import org.testcontainers.containers.GenericContainer;
import ru.raccoon.netologydiploma.dbentities.FileInfo;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NetologyDiplomaApplicationTests {

    private static final GenericContainer<?> diplomaApp = new GenericContainer<>("diploma:1.5")
            .withExposedPorts(8090);

    String hostname = "localhost";
    String requestString = "/cloud/list?limit=3";
    String loginString = "/login";

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public static void setup() {
        diplomaApp.start();
    }

    @Test
    void checkAccessDenied() {
        ResponseEntity<Void> responseEntity = restTemplate.getForEntity("http://" + hostname + ":" + diplomaApp.getMappedPort(8090) + requestString, Void.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void CheckLogin() {
        ResponseEntity<Void> responseEntity = restTemplate.getForEntity("http://" + hostname + ":" + diplomaApp.getMappedPort(8090) + loginString, Void.class);
    }

}
