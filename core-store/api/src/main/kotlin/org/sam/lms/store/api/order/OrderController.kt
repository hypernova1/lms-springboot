package org.sam.lms.store.api.order

import org.sam.lms.store.api.order.request.CreateOrderRequest
import org.sam.lms.store.api.order.response.OrderResponse
import org.sam.lms.store.domain.order.application.OrderService
import org.sam.lms.store.domain.order.application.`in`.CreateOrderDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/store/api/v1/orders")
class OrderController(private val orderService: OrderService) {

    @PostMapping
    fun order(@RequestBody createOrderRequest: CreateOrderRequest): ResponseEntity<OrderResponse> {
        val result = this.orderService.order(CreateOrderDto(courseId = createOrderRequest.courseId))
        return ResponseEntity.ok(OrderResponse(orderNo = result.orderNo, paidPrice = result.paidPrice))
    }

}