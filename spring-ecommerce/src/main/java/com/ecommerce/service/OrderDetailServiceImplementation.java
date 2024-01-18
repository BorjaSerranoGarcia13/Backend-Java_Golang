package com.ecommerce.service;

import com.ecommerce.constants.messages.OrderDetailsExceptionMessages;
import com.ecommerce.dto.OrderDetailsDto;
import com.ecommerce.exception.AuthenticationException;
import com.ecommerce.exception.OrderDetailsException;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.repository.IOrderDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ecommerce.constants.messages.OrderDetailsExceptionMessages.*;
import static com.ecommerce.constants.messages.UserExceptionMessages.ERROR_NOT_ADMIN_USER_LOGGED_IN;

@Service
public class OrderDetailServiceImplementation implements IOrderDetailsService {
    private final IOrderDetailsRepository iOrderDetailsRepository;
    private final IProductService iProductService;
    private final IOrderService iOrderService;
    private final IUserService iUserService;
    private final List<Product> productList = new ArrayList<>();
    private final Map<Integer, Product> productMap = new HashMap<>();
    private final ModelMapper modelMapper;

    public OrderDetailServiceImplementation(IOrderDetailsRepository iOrderDetailsRepository, IProductService iProductService,
                                            IOrderService iOrderService, IUserService iUserService,
                                            ModelMapper modelMapper) {
        this.iOrderDetailsRepository = iOrderDetailsRepository;
        this.iProductService = iProductService;
        this.iOrderService = iOrderService;
        this.iUserService = iUserService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Product> getCart() {
        return productList;
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
    public Map.Entry<List<Product>, BigDecimal> getCartAndTotalPrice() {
        return new AbstractMap.SimpleEntry<>(productList, getTotalPrice());
    }

    @Override
    public Map.Entry<Product, Integer> getCurrentStockById(@PathVariable Integer id) {
        Product product = iProductService.findById(id);
        Integer quantity = product.getQuantity() -
                getTotalProductQuantity(product);
        return new AbstractMap.SimpleEntry<>(product, quantity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order save() {
        if (productList.isEmpty()) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_EMPTY_CART);
        }

        List<Product> products = iProductService.findByIds(new ArrayList<>(productMap.keySet()));;

        List<OrderDetails> orderDetailsList = products.stream().map(product -> {
            OrderDetails orderDetails = new OrderDetails();
            Product matchingProduct = productMap.get(product.getId());
            if (matchingProduct != null) {
                orderDetails.setProduct(product);
                orderDetails.setQuantity(matchingProduct.getQuantity());
                orderDetails.setTotal(matchingProduct.getPrice());
                product.setQuantity(product.getQuantity() - matchingProduct.getQuantity());
            }
            return orderDetails;
        }).collect(Collectors.toList());

        Order savedOrder = iOrderService.save(orderDetailsList, getTotalPrice(), iUserService.getCurrentUserId());
        productList.clear();
        productMap.clear();

        return savedOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (id == null || id <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }
        if (!iUserService.isAdmin()) {
            throw new AuthenticationException(ERROR_NOT_ADMIN_USER_LOGGED_IN);
        }

        iOrderDetailsRepository.deleteById(id);
    }

    @Override
    public Product addProductToCart(Integer productId, Integer productQuantity) {
        if (productId == null || productId <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }
        if (productQuantity == null || productQuantity <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_QUANTITY);
        }

        Product product = iProductService.findById(productId);
        iProductService.validateAndThrow(product);

        return addProductToList(product, productQuantity);
    }

    @Override
    public void removeProductFromCart(Integer productId) {
        if (productId == null || productId <= 0) {
            throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_ID);
        }

        Product product = productMap.get(productId);
        if (product != null) {
            productList.remove(product);
            productMap.remove(productId);
        }
    }

    @Override
    public BigDecimal getTotalPrice() {
        return productList.stream()
                .map(Product::getPrice)
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
        long count = productList.stream()
                .filter(p -> p.getId().equals(product.getId()))
                .count();
        return (int) count;
    }

    private Product addProductToList(Product product, Integer productQuantity) {
        Product existingProduct = productMap.get(product.getId());
        if (existingProduct != null) {
            int totalQuantity = existingProduct.getQuantity() + productQuantity;
            if (product.getQuantity() < totalQuantity) {
                throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_STOCK);
            }
            existingProduct.setQuantity(totalQuantity);
            existingProduct.setPrice(product.getPrice().multiply(BigDecimal.valueOf(totalQuantity)));

            return existingProduct;
        } else {
            if (product.getQuantity() < productQuantity) {
                throw new OrderDetailsException(ERROR_ORDER_DETAILS_INVALID_STOCK);
            }
            product.setQuantity(productQuantity);
            product.setPrice(product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())));
            this.productList.add(product);
            this.productMap.put(product.getId(), product);

            return product;
        }
    }

}
