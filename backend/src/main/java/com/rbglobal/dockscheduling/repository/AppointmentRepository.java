package com.rbglobal.dockscheduling.repository;

import com.rbglobal.dockscheduling.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
