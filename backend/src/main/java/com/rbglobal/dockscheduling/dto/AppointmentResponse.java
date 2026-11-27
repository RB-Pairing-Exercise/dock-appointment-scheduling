package com.rbglobal.dockscheduling.dto;

import com.rbglobal.dockscheduling.domain.Appointment;
import com.rbglobal.dockscheduling.domain.AppointmentStatus;
import com.rbglobal.dockscheduling.domain.DockType;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long dockId,
        String dockName,
        DockType dockType,
        String carrierId,
        String carrierName,
        DockType trailerType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AppointmentStatus status) {

    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDock().getId(),
                appointment.getDock().getName(),
                appointment.getDock().getDockType(),
                appointment.getCarrierId(),
                appointment.getCarrierName(),
                appointment.getTrailerType(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getStatus());
    }
}
