package com.example.demo.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.repository.entity.Island;


@Component
public class IslandSelector {

   
    public Island selectBestIsland(List<Island> availableIslands) {
        if (availableIslands.isEmpty()) {
            throw new IllegalStateException("Workstations not available");
        }

        Island bestIsland = availableIslands.getFirst();
        
        
        for (int slots = 1; slots < Island.Disposition.CIRCULAR.getPlacements(); slots++) {
            final long positions = slots;
            var possibleIsland = availableIslands.stream()
                .filter(i -> i.countOccupiedWorkstations() == positions)
                .findFirst();
            
            if (possibleIsland.isPresent()) {
                bestIsland = possibleIsland.get();
                break;
            }
        }
        
        return bestIsland;
    }
}