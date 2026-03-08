package com.project.ticketing.controller;

import com.project.ticketing.model.Ticket;
import com.project.ticketing.model.Priority;
import com.project.ticketing.service.TicketService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets") // Rota base para todos os chamados
public class TicketController {

	private final TicketService ticketService;

	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@PostMapping
	public ResponseEntity<Ticket> create(@Valid @RequestBody Ticket ticket) {
		Ticket savedTicket = ticketService.createTicket(ticket);
		return ResponseEntity.ok(savedTicket);
	}

	@GetMapping
	public ResponseEntity<List<Ticket>> getAll() {
		List<Ticket> tickets = ticketService.getAllTickets();
		return ResponseEntity.ok(tickets);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Ticket>> getByTitle(@RequestParam("title") String title) {
	    List<Ticket> tickets = ticketService.getTicketsByTitle(title);
	    return ResponseEntity.ok(tickets);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Ticket> update(@PathVariable("id") String id, @RequestBody Ticket ticketDetails){
		Ticket updatedTicket = ticketService.updateTicket(id, ticketDetails);
	    return ResponseEntity.ok(updatedTicket);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<Ticket>> getByPriority(@RequestParam("priority") Priority priority) {
	    List<Ticket> tickets = ticketService.getTicketsByPriority(priority);
	    return ResponseEntity.ok(tickets);
	}
}