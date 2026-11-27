package com.rbglobal.dockscheduling.config;

import com.rbglobal.dockscheduling.domain.Appointment;
import com.rbglobal.dockscheduling.domain.AppointmentStatus;
import com.rbglobal.dockscheduling.domain.Dock;
import com.rbglobal.dockscheduling.domain.DockType;
import com.rbglobal.dockscheduling.repository.AppointmentRepository;
import com.rbglobal.dockscheduling.repository.DockRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DockRepository dockRepository;
    private final AppointmentRepository appointmentRepository;

    public DataSeeder(DockRepository dockRepository, AppointmentRepository appointmentRepository) {
        this.dockRepository = dockRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        Dock dock1 = dockRepository.save(new Dock("Dock 1", DockType.DRY_VAN, true));
        Dock dock2 = dockRepository.save(new Dock("Dock 2", DockType.DRY_VAN, true));
        Dock dock3 = dockRepository.save(new Dock("Dock 3", DockType.DRY_VAN, true));
        Dock dock4 = dockRepository.save(new Dock("Dock 4", DockType.REEFER, true));

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        appointmentRepository.save(new Appointment(
                dock1,
                "carrier-alpha",
                "Alpha Freight",
                DockType.DRY_VAN,
                today.atTime(LocalTime.of(8, 0)),
                today.atTime(LocalTime.of(9, 0)),
                AppointmentStatus.SCHEDULED));

        appointmentRepository.save(new Appointment(
                dock2,
                "carrier-beta",
                "Beta Logistics",
                DockType.DRY_VAN,
                today.atTime(LocalTime.of(10, 30)),
                today.atTime(LocalTime.of(11, 30)),
                AppointmentStatus.SCHEDULED));

        appointmentRepository.save(new Appointment(
                dock4,
                "carrier-gamma",
                "Gamma Transport",
                DockType.REEFER,
                today.atTime(LocalTime.of(13, 0)),
                today.atTime(LocalTime.of(14, 0)),
                AppointmentStatus.SCHEDULED));

        appointmentRepository.save(new Appointment(
                dock3,
                "carrier-alpha",
                "Alpha Freight",
                DockType.DRY_VAN,
                tomorrow.atTime(LocalTime.of(9, 0)),
                tomorrow.atTime(LocalTime.of(10, 0)),
                AppointmentStatus.SCHEDULED));

        appointmentRepository.save(new Appointment(
                dock1,
                "carrier-beta",
                "Beta Logistics",
                DockType.DRY_VAN,
                tomorrow.atTime(LocalTime.of(15, 0)),
                tomorrow.atTime(LocalTime.of(16, 0)),
                AppointmentStatus.SCHEDULED));

        appointmentRepository.save(new Appointment(
                dock4,
                "carrier-gamma",
                "Gamma Transport",
                DockType.REEFER,
                tomorrow.atTime(LocalTime.of(7, 30)),
                tomorrow.atTime(LocalTime.of(8, 30)),
                AppointmentStatus.SCHEDULED));
    }
}
