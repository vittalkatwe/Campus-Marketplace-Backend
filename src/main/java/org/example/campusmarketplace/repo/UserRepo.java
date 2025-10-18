package org.example.campusmarketplace.repo;

import org.example.campusmarketplace.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    @Query("select u from AppUser u where u.email like %?1%")
    List<AppUser> findByEmailContainingIgnoreCase(String emailPartial);
}
