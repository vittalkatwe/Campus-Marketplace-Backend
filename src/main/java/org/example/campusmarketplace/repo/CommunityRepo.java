package org.example.campusmarketplace.repo;

// repo/CommunityRepo.java

import org.example.campusmarketplace.entities.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommunityRepo extends JpaRepository<Community, Long> {
    Optional<Community> findByName(String name);
}
