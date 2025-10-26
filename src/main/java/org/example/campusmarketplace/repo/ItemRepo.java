package org.example.campusmarketplace.repo;

import org.example.campusmarketplace.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {



    @Query("select i from Item i where i.status=true and i.id=?1")
    Item findItemById(Long id);

    @Query("select i from Item i where i.category=?1 and i.status=true")
    List<Item> findItemByCategory(String category);

    @Query("select i from Item i where i.seller=?1 and i.status=true")
    List<Item> findItemsBySeller(String seller);
}
