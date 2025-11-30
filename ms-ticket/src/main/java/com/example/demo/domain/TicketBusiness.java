package com.example.demo.domain;

import java.time.LocalDateTime;

import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewTicketDTO;
import com.example.demo.controller.dto.PatchTicketDTO;
import com.example.demo.domain.stereotype.Business;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.entity.Ticket;

import jakarta.validation.Valid;

@Business 
@Validated
public class TicketBusiness {

    private final TicketRepository ticketRepository;

    public TicketBusiness(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(@Valid NewTicketDTO newTicket) {
        var ticket = new Ticket();

        ticket.setOwnerId(newTicket.ownerId());

        newTicket.recipientId().ifPresent(ticket::setRecipientId);

        ticket.setAction(newTicket.action());
        ticket.setDetails(newTicket.details());
        ticket.setObject(newTicket.object());
        ticket.setLocal(newTicket.local());

        return ticketRepository.save(ticket);
    }

    public Ticket patchTicket(Integer ticketId, @Valid PatchTicketDTO patchTicket) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()
                -> new IllegalArgumentException("Ticket com Id " + ticketId + " não encontrado"));

        if (ticket.isCanceled()) {
            throw new IllegalArgumentException("Ticket já foi cancelado e não pode ser alterado");
        }

        ticket.setManagerId(patchTicket.managerId());

        var status = Ticket.Status.parse(patchTicket.status())
                .orElseThrow(() -> new IllegalArgumentException("Status Inválido"));

        if (status == Ticket.Status.CANCELED && (patchTicket.cancelReason() == null || patchTicket.cancelReason().isEmpty())) {
            throw new IllegalArgumentException("Caso Status cancelled enviar cancelReason");
        }

        if (patchTicket.cancelReason().isPresent()) {
            ticket.setCancelReason(patchTicket.cancelReason().get());
        }

        ticket.setStatus(status);
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }
}
