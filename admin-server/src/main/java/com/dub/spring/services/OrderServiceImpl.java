package com.dub.spring.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.dub.spring.domain.OrderDocument;
import com.dub.spring.domain.OrderState;
import com.dub.spring.exceptions.OrderException;
import com.dub.spring.exceptions.OrderNotFoundException;
import com.dub.spring.repository.OrderRepository;


@Service
public class OrderServiceImpl implements OrderService {

	@Autowired 
	private OrderRepository orderRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Override
	public void setShipped(String orderId) {
		
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(orderId));
											
		Update update = new Update();
		update.set("state", OrderState.SHIPPED);
		
		OrderDocument oldOrder = this.getRawOrder(orderId);
			
		OrderState oldState = oldOrder.getState();
			
		if (!oldState.equals(OrderState.PRE_SHIPPING)) {
			throw new OrderException();
		}
		
		mongoOperations.findAndModify(query, update, options, OrderDocument.class);
	}
	

	private OrderDocument getRawOrder(String orderId) {
		Optional<OrderDocument> order = orderRepository.findById(orderId);
		if (order.isPresent()) {
			return order.get();
		} else {			
			throw new OrderNotFoundException();
		}
	}
	
}
