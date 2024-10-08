package org.sam.lms.store.domain.payment.application

import org.sam.lms.client.course.CourseClient
import org.sam.lms.common.exception.BadRequestException
import org.sam.lms.common.exception.ErrorCode
import org.sam.lms.common.exception.NotFoundException
import org.sam.lms.store.domain.order.application.OrderService
import org.sam.lms.store.domain.payment.domain.PaymentGateway
import org.sam.lms.store.domain.payment.application.`in`.CreatePaymentDto
import org.sam.lms.store.domain.payment.application.`in`.PaymentFailureDto
import org.sam.lms.store.domain.payment.application.`in`.PaymentSuccessDto
import org.sam.lms.store.domain.payment.application.out.PaymentResultDto
import org.sam.lms.store.domain.payment.domain.Payment
import org.sam.lms.store.domain.payment.domain.PaymentRepository
import org.sam.lms.store.domain.payment.domain.PaymentStatus
import org.sam.lms.store.domain.provider.Provider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val paymentGateway: PaymentGateway,
    private val orderService: OrderService,
    private val courseClient: CourseClient,
    private val paymentKafkaProducer: PaymentKafkaProducer
) {

    @Transactional
    fun create(createPaymentDto: CreatePaymentDto, provider: Provider): Payment {
        val order = orderService.findOne(createPaymentDto.orderNo) ?: throw NotFoundException(ErrorCode.ORDER_NOT_FOUND)
        if (order.isPaymentEligible) {
            throw BadRequestException(ErrorCode.PAYMENT_TIME_OUT)
        }

        val payment = Payment.from(createPaymentDto, provider)
        paymentRepository.save(payment)

        courseClient.enrollCourse(order.orderLines[0].courseId, provider.id)

        return payment
    }

    @Transactional
    fun complete(paymentSuccessDto: PaymentSuccessDto): PaymentResultDto {
        try {
            val payment = this.paymentRepository.findByOrderNo(paymentSuccessDto.orderNo)
                ?: return PaymentResultDto(
                    status = PaymentStatus.PAID,
                    message = "결제 정보가 존재하지 않습니다."
                )

            payment.complete(paymentSuccessDto)
            paymentGateway.confirm(paymentSuccessDto.paymentKey)

            return PaymentResultDto(
                status = PaymentStatus.PAID,
                message = "결제가 성공했습니다."
            )
        } catch (e: RuntimeException) {
            this.paymentKafkaProducer.paymentFailure(paymentSuccessDto.orderNo)
            throw RuntimeException(e)
        }

    }

    @Transactional
    fun failure(paymentFailureDto: PaymentFailureDto): PaymentResultDto {
        try {
            val payment = this.paymentRepository.findByOrderNo(paymentFailureDto.paymentKey)
                ?: return PaymentResultDto(
                    status = PaymentStatus.PAID,
                    message = "결제 정보가 존재하지 않습니다."
                )

            payment.failure(paymentFailureDto)

            return PaymentResultDto(
                status = PaymentStatus.FAILED,
                message = "결제가 실패했습니다."
            )
        } catch (e: RuntimeException) {
            this.paymentKafkaProducer.paymentFailure(paymentFailureDto.orderNo)
            throw RuntimeException(e)
        }

    }
}