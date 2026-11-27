package com.rbglobal.dockscheduling.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.rbglobal.dockscheduling.domain.Appointment;
import com.rbglobal.dockscheduling.domain.AppointmentStatus;
import com.rbglobal.dockscheduling.domain.Dock;
import com.rbglobal.dockscheduling.domain.DockType;
import com.rbglobal.dockscheduling.repository.AppointmentRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentService appointmentService;

    private Dock dock1;
    private LocalDate today;
    private LocalDate tomorrow;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(appointmentRepository);
        dock1 = new Dock("Dock 1", DockType.DRY_VAN, true);
        dock1.setId(1L);
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
    }

    private Appointment appointment(String carrierId, LocalDate date, AppointmentStatus status) {
        return new Appointment(
                dock1,
                carrierId,
                carrierId + "-name",
                DockType.DRY_VAN,
                date.atTime(LocalTime.of(8, 0)),
                date.atTime(LocalTime.of(9, 0)),
                status);
    }

    @Test
    void filtersByCarrierId() {
        when(appointmentRepository.findAll()).thenReturn(List.of(
                appointment("carrier-alpha", today, AppointmentStatus.SCHEDULED),
                appointment("carrier-beta", today, AppointmentStatus.SCHEDULED)));

        List<Appointment> result = appointmentService.getAppointments(null, "carrier-alpha");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCarrierId()).isEqualTo("carrier-alpha");
    }

    @Test
    void filtersByDate() {
        when(appointmentRepository.findAll()).thenReturn(List.of(
                appointment("carrier-alpha", today, AppointmentStatus.SCHEDULED),
                appointment("carrier-alpha", tomorrow, AppointmentStatus.SCHEDULED)));

        List<Appointment> result = appointmentService.getAppointments(tomorrow, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStartTime().toLocalDate()).isEqualTo(tomorrow);
    }

    @Test
    void filtersByCarrierIdAndDateCombined() {
        when(appointmentRepository.findAll()).thenReturn(List.of(
                appointment("carrier-alpha", today, AppointmentStatus.SCHEDULED),
                appointment("carrier-beta", today, AppointmentStatus.SCHEDULED),
                appointment("carrier-alpha", tomorrow, AppointmentStatus.SCHEDULED)));

        List<Appointment> result = appointmentService.getAppointments(today, "carrier-alpha");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCarrierId()).isEqualTo("carrier-alpha");
        assertThat(result.get(0).getStartTime().toLocalDate()).isEqualTo(today);
    }

    @Test
    void excludesCancelledAppointments() {
        when(appointmentRepository.findAll()).thenReturn(List.of(
                appointment("carrier-alpha", today, AppointmentStatus.CANCELLED)));

        List<Appointment> result = appointmentService.getAppointments(null, null);

        assertThat(result).isEmpty();
    }

    @Test
    void returnsEmptyListWhenNoAppointmentsMatch() {
        when(appointmentRepository.findAll()).thenReturn(List.of(
                appointment("carrier-alpha", today, AppointmentStatus.SCHEDULED)));

        List<Appointment> result = appointmentService.getAppointments(null, "carrier-zzz");

        assertThat(result).isEmpty();
    }

    @Test
    void returnsEmptyListWhenRepositoryIsEmpty() {
        when(appointmentRepository.findAll()).thenReturn(List.of());

        List<Appointment> result = appointmentService.getAppointments(today, "carrier-alpha");

        assertThat(result).isEmpty();
    }
}
