package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.IslandApplicationService;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.entity.Island;

@RestController
@RequestMapping("/api/v1/islands")
public class IslandController {

    private final IslandApplicationService islandApplicationService;
    private final IslandRepository islandRepository;

    public IslandController(
            IslandApplicationService islandApplicationService,
            IslandRepository islandRepository) {
        this.islandApplicationService = islandApplicationService;
        this.islandRepository = islandRepository;
    }

    @PostMapping("/allocate/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> allocateWorkstation(@PathVariable Integer userId) {
        islandApplicationService.alocarWorkstationDisponivel(userId);
        return ResponseEntity.ok("Workstation alocada com sucesso para o usuário " + userId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Island>> getAllIslands() {
        return ResponseEntity.ok(islandRepository.findAll());
    }

    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Island>> getAvailableIslands() {
        return ResponseEntity.ok(islandRepository.findIslandWithAvailableWorkstations());
    }
}