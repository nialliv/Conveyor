package ru.artemev.gateway.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.artemev.gateway.dto.FinishRegistrationRequestDTO
import ru.artemev.gateway.dto.LoanApplicationRequestDTO
import ru.artemev.gateway.dto.LoanOfferDTO
import java.io.File

@SpringBootTest
@AutoConfigureMockMvc
internal class GatewayControllerTest(
    @Autowired
    val gatewayController: GatewayController,
    @Autowired
    val mockMvc: MockMvc,
) {
    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

    private val loanApplicationRequestDTO: LoanApplicationRequestDTO =
        objectMapper.readValue(File("src/test/resources/json/LoanApplicationRequestDTO.json"))

    private val finishRegistrationRequestDTO: FinishRegistrationRequestDTO =
        objectMapper.readValue(File("src/test/resources/json/FinishRegistrationRequestDTO.json"))

    @Test
    @DisplayName("Testing getLoanOfferDtoList")
    fun getLoanOfferDtoList() {
        mockMvc.post("/application") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loanApplicationRequestDTO)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }
    }

    @Test
    @DisplayName("Testing selectOneOfOffers")
    fun selectOneOfOffers() {
        val loanOfferDTO: LoanOfferDTO = gatewayController.getLoanOfferDtoList(loanApplicationRequestDTO)[0]

        mockMvc.post("/application/apply") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loanOfferDTO)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @DisplayName("Testing completionOfRegistration")
    fun completionOfRegistration() {
        mockMvc.post("/application/registration/${preparedForGettingApplicationID()}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(finishRegistrationRequestDTO)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }

    }

    @Test
    @DisplayName("Testing sendDocuments")
    fun sendDocuments() {
        mockMvc.post("/document/${preparedForGettingApplicationID()}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @DisplayName("Testing signDocuments")
    fun signDocuments() {
        // post + document/{applicationId}/sign
        // @PathVariable applicationId: Long
        mockMvc.post("/document/${preparedForGettingApplicationID()}/sign")
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @DisplayName("Testing codeDocuments")
    fun codeDocuments() {
        mockMvc.post("/document/${preparedForGettingApplicationID()}/sign/code") {
            contentType = MediaType.APPLICATION_JSON
            content = 1234
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { }
            }
    }


    private fun preparedForGettingApplicationID(): Long {
        val loanOfferDto = gatewayController.getLoanOfferDtoList(loanApplicationRequestDTO)[0]
        val applicationId = loanOfferDto.applicationId
        gatewayController.selectOneOfOffers(loanOfferDto)
        gatewayController.completionOfRegistration(applicationId!!, finishRegistrationRequestDTO)
        return applicationId
    }
}