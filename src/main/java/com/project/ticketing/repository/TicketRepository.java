package com.project.ticketing.repository;

import com.project.ticketing.model.Priority;
import com.project.ticketing.model.Status;
import com.project.ticketing.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findByTitle(String title);
    
    List<Ticket> findByTitleContainingIgnoreCase(String title);
    
    List<Ticket> findByPriority(Priority priority);

	List<Ticket> findByStatus(Status status);
}