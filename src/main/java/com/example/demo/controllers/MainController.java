package com.example.demo.controllers;

import com.example.demo.models.Review;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repo.ReviewRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class   MainController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String home(Map<String,Object> model){
        model.put("name", "Ilya");
        return "home";
    }

    @GetMapping("/about")
    public String about(Map<String,Object>model){
        model.put("title", "About us");
        return "about";
    }

    @GetMapping("/reviews")
    public String reviews(Map<String,Object>model){
        Iterable<Review> reviews = reviewRepository.findAll();
        model.put("title", "Feedback");
        model.put("reviews",reviews);
        return "reviews";
    }

    @PostMapping("/reviews/add")
    public String reviewsAdd(@RequestParam String title, @RequestParam String text, Map<String,Object> model){
        Review review = new Review(title, text);
        reviewRepository.save(review);
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/{id}")
    public String reviewInfo(@PathVariable(value = "id") long review_id, Map<String,Object> model){
        Optional<Review> review = reviewRepository.findById(review_id);
        ArrayList<Review> result = new ArrayList<>();
        review.ifPresent(result::add);
        model.put("reviews",result);
        return "review-info";
    }

    @GetMapping("/reviews/{id}/update")
    public String reviewUpdate(@PathVariable(value = "id") long review_id, Map<String,Object> model){
        Optional<Review> review = reviewRepository.findById(review_id);
        ArrayList<Review> result = new ArrayList<>();
        review.ifPresent(result::add);
        model.put("reviews",result);
        return "review-update";
    }

    @PostMapping("/reviews/{id}/update")
    public String reviewsUpdateForm(@PathVariable(value = "id") long review_id, @RequestParam String title, @RequestParam String text, Map<String,Object> model){
        Review review = reviewRepository.findById(review_id).orElseThrow();
        review.setTitle(title);
        review.setText(text);
        reviewRepository.save(review);
        return "redirect:/reviews/" + review_id;
    }

    @GetMapping("/reg")
    public String reg(){
        return "reg";
    }

    @PostMapping("/reg")
    public String addUsers(User user, Map<String, Object> model){
        user.setEnabled(true);
        user.setRole(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}
