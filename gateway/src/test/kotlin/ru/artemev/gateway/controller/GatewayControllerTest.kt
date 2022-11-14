package ru.artemev.gateway.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.artemev.gateway.client.ApplicationClient
import ru.artemev.gateway.client.DealClient
import ru.artemev.gateway.dto.FinishRegistrationRequestDTO
import ru.artemev.gateway.dto.LoanApplicationRequestDTO
import ru.artemev.gateway.dto.LoanOfferDTO
import java.io.File

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class, MockitoExtension::class)
internal class GatewayControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var applicationClient: ApplicationClient

    @MockBean
    lateinit var dealClient: DealClient


    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

    private val loanApplicationRequestDTO: LoanApplicationRequestDTO =
        objectMapper.readValue(File("src/test/resources/json/LoanApplicationRequestDTO.json"))

    private val finishRegistrationRequestDTO: FinishRegistrationRequestDTO =
        objectMapper.readValue(File("src/test/resources/json/FinishRegistrationRequestDTO.json"))

    private val loanOfferDTOList: List<LoanOfferDTO> =
        objectMapper.readValue(File("src/test/resources/json/LoanOfferDTOList.json"))

    private val applicationId: Long = loanOfferDTOList[0].applicationId!!

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
            }
    }

    @Test
    @DisplayName("Testing selectOneOfOffers")
    fun selectOneOfOffers() {
        mockMvc.post("/application/apply") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loanOfferDTOList[0])
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @DisplayName("Testing completionOfRegistration")
    fun completionOfRegistration() {
        mockMvc.post("/application/registration/${applicationId}") {
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
        mockMvc.post("/document/${applicationId}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @DisplayName("Testing signDocuments")
    fun signDocuments() {
        mockMvc.post("/document/${applicationId}/sign")
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @DisplayName("Testing codeDocuments")
    fun codeDocuments() {
        mockMvc.post("/document/${applicationId}/sign/code") {
            contentType = MediaType.APPLICATION_JSON
            content = 1234
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { }
            }
    }
}