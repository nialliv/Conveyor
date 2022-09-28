package ru.kit.deal.service.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.kit.deal.ConveyorClient
import ru.kit.deal.dto.*
import ru.kit.deal.entity.Application
import ru.kit.deal.entity.Client
import ru.kit.deal.repository.ApplicationRepository
import ru.kit.deal.repository.ClientRepository
import ru.kit.deal.repository.CreditRepository
import ru.kit.deal.service.DealService
import java.time.LocalDate

@Service
class DealServiceImpl(
    private val creditRepository: CreditRepository,
    private val clientRepository: ClientRepository,
    private val applicationRepository: ApplicationRepository,
    private val conveyorClient: ConveyorClient,
) : DealService {
    override fun calculationPossibleLoans(loanApplicationRequestDTO: LoanApplicationRequestDTO): List<LoanOfferDTO> {
        /*
        По API приходит LoanApplicationRequestDTO
        На основе LoanApplicationRequestDTO создаётся сущность Client и сохраняется в БД.
        Создаётся Application со связью на только что созданный Client и сохраняется в БД.
        Отправляется POST запрос на /conveyor/offers МС conveyor через FeignClient. Каждому элементу из списка List<LoanOfferDTO> присваивается id созданной заявки (Application)
        Ответ на API - список из 4х LoanOfferDTO от "худшего" к "лучшему".
         */
        val clientEntity = clientRepository.save(loanApplicationRequestDTO.toClientEntity())
        applicationRepository.save(loanApplicationRequestDTO.toApplicationEntity(clientEntity))
        return conveyorClient.getOffers(loanApplicationRequestDTO)
    }

    override fun selectOneOfOffers(loanOfferDTO: LoanOfferDTO) {
        /*
        По API приходит LoanOfferDTO
        Достаётся из БД заявка(Application) по applicationId из LoanOfferDTO.
        В заявке обновляется статус, история статусов(List<ApplicationStatusHistoryDTO>), принятое предложение LoanOfferDTO устанавливается в поле appliedOffer.
        Заявка сохраняется.
         */
        val applicationEntity = applicationRepository.findByIdOrNull(
            loanOfferDTO.applicationId ?: throw RuntimeException("Received null instead of id")
        )
        var applicationStatus = applicationEntity?.statusHistory

    }

    override fun completionOfRegistration(id: Long, finishRegistrationRequestDTO: FinishRegistrationRequestDTO) {
        TODO("Not yet implemented")
    }

    private fun LoanApplicationRequestDTO.toClientEntity(): Client {
        return Client(
            id = 0,
            firstName = this.firstName,
            lastName = this.lastName,
            middleName = this.middleName,
            birthday = this.birthday,
            email = this.email,
            passport = Passport(this.passportSeries, this.passportNumber)
        )
    }

    private fun LoanApplicationRequestDTO.toApplicationEntity(clientEntity: Client): Application {
        return Application(
            id = 0,
            client = clientEntity,
            credit = null,
            applicationStatus = null,
            creationDate = LocalDate.now()
        )
    }
}