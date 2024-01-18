package com.ecommerce.service;

import com.ecommerce.dto.OrderDetailsDto;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.exception.OrderException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.repository.IOrderRepository;
import com.ecommerce.utils.ReferenceGeneratorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ecommerce.constants.messages.OrderExceptionMessages.*;
import static com.ecommerce.constants.messages.UserExceptionMessages.ERROR_NOT_ADMIN_USER_LOGGED_IN;
import static com.ecommerce.utils.ReferenceGeneratorUtil.generateUniqueReference;

@Service
public class OrderServiceImplementation implements IOrderService {
    private final IOrderRepository iOrderRepository;
    private final ModelMapper modelMapper;
    private final IUserService iUserService;

    public OrderServiceImplementation(IOrderRepository iOrderRepository, ModelMapper modelMapper,
                                      IUserService iUserService) {
        this.iOrderRepository = iOrderRepository;
        this.iUserService = iUserService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Order> findAll() {
        return iOrderRepository.findAll();
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return iOrderRepository.findAll(pageable);
    }

    @Override
    public Order findById(Integer id) {
        Optional<Order> optionalOrder = iOrderRepository.findById(id);
        return optionalOrder.orElseThrow(() -> new OrderException(ERROR_ORDER_NOT_FOUND));
    }

    @Override
    public Order findByReference(String reference) {
        if (reference == null || !reference.startsWith(ReferenceGeneratorUtil.ORDER_PREFIX)) {
            throw new OrderException(ERROR_ORDER_INVALID_REFERENCE_PREFIX);
        }

        Optional<Order> optionalOrder = iOrderRepository.findByReference(reference);
        return optionalOrder.orElseThrow(() -> new OrderException(ERROR_ORDER_NOT_FOUND));
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        return iOrderRepository.findAllByUserId(userId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order save(List<OrderDetails> ordersDetails, BigDecimal totalPrice, Integer userId) {
        Order order = new Order();
        order.setUserId(userId);
        String reference = generateUniqueReference(ReferenceGeneratorUtil.ORDER_PREFIX);
        order.setReference(reference);
        order.setCreationDate(new Date(System.currentTimeMillis()));
        order.setTotal(totalPrice);
        order.setOrderDetails(new ArrayList<>(ordersDetails));

        validateAndThrow(order);

        return iOrderRepository.save(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (!iUserService.isAdmin()) {
            throw new UserException(ERROR_NOT_ADMIN_USER_LOGGED_IN);
        }
        if (id == null || id <= 0) {
            throw new OrderException(ERROR_ORDER_INVALID_ID);
        }

        iOrderRepository.deleteById(id);
    }

    @Override
    public OrderDto convertOrderToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        modelMapper.map(order, orderDto);

        List<OrderDetailsDto> orderDetailsDtoList = order.getOrderDetails().stream()
                .map(orderDetails -> modelMapper.map(orderDetails, OrderDetailsDto.class))
                .collect(Collectors.toList());

        orderDto.setOrderDetails(orderDetailsDtoList);

        return orderDto;
    }

    @Override
    public List<OrderDto> convertOrderToDto(List<Order> orders) {
        return orders.stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> validateOrder(Order order) {
        List<String> errors = new ArrayList<>();

        if (order == null) {
            errors.add(ERROR_ORDER_INVALID);
            return errors;
        }
        if (order.getReference() == null || order.getReference().isEmpty()) {
            errors.add(ERROR_ORDER_INVALID_REFERENCE);
        }

        if (order.getCreationDate() == null) {
            errors.add(ERROR_ORDER_INVALID_CREATION_DATE);
        }

        if (order.getTotal() == null || order.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(ERROR_ORDER_INVALID_TOTAL);
        }

        if (order.getUserId() == null || order.getUserId() <= 0) {
            errors.add(ERROR_ORDER_INVALID_USER_ID);
        }

        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            errors.add(ERROR_ORDER_INVALID_ORDER_DETAILS);
        }

        return errors;
    }

    @Override
    public List<String> validateOrders(List<Order> orders) {
        List<String> errors = new ArrayList<>();

        if (orders == null || orders.isEmpty()) {
            errors.add(ERROR_ORDER_INVALID);
            return errors;
        }

        for (Order order : orders) {
            List<String> orderErrors = validateOrder(order);
            errors.addAll(orderErrors);
        }

        return errors;
    }

    private void validateAndThrow(Order order) {
        List<String> validationOrderErrors = validateOrder(order);
        if (!validationOrderErrors.isEmpty()) {
            throw new OrderException(String.join(", ", validationOrderErrors));
        }
    }

}
