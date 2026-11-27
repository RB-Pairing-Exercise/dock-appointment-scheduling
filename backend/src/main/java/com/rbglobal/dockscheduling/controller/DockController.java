package com.rbglobal.dockscheduling.controller;

import com.rbglobal.dockscheduling.dto.DockResponse;
import com.rbglobal.dockscheduling.service.DockService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/docks")
public class DockController {

    private final DockService dockService;

    public DockController(DockService dockService) {
        this.dockService = dockService;
    }

    @GetMapping
    public List<DockResponse> getDocks() {
        return dockService.getAllDocks().stream().map(DockResponse::from).toList();
    }
}
