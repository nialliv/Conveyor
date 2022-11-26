package ru.artemev.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.artemev.audit.model.AuditAction;

import java.util.Map;
import java.util.UUID;

public interface AuditService {
    void addAction(String message) throws JsonProcessingException;
    void deleteAction(UUID id);
    AuditAction findAction(UUID id);
    Map<Object, Object> findAllAction();
}
