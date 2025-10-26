package com.example.demo.domain;

import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.Workstation;

@Service
public class IslandService {

    private final IslandRepository islandRepository;
    private final UserRepository userRepository;

    public IslandService(IslandRepository islandRepository, UserRepository userRepository) {
        this.islandRepository = islandRepository;
        this.userRepository = userRepository;
    }

    public void alocarWorkstationDisponivel(@NonNull Integer userId) {
        
        final var user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException());

        final var islands = islandRepository.findIslandWithAvailableWorkstations();

        if (islands.isEmpty()) {
            throw new IllegalStateException("Workstations not available");
        }

        Island freeIsland = islands.getFirst(); 
        for (int slots = 1; slots < Island.Disposition.CIRCULAR.getPlacements(); slots++) {
            final int positions = slots;
            var possibleIsland = islands.stream()
                .filter(i -> i.getWorkstations().stream()
                            .map(Workstation::getUser)
                            .filter(Objects::nonNull)
                            .count() == positions)
                .findFirst();
            if (possibleIsland.isPresent()) {
                freeIsland = possibleIsland.get();
                break;
            }
        }

        freeIsland.getWorkstations().stream()
            .filter(ws -> ws.getUser() == null)
            .findFirst()
            .ifPresent(ws -> ws.setUser(user));

        islandRepository.save(freeIsland);
    }
}
