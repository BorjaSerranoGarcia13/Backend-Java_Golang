package com.ecommerce.service;

import com.ecommerce.exception.*;
import com.ecommerce.model.Order;
import com.ecommerce.repository.IOrderRepository;
import com.ecommerce.utils.ReferenceGeneratorUtils;
import com.ecommerce.utils.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ecommerce.constants.messages.OrderErrorMessages.*;

@Service
public class OrderServiceImplementation implements IOrderService {
    private final IOrderRepository iOrderRepository;

    public OrderServiceImplementation(IOrderRepository iOrderRepository) {
        this.iOrderRepository = iOrderRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order save(Order order) {
        List<String> validationOrderErrors = Validator.validateOrder(order);
        if (!validationOrderErrors.isEmpty()) {
            throw new OrderException(String.join(", ", validationOrderErrors));
        }

        return iOrderRepository.save(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Order order) {
        List<String> validationOrderErrors = Validator.validateOrder(order);
        if (!validationOrderErrors.isEmpty()) {
            throw new OrderException(String.join(", ", validationOrderErrors));
        }

        iOrderRepository.save(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderException(ERROR_ORDER_INVALID_ID);
        }

        iOrderRepository.deleteById(id);
    }

    @Override
    public Order findByReference(String reference) {
        if (reference == null || !reference.startsWith(ReferenceGeneratorUtils.ORDER_DETAIL_PREFIX)) {
            throw new OrderException(ERROR_ORDER_INVALID_REFERENCE_PREFIX);
        }

        Optional<Order> optionalOrder = iOrderRepository.findByReference(reference);
        return optionalOrder.orElseThrow(() -> new OrderException(ERROR_ORDER_NOT_FOUND));
    }

    @Override
    public Order findById(Integer id) {
        Optional<Order> optionalOrder = iOrderRepository.findById(id);
        return optionalOrder.orElseThrow(() -> new OrderException(ERROR_ORDER_NOT_FOUND));
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = iOrderRepository.findAll();
        if (orders.isEmpty()) {
            throw new OrderException(ERROR_ORDER_NOT_FOUND);
        }
        return orders;
    }

}
