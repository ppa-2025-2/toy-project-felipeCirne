package com.example.demo.application;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.IslandSelector;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.UserRepository;


@Service
public class IslandApplicationService {

    private final IslandRepository islandRepository;
    private final UserRepository userRepository;
    private final IslandSelector islandSelector;

    public IslandApplicationService(
            IslandRepository islandRepository,
            UserRepository userRepository,
            IslandSelector islandSelector) {
        this.islandRepository = islandRepository;
        this.userRepository = userRepository;
        this.islandSelector = islandSelector;
    }

    
    @Transactional
    public void alocarWorkstationDisponivel(@NonNull Integer userId) {
        
        
        final var user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException());

        final var availableIslands = islandRepository.findIslandWithAvailableWorkstations();

        
        final var selectedIsland = islandSelector.selectBestIsland(availableIslands);
        
       
        selectedIsland.assignUserToFirstAvailableWorkstation(user);

   
        islandRepository.save(selectedIsland);
    }
}