package ru.artemev.audit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.artemev.audit.model.AuditAction;
import ru.artemev.audit.repository.RedisRepository;
import ru.artemev.audit.service.AuditService;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @KafkaListener(topics = {"audit"})
    public void addAction(String message) throws JsonProcessingException {
        log.info("=== Started addAction ===");
        log.info("Received String in Dossier => " + message);
        AuditAction auditAction = objectMapper.readValue(message, AuditAction.class);
        redisRepository.add(auditAction);
        log.info("=== Finish addAction ===");
    }

    @Override
    public void deleteAction(UUID id) {
        redisRepository.delete(id);
    }

    @Override
    public AuditAction findAction(UUID id) {
        return redisRepository.findAuditAction(id);
    }

    @Override
    public Map<Object, Object> findAllAction() {
        log.info("=== Started findAllAction ===");
        return redisRepository.findAllAuditAction();
    }
}
