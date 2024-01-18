package com.ecommerce.data;

import com.ecommerce.config.SimpleGrantedAuthority;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.IOrderDetailsService;
import com.ecommerce.service.IOrderService;
import com.ecommerce.service.IProductService;
import com.ecommerce.service.IUserService;
import com.ecommerce.utils.ReferenceGeneratorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.ecommerce.utils.ReferenceGeneratorUtil.generateUniqueReference;

@Component
public class DataGenerator {
    private final IProductService iProductService;
    private final IOrderService iOrderService;
    private final IOrderDetailsService iOrderDetailsService;
    private final IUserService iUserService;


    private final ObjectMapper mapper;
    private static final int ORDER_DETAILS_PER_ORDER = 5;

    @Autowired
    public DataGenerator(IProductService iProductService, IOrderService iOrderService,
                         IOrderDetailsService iOrderDetailsService, IUserService iUserService,
                         ObjectMapper mapper) {
        this.iProductService = iProductService;
        this.iOrderService = iOrderService;
        this.iOrderDetailsService = iOrderDetailsService;
        this.iUserService = iUserService;
        this.mapper = mapper;
    }

    public void generateDataAndWriteToJson() throws IOException {
        int total_users = 50;
        int total_products = 100;
        int total_orders = 40;
        int total_order_details = total_orders * ORDER_DETAILS_PER_ORDER;

        List<User> users = generateUsers(total_users);
        List<Product> products = generateProducts(total_products, users);
        List<OrderDetails> orderDetails = generateOrderDetails(total_order_details, products);
        List<Order> orders = generateOrders(total_orders, users, orderDetails);

        try {
            validateData(users, products, orders, orderDetails);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        writeToJsonFile(users, "src/main/resources/data/user.json");
        writeToJsonFile(products, "src/main/resources/data/product.json");
        writeToJsonFile(orders, "src/main/resources/data/order.json");
    }

    private void writeToJsonFile(List<?> data, String filePath) throws IOException {
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);

        File jsonFile = new File(filePath);
        if (jsonFile.exists() && !jsonFile.delete()) {
            throw new IOException("Failed to delete existing file " + filePath);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(json);
        }
    }

    public void validateData(List<User> users, List<Product> products, List<Order> orders,
                             List<OrderDetails> orderDetails) throws ExecutionException, InterruptedException {
        CompletableFuture<List<String>> userValidation = CompletableFuture.supplyAsync(() ->
                iUserService.validateUsers(users));
        CompletableFuture<List<String>> productValidation = CompletableFuture.supplyAsync(() ->
                iProductService.validateProducts(products));
        CompletableFuture<List<String>> orderValidation = CompletableFuture.supplyAsync(() ->
                iOrderService.validateOrders(orders));
        CompletableFuture<List<String>> orderDetailsValidation = CompletableFuture.supplyAsync(() ->
                iOrderDetailsService.validateOrdersDetails(orderDetails));

        CompletableFuture.allOf(userValidation, productValidation, orderValidation, orderDetailsValidation).join();

        List<String> validationErrors = new ArrayList<>();
        validationErrors.addAll(userValidation.get());
        validationErrors.addAll(productValidation.get());
        validationErrors.addAll(orderValidation.get());
        validationErrors.addAll(orderDetailsValidation.get());

        if (!validationErrors.isEmpty()) {
            throw new UserException(validationErrors.toString());
        }
    }

    private List<User> generateUsers(Integer quantity) {
        List<User> users = new ArrayList<>();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        List<SimpleGrantedAuthority> adminAuthorities = Collections.singletonList(new SimpleGrantedAuthority(
                "ROLE_ADMIN"));
        List<SimpleGrantedAuthority> userAuthorities = Collections.singletonList(new SimpleGrantedAuthority(
                "ROLE_USER"));


        for (int i = 1; i <= quantity; i++) {
            User user = new User();
            user.setId(i);
            user.setAddress("address " + i);
            user.setEmail("email" + i + "@example.com");
            user.setName("name" + i);
            user.setPassword(passwordEncoder.encode("123"));
            user.setPhoneNumber("00" + i);
            user.setUsername("username" + i);

            if (i <= 10) {
                user.setAuthorities(adminAuthorities);
            } else {
                user.setAuthorities(userAuthorities);
            }

            users.add(user);
        }

        return users;
    }

    private List<Order> generateOrders(Integer quantity, List<User> users, List<OrderDetails> orderDetails) {
        List<Order> orders = new ArrayList<>();
        Random rand = new Random();
        Date currentDate = new Date(System.currentTimeMillis());
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        int orderDetailsIndex = 0;
        BigDecimal orderTotal = BigDecimal.ZERO;

        for (int i = 1; i <= quantity; i++) {
            Order order = new Order();
            order.setId(i);
            order.setCreationDate(currentDate);
            order.setReceivedDate(currentDate);
            order.setReference(generateUniqueReference(ReferenceGeneratorUtil.ORDER_PREFIX));
            order.setUserId(users.get(rand.nextInt(users.size())).getId());

            for (int j = 0; j < ORDER_DETAILS_PER_ORDER; j++) {
                orderTotal = orderTotal.add(orderDetails.get(orderDetailsIndex).getTotal());
                orderDetailsList.add(orderDetails.get(orderDetailsIndex));
                orderDetailsIndex++;
            }

            order.setOrderDetails(new ArrayList<>(orderDetailsList));
            orderDetailsList.clear();

            order.setTotal(orderTotal);
            orderTotal = BigDecimal.ZERO;

            orders.add(order);
        }

        return orders;
    }

    private List<Product> generateProducts(Integer quantity, List<User> users) {
        List<Product> products = new ArrayList<>(quantity);
        Product product;
        BigDecimal basePrice = new BigDecimal("1.22");
        RoundingMode roundingMode = RoundingMode.HALF_UP;
        StringBuilder productName = new StringBuilder();
        StringBuilder productDescription = new StringBuilder();
        Random rand = new Random();

        for (int i = 1; i <= quantity; i++) {
            product = new Product();
            product.setId(i);

            productName.setLength(0);
            productName.append("Product ").append(i);
            product.setName(productName.toString());

            productDescription.setLength(0);
            productDescription.append("Description for product ").append(i);
            product.setDescription(productDescription.toString());

            product.setPrice(new BigDecimal(i).multiply(basePrice).setScale(2, roundingMode));
            product.setQuantity(900);
            product.setReference(generateUniqueReference(ReferenceGeneratorUtil.PRODUCT_PREFIX));

            User randomUser = users.get(rand.nextInt(10));
            product.setUserId(randomUser.getId());

            product.setDeleted(false);

            products.add(product);
        }

        return products;
    }

    private List<OrderDetails> generateOrderDetails(Integer quantity, List<Product> products) {
        List<OrderDetails> orderDetails = new ArrayList<>(quantity);

        OrderDetails orderDetail;
        Random rand = new Random();
        BigDecimal quantityBigDecimal;
        Product randomProduct;

        for (int i = 1; i <= quantity; i++) {
            orderDetail = new OrderDetails();
            orderDetail.setId(i);
            orderDetail.setQuantity(i);

            randomProduct = products.get(rand.nextInt(products.size()));
            orderDetail.setProduct(randomProduct);

            quantityBigDecimal = BigDecimal.valueOf(orderDetail.getQuantity());
            orderDetail.setTotal(randomProduct.getPrice().multiply(quantityBigDecimal));

            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }
}
