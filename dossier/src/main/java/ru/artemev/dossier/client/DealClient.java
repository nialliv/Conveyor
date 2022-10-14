package ru.artemev.dossier.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.artemev.dossier.model.enums.ApplicationStatus;

@FeignClient(name = "deal-client", url = "${deal.client.url}")
public interface DealClient {
  @PutMapping("/deal/admin/{applicationId}/status")
  void updateStatus(
      @PathVariable Long applicationId, @RequestBody ApplicationStatus applicationStatus);
}
