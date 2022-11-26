package ru.artemev.audit.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.artemev.audit.model.AuditAction;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

  private static final String KEY = "AuditAction";

  private final RedisTemplate<UUID, AuditAction> redisTemplate;
  private HashOperations hashOperations;

  @PostConstruct
  private void init() {
    hashOperations = redisTemplate.opsForHash();
  }

  @Override
  public Map<Object, Object> findAllAuditAction() {
    return hashOperations.entries(KEY);
  }

  @Override
  public void add(AuditAction auditAction) {
    hashOperations.put(KEY, auditAction.getUuid(), auditAction);
  }

  @Override
  public void delete(UUID id) {
    hashOperations.delete(KEY, id);
  }

  @Override
  public AuditAction findAuditAction(UUID id) {
    return (AuditAction) hashOperations.get(KEY, id);
  }
}
