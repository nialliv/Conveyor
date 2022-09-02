package ru.artemev.conveyor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.artemev.conveyor.dto.CreditDTO;
import ru.artemev.conveyor.dto.LoanApplicationRequestDTO;
import ru.artemev.conveyor.dto.LoanOfferDTO;
import ru.artemev.conveyor.dto.ScoringDataDTO;
import ru.artemev.conveyor.service.IConveyorService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ConveyorController {

    @Autowired
    private IConveyorService conveyorService;

    @PostMapping("/conveyor/offers")
    public ResponseEntity<List<LoanOfferDTO>> getOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return ResponseEntity.ok(conveyorService.getOffers(loanApplicationRequestDTO));
    }

    @PostMapping("/conveyor/calculation")
    public ResponseEntity<CreditDTO> getCreditDto(@RequestBody ScoringDataDTO scoringDataDTO) {
        return ResponseEntity.ok(conveyorService.getCreditDto(scoringDataDTO));
    }

}
