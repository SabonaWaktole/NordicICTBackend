package com.nordic.backend.company.features.subscriber.controller;


import com.nordic.backend.company.features.subscriber.model.SubscriberModel;
import com.nordic.backend.company.features.subscriber.service.SubscriberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/subscriber")
public class SubscriberController {
    private final SubscriberService subscriberService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllSubscribers() {
        return subscriberService.getAllSubscribers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubscriberById(@PathVariable Long id) {
        return subscriberService.getSubscriberById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSubscriberById(@PathVariable Long id, @RequestBody SubscriberModel subscriberModel) {
        return subscriberService.updateSubscriber(subscriberModel,id);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteSubscriberById(@PathVariable Long id) {
        return subscriberService.deleteSubscriber(id);
    }

    @PostMapping("/new")
    public ResponseEntity<?> createSubscriber(@RequestBody SubscriberModel subscriberModel) {
        return subscriberService.saveSubscriber(subscriberModel);
    }
}
