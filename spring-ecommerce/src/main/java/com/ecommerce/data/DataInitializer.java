package com.ecommerce.data;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.IOrderRepository;
import com.ecommerce.repository.IProductRepository;
import com.ecommerce.repository.IUserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final DataGenerator dataGenerator;
    private final ObjectMapper mapper;

    @Autowired
    public DataInitializer(IUserRepository userRepository, IOrderRepository orderRepository,
                           IProductRepository productRepository, DataGenerator dataGenerator, ObjectMapper mapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.dataGenerator = dataGenerator;
        this.mapper = mapper;
    }

    @Override
    public void run(String... args) throws Exception {
        dataGenerator.generateDataAndWriteToJson();
        initializeData();
    }

    public void initializeData() throws IOException {
        String userJsonPath = "src/main/resources/data/user.json";
        String productJsonPath = "src/main/resources/data/product.json";
        String orderJsonPath = "src/main/resources/data/order.json";

        // Load users from user.json
        InputStream inputStream = new FileInputStream(userJsonPath);
        TypeReference<List<User>> userTypeReference = new TypeReference<>() {
        };
        List<User> users = mapper.readValue(inputStream, userTypeReference);
        userRepository.saveAll(users);

        // Load products from product.json
        inputStream = new FileInputStream(productJsonPath);
        TypeReference<List<Product>> productTypeReference = new TypeReference<>() {
        };
        List<Product> products = mapper.readValue(inputStream, productTypeReference);
        productRepository.saveAll(products);

        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        inputStream = new FileInputStream(orderJsonPath);
        TypeReference<List<Order>> orderTypeReference = new TypeReference<>() {
        };
        List<Order> orders = mapper.readValue(inputStream, orderTypeReference);

        for (Order order : orders) {
            for (OrderDetails orderDetails : order.getOrderDetails()) {
                Product product = productMap.get(orderDetails.getProduct().getId());
                orderDetails.setProduct(product);
            }
        }

        orderRepository.saveAll(orders);
    }
}

