package com.dub.spring.services;


import com.dub.spring.domain.BookDocument;

public interface BookService {
	
	// only used for admin purpose		
	BookDocument setBookPrice(String bookId, int price);
}