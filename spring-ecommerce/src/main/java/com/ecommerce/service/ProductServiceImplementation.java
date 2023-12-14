package com.ecommerce.service;

import com.ecommerce.constants.messages.ProductErrorMessages;
import com.ecommerce.exception.OrderDetailsException;
import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.IProductRepository;
import com.ecommerce.utils.UserSessionUtils;
import com.ecommerce.utils.Validator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.ecommerce.constants.messages.ProductErrorMessages.ERROR_PRODUCT_INVALID_ADMIN_USER;
import static com.ecommerce.service.UploadFileService.IMAGE_BY_DEFAULT;
import static com.ecommerce.utils.ReferenceGeneratorUtils.*;
import static com.ecommerce.utils.StringUtils.*;

@Service
public class ProductServiceImplementation implements IProductService {
    private final IProductRepository iProductRepository;
    private final UploadFileService uploadFileService;
    private final UserSessionUtils userSessionUtils;

    public ProductServiceImplementation(IProductRepository iProductRepository, UploadFileService uploadFileService, UserSessionUtils userSessionUtils) {
        this.iProductRepository = iProductRepository;
        this.uploadFileService = uploadFileService;
        this.userSessionUtils = userSessionUtils;
    }

    @Override
    public Product findByReference(String reference) {
        Optional<Product> optionalProduct = iProductRepository.findByReference(reference);

        return optionalProduct.orElseThrow(() -> new ProductException(ProductErrorMessages.ERROR_PRODUCT_NOT_FOUND));
    }

    @Override
    public List<Product> findProductsByName(String name) {
        String nameConverted = removeNonAlphanumericAndConvertToLowerCase(name);
        List<Product> products = iProductRepository.findProductsByName(nameConverted);

        if (products.isEmpty()) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_NOT_FOUND);
        }

        return products;
    }

    @Override
    public Product findById(Integer id) {
        Optional<Product> optionalProduct = iProductRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_NOT_FOUND);
        }
        return optionalProduct.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product save(Product product, MultipartFile imageFile, HttpSession session) {
        String imageName = uploadFileService.saveImage(imageFile);
        product.setImage(imageName);

        product.setUser(checkVerifiedAdmin(session));

        String reference = generateUniqueReference(PRODUCT_PREFIX);
        product.setReference(reference);

        List<String> validationProductErrors = Validator.validateProduct(product);
        if (!validationProductErrors.isEmpty()) {
            throw new ProductException(String.join(", ", validationProductErrors));
        }
        String productExistsError = checkProductExists(product.getName());
        if (!productExistsError.isEmpty()) {
            throw new ProductException(String.join(", ", validationProductErrors));
        }

        return iProductRepository.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product update(Product product, MultipartFile newImageFile, HttpSession session) {
        List<String> validationProductErrors = Validator.validateProduct(product);
        if (!validationProductErrors.isEmpty()) {
            throw new ProductException(String.join(", ", validationProductErrors));
        }

        String imageName = uploadFileService.saveImage(newImageFile);
        product.setImage(imageName);

        product.setUser(checkVerifiedAdmin(session));

        return iProductRepository.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllProducts(List<Product> products) {
        for (Product product : products) {
            List<String> validationProductErrors = Validator.validateProduct(product);
            if (!validationProductErrors.isEmpty()) {
                throw new ProductException(String.join(", ", validationProductErrors));
            }
        }
        iProductRepository.saveAll(products);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id, HttpSession session) {
        if (id == null || id <= 0) {
            throw new ProductException(ProductErrorMessages.ERROR_PRODUCT_INVALID_ID);
        }

        checkVerifiedAdmin(session);

        Product product = iProductRepository.findById(id).orElseThrow(() -> new ProductException(ProductErrorMessages.ERROR_PRODUCT_NOT_FOUND));
        String imageName = product.getImage();
        if (imageName != null && !imageName.equals(IMAGE_BY_DEFAULT)) {
            uploadFileService.deleteImage(imageName);
        }
        iProductRepository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return iProductRepository.findAll();
    }

    public String checkProductExists(String name) {
        return iProductRepository.findByName(name).isPresent() ? ProductErrorMessages.ERROR_PRODUCT_ALREADY_EXISTS : "";
    }

    private User checkVerifiedAdmin(HttpSession session) {
        return userSessionUtils.checkVerifiedAdminFromSession(session)
                .orElseThrow(() -> new OrderDetailsException(ERROR_PRODUCT_INVALID_ADMIN_USER));
    }

}
