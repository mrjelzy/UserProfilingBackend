package com.hai913i.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hai913i.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
