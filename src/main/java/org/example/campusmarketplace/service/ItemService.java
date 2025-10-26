package org.example.campusmarketplace.service;

import org.example.campusmarketplace.dto.ItemDto;
import org.example.campusmarketplace.entities.Item;
import org.example.campusmarketplace.model.AppUser;
import org.example.campusmarketplace.repo.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private UserService userService;

    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }

    public Item addItem(ItemDto item) {
        if(item==null) throw new IllegalArgumentException("item is null");

        Item newItem = new Item();
        newItem.setSeller(userService.getCurrentUserEmail());
        newItem.setCategory(item.getCategory());
        newItem.setDescription(item.getDescription());
        newItem.setPrice(item.getPrice());
        newItem.setImage(item.getImageUrl());
        newItem.setStatus(true);
        newItem.setTitle(item.getTitle());
        newItem.setCreatedAt(LocalDateTime.now());

        Item savedItem = itemRepo.save(newItem);
        return savedItem;
    }


    public Item getItemById(long id) {
        Item item=itemRepo.findItemById(id);
        item.setViews(item.getViews() + 1);
        return itemRepo.save(item);

    }


    public List<Item> getItemsByCategory(String category) {
        return itemRepo.findItemByCategory(category);
    }

    public List<Item> getUserMeItems(String email) {
        return itemRepo.findItemsBySeller(email);
    }

    public List<Item> getItemsByUserId(int id) {
        AppUser user=userService.getAppUserById(id);
        return itemRepo.findItemsBySeller(user.getEmail());
    }
}
