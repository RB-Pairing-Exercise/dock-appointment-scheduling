package com.rbglobal.dockscheduling.service;

import com.rbglobal.dockscheduling.domain.Dock;
import com.rbglobal.dockscheduling.repository.DockRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DockService {

    private final DockRepository dockRepository;

    public DockService(DockRepository dockRepository) {
        this.dockRepository = dockRepository;
    }

    public List<Dock> getAllDocks() {
        return dockRepository.findAll();
    }
}
