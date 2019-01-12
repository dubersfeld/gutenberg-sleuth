package com.dub.spring.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.dub.spring.domain.Book;

@Repository
public class BookRedisRepositoryImpl implements BookRedisRepository {

	private static final String HASH_NAME ="book";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Object> hashOperations;
	
    
    public BookRedisRepositoryImpl() {
        super();
    }
    
    @Autowired
    private BookRedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }
    
  
	@Override
	public Book findBookById(String bookId) {
		Book book = (Book) hashOperations.get(HASH_NAME, bookId);
		return book;
	}

	@Override
	public void saveBook(Book book) {     
		hashOperations.put(HASH_NAME, book.getId(), book);
	}

	@Override
	public void deleteBook(String bookId) {
		hashOperations.delete(HASH_NAME, bookId);	
	}

}
