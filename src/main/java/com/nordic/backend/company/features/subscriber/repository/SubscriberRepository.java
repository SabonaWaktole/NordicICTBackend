package com.nordic.backend.company.features.subscriber.repository;

import com.nordic.backend.company.features.subscriber.model.SubscriberModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<SubscriberModel, Long> {
}
