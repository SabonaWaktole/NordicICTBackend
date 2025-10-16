package com.nordic.backend.company.features.subscriber.service;

import com.nordic.backend.company.features.subscriber.repository.SubscriberRepository;
import com.nordic.backend.company.features.subscriber.model.SubscriberModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SubscriberService {
  private final SubscriberRepository subscriberRepository;

  public ResponseEntity<?> saveSubscriber(SubscriberModel subscriberModel) {
    return ResponseEntity.ok(subscriberRepository.save(subscriberModel));
  }

  public ResponseEntity<?> getAllSubscribers() {
    return ResponseEntity.ok(subscriberRepository.findAll());
  }

  public ResponseEntity<?> getSubscriberById(Long id) {
    return ResponseEntity.ok(subscriberRepository.findById(id).orElse(null));
  }

  public ResponseEntity<?> updateSubscriber(SubscriberModel subscriberModel, Long id) {
    try {
      SubscriberModel subscriber = subscriberRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Subscriber not found"));
      subscriber.setFirstname(subscriberModel.getFirstname());
      subscriber.setLastname(subscriberModel.getLastname());
      subscriber.setEmail(subscriberModel.getEmail());
      return ResponseEntity.ok(subscriberRepository.save(subscriber));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  public ResponseEntity<?> deleteSubscriber(Long id) {
    subscriberRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
