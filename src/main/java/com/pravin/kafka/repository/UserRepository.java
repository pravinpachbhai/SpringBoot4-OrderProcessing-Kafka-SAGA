package com.pravin.kafka.repository;

import com.pravin.kafka.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}