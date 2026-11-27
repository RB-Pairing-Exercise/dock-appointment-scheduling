package com.rbglobal.dockscheduling.repository;

import com.rbglobal.dockscheduling.domain.Dock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DockRepository extends JpaRepository<Dock, Long> {
}
