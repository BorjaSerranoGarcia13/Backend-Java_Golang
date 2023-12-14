package com.ecommerce.service;

import com.ecommerce.constants.messages.OrderDetailsErrorMessages;
import com.ecommerce.exception.*;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.IOrderDetailRepository;
import com.ecommerce.utils.UserSessionUtils;
import com.ecommerce.utils.ReferenceGeneratorUtils;
import com.ecommerce.utils.Validator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

import static com.ecommerce.constants.messages.OrderDetailsErrorMessages.*;
import static com.ecommerce.utils.ReferenceGeneratorUtils.generateUniqueReference;

@Service
public class OrderDetailServiceImplementation implements IOrderDetailService {
    private final IOrderDetailRepository iOrderDetailRepository;
    private final IProductService iProductService;
    private final IOrderService iOrderService;
    private final UserSessionUtils userSessionUtils;

    private List<OrderDetails> orderDetailsList = new ArrayList<>();
    private final Map<String, OrderDetails> orderDetailsMap = new HashMap<>();

    public OrderDetailServiceImplementation(IOrderDetailRepository iOrderDetailRepository, IProductService iProductService, IOrderService iOrderService, UserSessionUtils userSessionUtils) {
        this.iOrderDetailRepository = iOrderDetailRepository;
        this.iProductService = iProductService;
        this.iOrderService = iOrderService;
        this.userSessionUtils = userSessionUtils;
    }

    public void setOrderDetailsList(List<OrderDetails> orderDetailsList) {
        if (orderDetailsList == null) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_LIST);
        }
        this.orderDetailsList = orderDetailsList;
    }

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }

    @Override
    public Integer getCurrentOrderDetailsProductQuantity(String reference) {
        if (reference == null) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_PRODUCT_REFERENCE);
        }
        OrderDetails orderDetails = orderDetailsMap.get(reference);
        return orderDetails != null ? orderDetails.getQuantity() : 0;
    }

    public void addOrderDetailList(OrderDetails orderDetails) {
       List<String> validationProductErrors = Validator.validateOrderDetails(orderDetails);
        if (!validationProductErrors.isEmpty()) {
            throw new OrderDetailsException(String.join(", ", validationProductErrors));
        }
        this.orderDetailsList.add(orderDetails);
        this.orderDetailsMap.put(orderDetails.getProduct().getReference(), orderDetails);
    }

    @Override
    public List<OrderDetails> findByReference(String reference) {
        if (reference == null || !reference.startsWith(ReferenceGeneratorUtils.ORDER_DETAIL_PREFIX)) {
            throw new OrderDetailsException(OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_REFERENCE);
        }

        List<OrderDetails> orderDetails = iOrderDetailRepository.findByReference(reference);

        if (orderDetails.isEmpty()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_NOT_FOUND);
        }

        return orderDetails;
    }

    @Override
    public OrderDetails findById(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(OrderDetailsErrorMessages.ERROR_ORDER_DETAILS_INVALID_ID);
        }

        Optional<OrderDetails> optionalOrderDetail = iOrderDetailRepository.findById(id);
        if (optionalOrderDetail.isEmpty()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_NOT_FOUND);
        }

        return optionalOrderDetail.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(HttpSession session) {
        if (orderDetailsList == null || orderDetailsList.isEmpty()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_LIST);
        }

        String reference;
        reference = generateUniqueReference(ReferenceGeneratorUtils.ORDER_DETAIL_PREFIX);

        Optional<User> optionalUser = userSessionUtils.checkVerifiedUserFromSession(session);
        if (optionalUser.isEmpty()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_USER);
        }

        Order order = createOrder(reference, optionalUser.get(), orderDetailsList);

        order = iOrderService.save(order);

        List<OrderDetails> orderDetailsToSave = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();
        Order finalOrder = order;

        orderDetailsList.forEach(orderDetail -> {
            orderDetail.setReference(reference);
            orderDetail.setOrder(finalOrder);
            orderDetailsToSave.add(orderDetail);

            orderDetail.getProduct().setQuantity(orderDetail.getProduct().getQuantity() - orderDetail.getQuantity());
            productsToUpdate.add(orderDetail.getProduct());
        });

        iOrderDetailRepository.saveAll(orderDetailsToSave);
        iProductService.saveAllProducts(productsToUpdate);

        clearOrderDetailsList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id, HttpSession session) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }

        Optional<User> optionalUser = userSessionUtils.checkVerifiedAdminFromSession(session);
        if (optionalUser.isEmpty()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ADMIN);
        }

        iOrderDetailRepository.deleteById(id);
    }

    @Override
    public void addProductToOrderDetailList(String reference, Integer quantity) {
        if (reference == null || !reference.startsWith(ReferenceGeneratorUtils.PRODUCT_PREFIX)) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_PRODUCT_REFERENCE);
        }
        if (quantity == null || quantity <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_QUANTITY);
        }

        Product product = iProductService.findByReference(reference);

        updateOrderDetailList(product, quantity);
    }

    @Override
    public void updateOrderDetailList(Product product, Integer quantity) {
        List<String> validationProductErrors = Validator.validateProduct(product);
        if (!validationProductErrors.isEmpty()) {
            throw new OrderDetailsException(String.join(", ", validationProductErrors));
        }
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

        OrderDetails orderDetails = orderDetailsMap.get(product.getReference());

        if (orderDetails != null) {
            orderDetails.setQuantity(orderDetails.getQuantity() + quantity);
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(orderDetails.getQuantity()));
            orderDetails.setTotal(totalPrice);
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
    public void deleteProductInOrderDetailList(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }

        Iterator<OrderDetails> iterator = getOrderDetailsList().iterator();
        while (iterator.hasNext()) {
            OrderDetails orderDetails = iterator.next();
            if (orderDetails.getProduct().getId().equals(id)) {
                iterator.remove();
                orderDetailsMap.remove(orderDetails.getProduct().getReference());
                break;
            }
        }
    }

    @Override
    public BigDecimal getTotalPrice() {
        return getOrderDetailsList().stream()
                .map(OrderDetails::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order createOrder(String reference, User user, List<OrderDetails> orderDetails) {
        Order order = new Order();
        order.setUser(user);
        order.setReference(reference);
        order.setCreationDate(new Date(System.currentTimeMillis()));
        order.setOrderDetails(orderDetails);
        order.setTotal(getTotalPrice());
        return order;
    }

    private Integer getTotalProductQuantity(Product product) {
        OrderDetails orderDetails = orderDetailsMap.get(product.getReference());
        return orderDetails != null ? orderDetails.getQuantity() : 0;
    }

    private void clearOrderDetailsList() {
        orderDetailsList.clear();
        orderDetailsMap.clear();
    }

}
