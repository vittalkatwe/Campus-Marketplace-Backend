package org.example.campusmarketplace.repo;

import org.example.campusmarketplace.entities.Community;
import org.example.campusmarketplace.entities.Message;
import org.example.campusmarketplace.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    // Find messages within a community, ordered by timestamp
    List<Message> findByCommunityOrderByTimestampAsc(Community community);

    // Find direct messages between two users, ordered by timestamp
    List<Message> findBySenderAndReceiverOrderByTimestampAsc(AppUser sender, AppUser receiver);
    List<Message> findByReceiverAndSenderOrderByTimestampAsc(AppUser receiver, AppUser sender);
}
