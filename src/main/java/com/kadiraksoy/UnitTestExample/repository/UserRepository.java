package com.kadiraksoy.UnitTestExample.repository;

import com.kadiraksoy.UnitTestExample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
