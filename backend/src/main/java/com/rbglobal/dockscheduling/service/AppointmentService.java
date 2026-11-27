package com.rbglobal.dockscheduling.service;

import com.rbglobal.dockscheduling.domain.Appointment;
import com.rbglobal.dockscheduling.domain.AppointmentStatus;
import com.rbglobal.dockscheduling.repository.AppointmentRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Returns scheduled appointments, optionally filtered by date and/or carrierId.
     */
    public List<Appointment> getAppointments(LocalDate date, String carrierId) {
        return appointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.SCHEDULED)
                .filter(appointment -> date == null || appointment.getStartTime().toLocalDate().equals(date))
                .filter(appointment -> !StringUtils.hasText(carrierId)
                        || appointment.getCarrierId().equalsIgnoreCase(carrierId))
                .toList();
    }
}
