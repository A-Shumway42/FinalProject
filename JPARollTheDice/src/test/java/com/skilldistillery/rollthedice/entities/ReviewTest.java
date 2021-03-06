package com.skilldistillery.rollthedice.entities;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReviewTest {

	private static EntityManagerFactory emf;
	private EntityManager em;
	private Review review;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("JPARollTheDice");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		emf.close();
	}

	@BeforeEach
	void setUp() throws Exception {
		em = emf.createEntityManager();
		review = em.find(Review.class, 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		em.close();
		review = null;
	}

	@Test
	void test_Review_entity_mapping() {
		assertNotNull(review);
		assertEquals("Omg this event was so fun! Great host!", review.getMessage());

	}
	@Test
	void test_Review_GameEvent_relational_mapping() {
		assertNotNull(review);
		assertNotNull(review.getGameEvent());
		assertEquals("Best Game Night", review.getGameEvent().getTitle());
	}
	@Test
	void test_Review_User_relational_mapping() {
		assertNotNull(review);
		assertNotNull(review.getUser());
		assertEquals("admin@admin.com", review.getUser().getEmail());
	}
}
