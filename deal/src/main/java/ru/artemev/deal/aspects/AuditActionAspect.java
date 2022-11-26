package ru.artemev.deal.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.artemev.deal.model.AuditAction;
import ru.artemev.deal.model.enums.AuditService;
import ru.artemev.deal.model.enums.AuditType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditActionAspect {
  private final KafkaTemplate<String, AuditAction> kafkaAuditTemplate;

  @Around("@annotation(AuditAction)")
  public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("=== Start send message to audit topic ===");

    UUID uuid = UUID.randomUUID();
    Object result = joinPoint.proceed();
    Object[] args = joinPoint.getArgs();
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().toString();
    AuditType auditType = AuditType.FAILURE;

    switch (methodName) {
      case "calculationPossibleLoans":
      case "signDocuments":
      case "sendDocuments":
      case "completionOfRegistration":
      case "selectOneOfOffers":
        auditType = AuditType.START;
        break;
      case "codeDocuments":
        auditType = AuditType.SUCCESS;
        break;
    }

    kafkaAuditTemplate.send(
        "audit",
        uuid.toString(),
        new AuditAction(
            uuid,
            auditType,
            AuditService.DEAL,
            LocalDateTime.now()
                + className
                + " in method - "
                + methodName
                + " received object - "
                + Arrays.toString(args)
                + " return object - "
                + result));

    log.info("=== Finish send message to audit topic ===");
    return result;
  }
}
