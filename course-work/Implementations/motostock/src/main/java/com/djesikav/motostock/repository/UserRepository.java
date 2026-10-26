package com.djesikav.motostock.repository;

import com.djesikav.motostock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
