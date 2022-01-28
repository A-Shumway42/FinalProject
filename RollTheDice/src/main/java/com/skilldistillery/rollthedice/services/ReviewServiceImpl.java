package com.skilldistillery.rollthedice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilldistillery.rollthedice.entities.Review;
import com.skilldistillery.rollthedice.entities.User;
import com.skilldistillery.rollthedice.repositories.GameEventRepository;
import com.skilldistillery.rollthedice.repositories.ReviewRepository;
import com.skilldistillery.rollthedice.repositories.UserRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ReviewRepository reviewRepo;
	@Autowired 
	GameEventRepository gameEventRepo;
	
	@Override
	public List<Review> findAllReviews() {
		return reviewRepo.findAll();
	}
	
	@Override
	public Review findReviewById(int id) {
		Optional<Review> review = reviewRepo.findById(id);
		if (review.isPresent()) {
			return review.get();
		}
		return null;
	}
	
	@Override
	public Review createReview(Review review, String username, int gId) {
		review.setGameEvent(gameEventRepo.queryById(gId));
		User user = userRepo.findByUsername(username);
		review.setUser(user);
		return reviewRepo.saveAndFlush(review);
	}
	
	@Override
	public Review updateReview(Review review, int id, String username, int gId) {
		review.setId(id);
		User loggedInUser = userRepo.findByUsername(username);
		review.setUser(loggedInUser);
		Optional<Review> updatedReview = reviewRepo.findById(id);
		if (updatedReview.isPresent() && ((loggedInUser.getId() == review.getUser().getId()) || loggedInUser.getRole().equals("ROLE_ADMIN")) && gId == review.getGameEvent().getId()) {
			return reviewRepo.saveAndFlush(review);
		}
		return null;
	}
	
	@Override
	public boolean deleteReview(int id, String username, int gId) {
		boolean deleted = false;
		Optional<Review> r = reviewRepo.findById(id);
		Review review = r.get();
		User loggedInUser = userRepo.findByUsername(username);
		if (r.isPresent() && ((loggedInUser.getId() == review.getUser().getId()) || loggedInUser.getRole().equals("ROLE_ADMIN")) && review.getGameEvent().getId() == gId) {
			reviewRepo.delete(review);
			deleted = true;
		}
		return deleted;
	}
	
}
