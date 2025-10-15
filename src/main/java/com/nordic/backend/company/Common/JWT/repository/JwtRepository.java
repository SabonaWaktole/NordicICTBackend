package com.nordic.backend.company.Common.JWT.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nordic.backend.company.Common.JWT.model.JwtModel;

public interface JwtRepository extends JpaRepository<JwtModel, Long>{
  Optional<JwtModel> findByToken(String token);

}
