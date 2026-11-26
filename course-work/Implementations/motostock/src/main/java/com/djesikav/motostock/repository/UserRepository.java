package com.djesikav.motostock.repository;

import com.djesikav.motostock.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select u from User u
            where u.username = :actor or u.id = :target
            order by u.id
            """)
    List<User> findForAdminChange(
            @Param("actor") String actor,
            @Param("target") Long target
    );
}
