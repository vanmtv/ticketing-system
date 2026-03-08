package com.project.ticketing.service;

import com.project.ticketing.model.Ticket;
import com.project.ticketing.model.Priority;
import com.project.ticketing.model.Status;
import com.project.ticketing.repository.TicketRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

	private final TicketRepository ticketRepository;

	public TicketService(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	public Ticket createTicket(Ticket ticket) {
		if (ticket.getStatus() == null) {
			ticket.setStatus(Status.OPEN);
		}
		return ticketRepository.save(ticket);
	}

	public Ticket getTicketById(String id) {
		return ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
	}

	public void deleteTicket(String id) {
		Ticket ticket = getTicketById(id);
		ticketRepository.delete(ticket);
	}

	public List<Ticket> getTicketsByTitle(String title) {
		return ticketRepository.findByTitle(title);
	}

	public List<Ticket> getTicketsByStatus(Status status) {
		return ticketRepository.findByStatus(status);
	}

	public Ticket updateTicket(String id, Ticket ticketDetails) {
		Ticket existingTicket = ticketRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

		existingTicket.setTitle(ticketDetails.getTitle());
		existingTicket.setDescription(ticketDetails.getDescription());
		existingTicket.setStatus(ticketDetails.getStatus());
		existingTicket.setPriority(ticketDetails.getPriority());
		existingTicket.setStatus(ticketDetails.getStatus());

		return ticketRepository.save(existingTicket);
	}

	public List<Ticket> getTicketsByPriority(Priority priority) {
		return ticketRepository.findByPriority(priority);
	}

	public List<Ticket> getAllTickets() {
		return ticketRepository.findAll();
	}
}