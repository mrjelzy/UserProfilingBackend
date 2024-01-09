package com.hai913i.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hai913i.models.User;

public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findByEmailAndPassword(String email, String password);

	Optional<User> findByEmail(String email);
}
