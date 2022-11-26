package ru.artemev.audit.repository;

import ru.artemev.audit.model.AuditAction;

import java.util.Map;
import java.util.UUID;

public interface RedisRepository {

  /** Return all AuditAction */
  Map<Object, Object> findAllAuditAction();

  /** Add key-value pair to Redis. */
  void add(AuditAction auditAction);

  /** Delete a key-value pair in Redis. */
  void delete(UUID id);

  /** find a AuditAction */
  AuditAction findAuditAction(UUID id);
}
