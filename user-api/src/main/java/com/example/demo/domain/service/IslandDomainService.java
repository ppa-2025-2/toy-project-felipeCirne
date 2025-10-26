package com.example.demo.domain.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.User;
import com.example.demo.repository.entity.Workstation;


@Service
public class IslandDomainService {

 
    public Island findBestIslandForAllocation(List<Island> availableIslands) {
        if (availableIslands.isEmpty()) {
            throw new IllegalStateException("Workstations not available");
        }

        Island bestIsland = availableIslands.getFirst();
        
        for (int slots = 1; slots < Island.Disposition.CIRCULAR.getPlacements(); slots++) {
            final int positions = slots;
            var possibleIsland = availableIslands.stream()
                .filter(i -> i.getWorkstations().stream()
                            .map(Workstation::getUser)
                            .filter(Objects::nonNull)
                            .count() == positions)
                .findFirst();
            if (possibleIsland.isPresent()) {
                bestIsland = possibleIsland.get();
                break;
            }
        }
        
        return bestIsland;
    }

    
    public void allocateUserToIsland(Island island, User user) {
        island.getWorkstations().stream()
            .filter(ws -> ws.getUser() == null)
            .findFirst()
            .ifPresentOrElse(
                ws -> ws.setUser(user),
                () -> {
                    throw new IllegalStateException("Nenhuma workstation disponível na ilha");
                }
            );
    }
}