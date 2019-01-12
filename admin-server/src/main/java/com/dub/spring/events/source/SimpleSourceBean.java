package com.dub.spring.events.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.dub.spring.events.models.BookChangeModel;

@Component
public class SimpleSourceBean {
	
    private Source source;// provided

    // implementation of Source interface injected by Spring Cloud Stream
    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishBookChange(String action, String bookId) {
 
        System.out.println("publishBookChange begin");
    	
    	BookChangeModel change =  new BookChangeModel(
                BookChangeModel.class.getTypeName(),
                action,
                bookId
                );

        source.output().send(MessageBuilder.withPayload(change).build());
    }
}