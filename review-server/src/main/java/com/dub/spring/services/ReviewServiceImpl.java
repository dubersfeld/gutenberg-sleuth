package com.dub.spring.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.dub.spring.domain.BookRating;
import com.dub.spring.domain.Review;
import com.dub.spring.domain.ReviewDocument;
import com.dub.spring.exceptions.NoReviewFoundException;
import com.dub.spring.exceptions.ReviewNotFoundException;
import com.dub.spring.repository.ReviewRepository;
import com.dub.spring.utils.ReviewUtils;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	

	@Override
	public Review createReview(Review review) {
		
		/**
		 * remove all hard coupling between book-server and review-server
		 * replace it by a messaging from review-server to book server
		 * */
			
		ReviewDocument newReview  = reviewRepository.save(ReviewUtils.reviewToDocument(review));
					
		return ReviewUtils.documentToReview(newReview);
	}

	@Override
	public List<Review> getReviewByBookId(String bookId) {
		List<ReviewDocument> docs = reviewRepository
							.findByBookId(new ObjectId(bookId));
		
		List<Review> reviews = new ArrayList<>();
		for (ReviewDocument doc : docs) {
			reviews.add(ReviewUtils.documentToReview(doc));			
		}
		return reviews;
	}


	@Override
	public List<Review> getReviewByBookId(String bookId, String sortBy) {

		List<ReviewDocument> docs = reviewRepository.findByBookId(
													new ObjectId(bookId), 
													new Sort(Sort.Direction.DESC, sortBy));
			
		List<Review> reviews = new ArrayList<>();
		for (ReviewDocument doc : docs) {
			reviews.add(ReviewUtils.documentToReview(doc));			
		}
		return reviews;
	}


	/** Aggregation demo */
	@Override
	public Optional<Double> getBookRating(String bookId) {
		
		GroupOperation group = group("bookId")
											.avg("rating").as("bookRating");
		 
		MatchOperation match = match(new Criteria("bookId").is(new ObjectId(bookId)));
		
		// static method, not constructor
		Aggregation aggregation = newAggregation(match, group);
		AggregationResults<BookRating> result = mongoOperations.aggregate(aggregation, "reviews", BookRating.class);
			
		if (result.getMappedResults().size() > 0) {
			return Optional.of(result.getMappedResults().get(0).getBookRating());
		} else {
			return Optional.empty();
		}
	}
	

	@Override
	public boolean hasVoted(String reviewId, String userId) {
		Optional<ReviewDocument> review = reviewRepository.findById(reviewId);
		if (review.isPresent()) {
			Set<ObjectId> voterIds = review.get().getVoterIds();
			return voterIds.contains(new ObjectId(userId));
		} else {
			throw new NoReviewFoundException();
		}
	}


	@Override
	public void voteHelpful(String reviewId, String userId, boolean helpful) {
	
		Query query = new Query();
		Update update = new Update();
		
		query.addCriteria(Criteria.where("id").is(reviewId));
		update.inc("helpfulVotes", helpful ? 1 : 0);
		update.addToSet("voterIds", new ObjectId(userId));
					
		mongoOperations.findAndModify(query, update, 
						new FindAndModifyOptions().returnNew(true), 
						Review.class);
	}


	@Override
	public Review getReviewById(String reviewId) {
		 
		Optional<ReviewDocument> review = reviewRepository.findById(reviewId);
		
		if (review.isPresent()) {
			return ReviewUtils.documentToReview(review.get());
		} else {
			throw new ReviewNotFoundException();
		}
	}


	@Override
	public List<Review> getReviewsByUserId(String userId) {

		List<ReviewDocument> docs = reviewRepository.findByUserId(new ObjectId(userId));
		List<Review> reviews = new ArrayList<>();
		for (ReviewDocument doc : docs) {
			reviews.add(ReviewUtils.documentToReview(doc));
		}	
		
		return reviews;
	}
}