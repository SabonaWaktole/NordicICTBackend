package com.nordic.backend.company.features.authorization.model;

import java.util.Date;


import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder // lombok annotation to  generate constructor
@Data
@Component
public class JwtModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(unique = true)
  @NotNull
  private String token;

  @NotNull
  private Date issuedat;

  @NotNull
  private Date expiresat;

  @NotNull
  private String useremail;

}
