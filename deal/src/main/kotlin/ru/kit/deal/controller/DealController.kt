package ru.kit.deal.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("deal")
class DealController {
    @GetMapping("test")
    fun test(): ResponseEntity<String> = ResponseEntity.ok("Success");
}