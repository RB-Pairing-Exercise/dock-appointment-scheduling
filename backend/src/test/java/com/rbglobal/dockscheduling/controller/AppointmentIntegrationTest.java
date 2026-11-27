package com.rbglobal.dockscheduling.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.rbglobal.dockscheduling.dto.AppointmentResponse;
import com.rbglobal.dockscheduling.dto.DockResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppointmentIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void contextLoads() {
        assertThat(port).isGreaterThan(0);
    }

    @Test
    void getDocksReturnsFourSeededDocks() {
        DockResponse[] docks = restTemplate.getForObject(url("/api/docks"), DockResponse[].class);

        assertThat(docks).hasSize(4);
        assertThat(docks).extracting(DockResponse::name)
                .contains("Dock 1", "Dock 2", "Dock 3", "Dock 4");
    }

    @Test
    void getAppointmentsReturnsAllSixSeededAppointments() {
        AppointmentResponse[] appointments =
                restTemplate.getForObject(url("/api/appointments"), AppointmentResponse[].class);

        assertThat(appointments).hasSize(6);
    }

    @Test
    void getAppointmentsFiltersByCarrierId() {
        AppointmentResponse[] appointments = restTemplate.getForObject(
                url("/api/appointments?carrierId=carrier-alpha"), AppointmentResponse[].class);

        assertThat(appointments).hasSize(2);
        assertThat(appointments).allMatch(appointment -> "carrier-alpha".equals(appointment.carrierId()));
    }

    @Test
    void getAppointmentsFiltersByDate() {
        String today = LocalDate.now().toString();

        AppointmentResponse[] appointments = restTemplate.getForObject(
                url("/api/appointments?date=" + today), AppointmentResponse[].class);

        assertThat(appointments).hasSize(3);
    }

    @Test
    void getAppointmentsFiltersByDateAndCarrierId() {
        String tomorrow = LocalDate.now().plusDays(1).toString();

        AppointmentResponse[] appointments = restTemplate.getForObject(
                url("/api/appointments?date=" + tomorrow + "&carrierId=carrier-gamma"),
                AppointmentResponse[].class);

        assertThat(appointments).hasSize(1);
        assertThat(appointments[0].carrierId()).isEqualTo("carrier-gamma");
    }
}
