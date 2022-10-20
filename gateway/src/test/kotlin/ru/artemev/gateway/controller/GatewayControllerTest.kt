package ru.artemev.gateway.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
internal class GatewayControllerTest(
    @MockBean private val gatewayController: GatewayController,
    private val objectMapper: ObjectMapper,
) {

    @Test
    @DisplayName("Testing getLoanOfferDtoList")
    fun getLoanOfferDtoList() {

    }

    @Test
    @DisplayName("Testing selectOneOfOffers")
    fun selectOneOfOffers() {
    }

    @Test
    @DisplayName("Testing completionOfRegistration")
    fun completionOfRegistration() {
    }

    @Test
    @DisplayName("Testing sendDocuments")
    fun sendDocuments() {
    }

    @Test
    @DisplayName("Testing signDocuments")
    fun signDocuments() {
    }

    @Test
    @DisplayName("Testing codeDocuments")
    fun codeDocuments() {
    }
}