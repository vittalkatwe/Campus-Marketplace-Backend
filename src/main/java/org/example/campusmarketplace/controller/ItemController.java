package org.example.campusmarketplace.controller;

import org.example.campusmarketplace.dto.ItemDto;
import org.example.campusmarketplace.entities.Item;
import org.example.campusmarketplace.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @PostMapping
    public Item addItem(@RequestBody ItemDto item) {
        return itemService.addItem(item);
    }

    @GetMapping("user/{id}")
    public Item getItemById(@PathVariable int id) {
        return itemService.getItemById(id);
    }

    @GetMapping("category/{category}")
    public List<Item> getItemsByCategory(@PathVariable String category) {
        return itemService.getItemsByCategory(category);
    }

    @GetMapping("user/me")
    public List<Item> getUserMeItems(@AuthenticationPrincipal String email) {
        return itemService.getUserMeItems(email);
    }



}
