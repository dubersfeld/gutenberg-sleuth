package com.dub.spring.events.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.dub.spring.events.models.BookChangeModel;
import com.dub.spring.repository.BookRedisRepository;

@EnableBinding(Sink.class)
public class BookChangeHandler {

    @Autowired
    private BookRedisRepository bookRedisRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(BookChangeHandler.class);

    @StreamListener(Sink.INPUT)
    public void loggerSink(BookChangeModel bookChange) {
       
        System.out.println("Received an event from the admin service for book id " 
      							+ bookChange.getBookId());              
        
        switch(bookChange.getAction()) {
            case "GET":
                logger.debug("Received a GET event from the organization service for organization id {}", bookChange.getBookId());
                break;
            case "SAVE":
                logger.debug("Received a SAVE event from the organization service for organization id {}", bookChange.getBookId());
                break;
            case "UPDATE":
            	System.out.println("Received a UPDATE event from the admin service for book id " 
            				+ bookChange.getBookId());                               
            	bookRedisRepository.deleteBook(bookChange.getBookId());       
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the organization service for organization id {}", bookChange.getBookId());
                bookRedisRepository.deleteBook(bookChange.getBookId());
                break;
            default:
                logger.error("Received an UNKNOWN event from the organization service of type {}", bookChange.getType());
                break;

        }
    }
    
    

}