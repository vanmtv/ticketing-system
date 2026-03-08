package com.project.ticketing.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.ticketing.model.Priority;
import com.project.ticketing.model.Status;
import com.project.ticketing.model.Ticket;
import com.project.ticketing.repository.TicketRepository;
import com.project.ticketing.util.TicketTestData;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

	@Mock
	private TicketRepository ticketRepository;

	@InjectMocks
	private TicketService ticketService;

	private Ticket testTicket;
	private String validId;

	@BeforeEach
	void setUp() {
		testTicket = TicketTestData.createValidTicket();
		validId = testTicket.getId();
	}

	@Test
	void shouldSetDefaultStatusWhenNull() {
		// Arrange
		testTicket.setStatus(null);

		when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		Ticket result = ticketService.createTicket(testTicket);

		// Assert
		assertNotNull(result);
		assertEquals(Status.OPEN, result.getStatus());
		verify(ticketRepository, times(1)).save(any(Ticket.class));
	}

	@Test
	void shouldKeepProvidedStatus() {
		testTicket.setStatus(Status.IN_PROGRESS);

		when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		Ticket result = ticketService.createTicket(testTicket);

		// Assert
		assertEquals(Status.IN_PROGRESS, result.getStatus());
		verify(ticketRepository).save(testTicket);
	}

	@Test
	void getTicketByIdNotFound() {
		when(ticketRepository.findById("invalidId")).thenReturn(Optional.empty());

		Exception exception = assertThrows(RuntimeException.class, () -> {
			ticketService.getTicketById("invalidId");
		});

		assertTrue(exception.getMessage().contains("Ticket not found"));
	}

	@Test
	void deleteTicket_Success() {
		when(ticketRepository.findById(validId)).thenReturn(Optional.of(testTicket));
		doNothing().when(ticketRepository).delete(testTicket);

		assertDoesNotThrow(() -> ticketService.deleteTicket(validId));

		verify(ticketRepository, times(1)).delete(testTicket);
	}

	@Test
	void updateTicket_Success() {
		Ticket updateDetails = new Ticket();
		updateDetails.setTitle("Updated Title");
		updateDetails.setDescription("Updated Desc");
		updateDetails.setStatus(Status.IN_PROGRESS);
		updateDetails.setPriority(Priority.HIGH);

		when(ticketRepository.findById(validId)).thenReturn(Optional.of(testTicket));
		when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));

		Ticket updated = ticketService.updateTicket(validId, updateDetails);

		assertEquals("Updated Title", updated.getTitle());
		assertEquals(Status.IN_PROGRESS, updated.getStatus());
		assertEquals(Priority.HIGH, updated.getPriority());
		verify(ticketRepository).save(any(Ticket.class));
	}

	@Test
	void filterMethodsCoverage() {
		List<Ticket> ticketList = List.of(testTicket);

		when(ticketRepository.findByTitle("Search")).thenReturn(ticketList);
		when(ticketRepository.findByStatus(Status.OPEN)).thenReturn(ticketList);
		when(ticketRepository.findByPriority(Priority.MEDIUM)).thenReturn(ticketList);
		when(ticketRepository.findAll()).thenReturn(ticketList);

		assertFalse(ticketService.getTicketsByTitle("Search").isEmpty());
		assertFalse(ticketService.getTicketsByStatus(Status.OPEN).isEmpty());
		assertFalse(ticketService.getTicketsByPriority(Priority.MEDIUM).isEmpty());
		assertFalse(ticketService.getAllTickets().isEmpty());
	}

	@Test
    void testCruOperations() {
        // Arrange
        when(ticketRepository.findById(validId)).thenReturn(Optional.of(testTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

        // Act & Assert (Covers getTicketById, updateTicket, and deleteTicket)
        assertNotNull(ticketService.getTicketById(validId));
        assertNotNull(ticketService.updateTicket(validId, testTicket));
        assertDoesNotThrow(() -> ticketService.deleteTicket(validId));
        
        verify(ticketRepository, atLeastOnce()).findById(validId);
    }

    @Test
    void testFiltersAndExceptions() {
        // Arrange
        when(ticketRepository.findByTitle(anyString())).thenReturn(List.of(testTicket));
        when(ticketRepository.findByStatus(any(Status.class))).thenReturn(List.of(testTicket));
        when(ticketRepository.findByPriority(any(Priority.class))).thenReturn(List.of(testTicket));
        when(ticketRepository.findAll()).thenReturn(List.of(testTicket));
        when(ticketRepository.findById("none")).thenReturn(Optional.empty());

        // Act & Assert
        assertFalse(ticketService.getTicketsByTitle("test").isEmpty());
        assertFalse(ticketService.getTicketsByStatus(Status.OPEN).isEmpty());
        assertFalse(ticketService.getTicketsByPriority(Priority.HIGH).isEmpty());
        assertFalse(ticketService.getAllTickets().isEmpty());
        
        assertThrows(RuntimeException.class, () -> ticketService.getTicketById("none"));
    }
}