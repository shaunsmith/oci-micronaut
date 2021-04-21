/**
 * * Copyright © 2020, Oracle and/or its affiliates. All rights reserved.
 * * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 **/
package mushop.orders.controllers;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.exceptions.HttpStatusException;
import mushop.orders.entities.CustomerOrder;
import mushop.orders.resources.NewOrderResource;
import mushop.orders.services.OrdersService;

import java.util.List;

/**
 * Orders controller.
 */
@Controller("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Status(HttpStatus.CREATED)
    @Post
    public CustomerOrder newOrder(@Body NewOrderResource newOrderResource) {
        if (newOrderResource.address == null || newOrderResource.customer == null || newOrderResource.card == null || newOrderResource.items == null) {
            throw new InvalidOrderException("Invalid order request. Order requires customer, address, card and items.");
        }
        return ordersService.placeOrder(newOrderResource);
    }

    @Get
    public CustomerOrder getOrder(Long orderId) {
        return ordersService.getById(orderId);
    }

    @Get
    public List<CustomerOrder> listOrders() {
        return ordersService.listOrders();
    }

    public static class PaymentDeclinedException extends HttpStatusException {
        public PaymentDeclinedException(String s) {
            super(HttpStatus.NOT_ACCEPTABLE, s);
        }
    }

    public static class InvalidOrderException extends HttpStatusException {
        public InvalidOrderException(String s) {
            super(HttpStatus.NOT_ACCEPTABLE, s);
        }
    }

    public static class OrderFailedException extends HttpStatusException {
        public OrderFailedException(String s) {
            super(HttpStatus.SERVICE_UNAVAILABLE, s);
        }
    }
}
