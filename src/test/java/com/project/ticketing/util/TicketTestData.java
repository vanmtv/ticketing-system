package com.project.ticketing.util;

import com.project.ticketing.model.Priority;
import com.project.ticketing.model.Status;
import com.project.ticketing.model.Ticket;
import java.util.UUID;

public class TicketTestData {

	public static String generateMockId() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 24);
	}

	public static Ticket createValidTicket() {
		Ticket ticket = new Ticket();
		ticket.setId(generateMockId());
		ticket.setTitle("Test Ticket " + UUID.randomUUID().toString().substring(0, 8));
		ticket.setDescription("This is a ticket description");
		ticket.setPriority(Priority.MEDIUM);
		ticket.setStatus(Status.OPEN);
		return ticket;
	}
}