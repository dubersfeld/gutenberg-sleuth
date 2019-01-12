package com.dub.spring.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.spring.domain.BookDocument;
import com.dub.spring.events.source.SimpleSourceBean;
import com.dub.spring.exceptions.BookNotFoundException;
import com.dub.spring.repository.BookRepository;


@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	SimpleSourceBean simpleSourceBean;

	@Autowired
	private BookRepository bookRepository;
		

	@Override
	public BookDocument setBookPrice(String bookId, int price) {
		Optional<BookDocument> book = bookRepository.findById(bookId);
		
		if (book.isPresent()) {
			BookDocument thisBook = book.get();
			thisBook.setPrice(price); 
			// publish change message
			simpleSourceBean.publishBookChange("UPDATE", bookId);
			return bookRepository.save(thisBook);
		} else {
			throw new BookNotFoundException();
		}
		
	}
}