package com.ecommerce.service;

import com.ecommerce.constants.messages.OrderDetailsExceptionMessages;
import com.ecommerce.dto.OrderDetailsDto;
import com.ecommerce.exception.OrderDetailsException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.repository.IOrderDetailsRepository;
import com.ecommerce.utils.ReferenceGeneratorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ecommerce.constants.messages.OrderDetailsExceptionMessages.*;
import static com.ecommerce.constants.messages.UserExceptionMessages.ERROR_USER_NOT_LOGGED;
import static com.ecommerce.utils.ReferenceGeneratorUtil.generateUniqueReference;

@Service
public class OrderDetailServiceImplementation implements IOrderDetailsService {
    private final IOrderDetailsRepository iOrderDetailsRepository;
    private final IProductService iProductService;
    private final IOrderService iOrderService;
    private final List<OrderDetails> orderDetailsList = new ArrayList<>();
    private final Map<Integer, OrderDetails> orderDetailsMap = new HashMap<>();
    private final ModelMapper modelMapper;

    public OrderDetailServiceImplementation(IOrderDetailsRepository iOrderDetailsRepository,
                                            IProductService iProductService, IOrderService iOrderService,
                                            ModelMapper modelMapper) {
        this.iOrderDetailsRepository = iOrderDetailsRepository;
        this.iProductService = iProductService;
        this.iOrderService = iOrderService;
        this.modelMapper = modelMapper;
    }

    public List<OrderDetails> getCart() {
        return orderDetailsList;
    }

    @Override
    public List<OrderDetails> findAll() {
        return iOrderDetailsRepository.findAll();
    }

    @Override
    public Page<OrderDetails> findAll(Pageable pageable) {
        return iOrderDetailsRepository.findAll(pageable);
    }

    @Override
    public OrderDetails findById(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(OrderDetailsExceptionMessages.ERROR_ORDER_DETAILS_INVALID_ID);
        }

