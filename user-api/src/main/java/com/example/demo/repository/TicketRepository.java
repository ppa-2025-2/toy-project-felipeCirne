package com.example.demo.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.repository.entity.Ticket;

public interface TicketRepository extends ListCrudRepository<Ticket, Integer> {
   
}
