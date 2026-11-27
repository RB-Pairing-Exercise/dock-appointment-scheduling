package com.rbglobal.dockscheduling.controller;

import com.rbglobal.dockscheduling.dto.AppointmentResponse;
import com.rbglobal.dockscheduling.service.AppointmentService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<AppointmentResponse> getAppointments(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String carrierId) {
        return appointmentService.getAppointments(date, carrierId).stream()
                .map(AppointmentResponse::from)
                .toList();
    }
}
