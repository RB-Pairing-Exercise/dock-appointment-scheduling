package com.rbglobal.dockscheduling.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.rbglobal.dockscheduling.domain.Dock;
import com.rbglobal.dockscheduling.domain.DockType;
import com.rbglobal.dockscheduling.repository.DockRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DockServiceTest {

    @Mock
    private DockRepository dockRepository;

    @Test
    void returnsAllDocks() {
        DockService dockService = new DockService(dockRepository);
        when(dockRepository.findAll()).thenReturn(List.of(
                new Dock("Dock 1", DockType.DRY_VAN, true),
                new Dock("Dock 4", DockType.REEFER, true)));

        List<Dock> result = dockService.getAllDocks();

        assertThat(result).hasSize(2);
    }

    @Test
    void returnsEmptyListWhenNoDocksExist() {
        DockService dockService = new DockService(dockRepository);
        when(dockRepository.findAll()).thenReturn(List.of());

        List<Dock> result = dockService.getAllDocks();

        assertThat(result).isEmpty();
    }
}
