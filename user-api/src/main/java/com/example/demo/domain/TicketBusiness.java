package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewTicketDTO;
import com.example.demo.controller.dto.PatchTicketDTO;
import com.example.demo.domain.stereotype.Business;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Ticket;
import com.example.demo.repository.entity.User;

import jakarta.validation.Valid;


@Business
@Validated
public class TicketBusiness {

    private TicketRepository ticketRepository;

    private UserRepository userRepository;

    public TicketBusiness(
        TicketRepository ticketRepository,
        UserRepository userRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Ticket createTicket(@Valid NewTicketDTO newTicket){
        var ticket = new Ticket();
        
        var owner = userRepository.findById(newTicket.ownerId()).orElseThrow(() ->
            new IllegalArgumentException("Owner não encontrado")
        );
        
        ticket.setOwner(owner);
        ticket.setRecipient(owner);
        // observers.add(owner);

        newTicket.recipientId().ifPresent(id -> {
            var recipient = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipient não encontrado"));
            ticket.setRecipient(recipient);
        });

        ticket.setAction(newTicket.action());
        ticket.setDetails(newTicket.details());
        ticket.setObject(newTicket.object());
        ticket.setLocal(newTicket.local());
        
        return ticketRepository.save(ticket);
    }

    public Ticket patchTicket(Integer ticketId,@Valid PatchTicketDTO patchTicket){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
            new IllegalArgumentException("Ticket com Id " +ticketId+" não encontrado"));

        if (ticket.isCanceled())
            throw new IllegalArgumentException("Ticket Já foi cancelado e não pode ser alterado");
        
        var managerId = patchTicket.managerId();
        User manager = userRepository.findById(managerId).orElseThrow(() ->
            new IllegalArgumentException("managerId não existe"));

        
        var status = Ticket.Status.parse(patchTicket.status())
            .orElseThrow(()-> new IllegalArgumentException("Status Inválido"));        

        if (status == Ticket.Status.CANCELED && patchTicket.cancelReason().isEmpty()){
            throw new IllegalArgumentException("Caso Status cancelled enviar cancelReason");
        }
        
        ticket.setCancelReason(patchTicket.cancelReason().get());
        ticket.setManager(manager);
        ticket.setStatus(status);
        ticket.setUpdatedAt(LocalDateTime.now());
        
        System.out.println(ticket.getObservers());
        
        return ticketRepository.save(ticket);
    }
}
