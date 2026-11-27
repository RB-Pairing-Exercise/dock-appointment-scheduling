package com.rbglobal.dockscheduling.dto;

import com.rbglobal.dockscheduling.domain.Dock;
import com.rbglobal.dockscheduling.domain.DockType;

public record DockResponse(Long id, String name, DockType dockType, boolean active) {

    public static DockResponse from(Dock dock) {
        return new DockResponse(dock.getId(), dock.getName(), dock.getDockType(), dock.isActive());
    }
}
