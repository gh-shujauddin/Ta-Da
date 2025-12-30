package com.qadri.tada.repository;

import com.qadri.tada.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email=:username")
    UserDetails findByUsername(@Param("username") String username);

}