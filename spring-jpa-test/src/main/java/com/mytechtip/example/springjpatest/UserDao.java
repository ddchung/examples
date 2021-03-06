package com.mytechtip.example.springjpatest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository()
public class UserDao extends AbstractUserDao {
	
	@PersistenceContext
	EntityManager em;
	
	@Override
	@Transactional
	public User save(User u) {
		// some business logic
		if (u.getName()==null) {
			throw new IllegalArgumentException("Name can not be null");
		}
		if (u.getDob()==null) {
			throw new IllegalArgumentException("Dob can not be null");
		}
		if ((System.currentTimeMillis()-u.getDob().getTime())< 10 * ONE_YEAR) {
			throw new IllegalArgumentException("Must be 10+ year older");
		}
		return em.merge(u);
	}

	@Override
	@Transactional
	public List<User> getNameStartsWith(String namePrefix) {
		String query = "select u from User u where u.name like :prefix " +
				"order by u.name";
		Query q = em.createQuery(query);
		q.setParameter("prefix", namePrefix+ "%");
		return q.getResultList();
	}
}
