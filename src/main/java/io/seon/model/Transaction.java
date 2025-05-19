package io.seon.model;

import java.time.Instant;
import java.util.Map;


/**
 * Represents a Customer Transaction
 */
public record Transaction(
  String id,
  String userId,
  String email,
  double amount,
  String country,
  Instant timestamp,
  Map<String, String> metadata
) {}