        Optional<OrderDetails> optionalOrderDetail = iOrderDetailsRepository.findById(id);
        if (optionalOrderDetail.isEmpty()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_NOT_FOUND);
        }

        return optionalOrderDetail.get();
    }

    @Override
    public Map.Entry<List<OrderDetails>, BigDecimal> getCartAndTotalPrice() {
        return new AbstractMap.SimpleEntry<>(getCart(), getTotalPrice());
    }

    @Override
    public Map.Entry<Product, Integer> getCurrentStockById(@PathVariable Integer id) {
        Product product = iProductService.findById(id);
        Integer quantity = product.getQuantity() -
                getCurrentOrderDetailsProductQuantity(product.getId());
        return new AbstractMap.SimpleEntry<>(product, quantity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new UserException(ERROR_USER_NOT_LOGGED);
        }
        if (orderDetailsList.isEmpty()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_EMPTY_CART);
        }

        orderDetailsList.forEach(orderDetail -> {
            Product product = iProductService.findById(orderDetail.getProduct().getId());

            product.setQuantity(product.getQuantity() - orderDetail.getQuantity());

            orderDetail.setProduct(product);
        });

        iOrderService.save(orderDetailsList, getTotalPrice(), userId);

        clearOrderDetailsList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }

        iOrderDetailsRepository.deleteById(id);
    }

    @Override
    public void addProductToCart(Integer productId, Integer productQuantity) {
        if (productId == null || productId <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }
        if (productQuantity == null || productQuantity <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_QUANTITY);
        }

        Product product = iProductService.findById(productId);
        iProductService.validateAndThrow(product);

        updateCart(product, productQuantity);
    }

    @Override
    public void updateCart(Product product, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_QUANTITY);
        }
        if (quantity > product.getQuantity()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_STOCK);
        }

        Integer totalProductQuantity = getTotalProductQuantity(product);
        if (totalProductQuantity + quantity > product.getQuantity()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_STOCK);
        }

        OrderDetails orderDetails = orderDetailsMap.get(product.getId());

        if (orderDetails != null) {
            orderDetails.setQuantity(orderDetails.getQuantity() + quantity);
            orderDetails.setTotal(product.getPrice().multiply(BigDecimal.valueOf(orderDetails.getQuantity())));
        } else {
            OrderDetails newOrderDetails = new OrderDetails();
            newOrderDetails.setProduct(product);
            newOrderDetails.setPrice(product.getPrice());
            newOrderDetails.setQuantity(quantity);
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(newOrderDetails.getQuantity()));
            newOrderDetails.setTotal(totalPrice);

            addOrderDetailList(newOrderDetails);
        }
    }

    @Override
    public void removeProductFromCart(Integer productId) {
        if (productId == null || productId <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }

        OrderDetails orderDetails = orderDetailsMap.get(productId);
        if (orderDetails != null) {
            orderDetailsList.remove(orderDetails);
            orderDetailsMap.remove(productId);
        }
    }

    @Override
    public BigDecimal getTotalPrice() {
        return getCart().stream()
                .map(OrderDetails::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDetailsDto convertOrderDetailsToDto(OrderDetails orderDetails) {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        modelMapper.map(orderDetails, orderDetailsDto);

        return orderDetailsDto;
    }

    @Override
    public List<OrderDetailsDto> convertOrderDetailsToDto(List<OrderDetails> ordersDetails) {
        return ordersDetails.stream()
                .map(this::convertOrderDetailsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> validateOrderDetails(OrderDetails orderDetails) {
        List<String> errors = new ArrayList<>();

        if (orderDetails == null) {
            errors.add(ERROR_ORDER_DETAILS_INVALID);
            return errors;
        }
        if (orderDetails.getProduct() == null) {
            errors.add(ERROR_ORDER_DETAILS_INVALID_PRODUCT);
        }
        if (orderDetails.getPrice() == null || orderDetails.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(ERROR_ORDER_DETAILS_INVALID_PRICE);
        }
        if (orderDetails.getQuantity() == null || orderDetails.getQuantity() < 0) {
            errors.add(ERROR_ORDER_DETAILS_INVALID_QUANTITY);
        }
        if (orderDetails.getTotal() == null || orderDetails.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(ERROR_ORDER_DETAILS_INVALID_TOTAL);
        }

        return errors;
    }

    @Override
    public List<String> validateOrdersDetails(List<OrderDetails> ordersDetails) {
        List<String> errors = new ArrayList<>();

        if (ordersDetails == null || ordersDetails.isEmpty()) {
            errors.add(ERROR_ORDER_DETAILS_INVALID);
            return errors;
        }

        for (OrderDetails orderDetails : ordersDetails) {
            List<String> orderDetailsErrors = validateOrderDetails(orderDetails);
            errors.addAll(orderDetailsErrors);
        }

        return errors;
    }

    private Integer getTotalProductQuantity(Product product) {
        OrderDetails orderDetails = orderDetailsMap.get(product.getId());
        return orderDetails != null ? orderDetails.getQuantity() : 0;
    }

    private Integer getCurrentOrderDetailsProductQuantity(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }
        OrderDetails orderDetails = orderDetailsMap.get(id);
        return orderDetails != null ? orderDetails.getQuantity() : 0;
    }

    private void addOrderDetailList(OrderDetails orderDetails) {
        List<String> validationOrderDetailsErrors = validateOrderDetails(orderDetails);
        if (!validationOrderDetailsErrors.isEmpty()) {
            throw new OrderDetailsException(String.join(", ", validationOrderDetailsErrors));
        }
        this.orderDetailsList.add(orderDetails);
        this.orderDetailsMap.put(orderDetails.getProduct().getId(), orderDetails);
    }

    private void clearOrderDetailsList() {
        orderDetailsList.clear();
        orderDetailsMap.clear();
    }

}
