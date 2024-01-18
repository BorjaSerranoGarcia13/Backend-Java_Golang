package com.ecommerce.service;

import com.ecommerce.constants.messages.ProductExceptionMessages;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.exception.AuthenticationException;
import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Product;
import com.ecommerce.repository.IProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ecommerce.constants.messages.ProductExceptionMessages.*;
import static com.ecommerce.constants.messages.UserExceptionMessages.ERROR_NOT_ADMIN_USER_LOGGED_IN;
import static com.ecommerce.utils.ReferenceGeneratorUtil.PRODUCT_PREFIX;
import static com.ecommerce.utils.ReferenceGeneratorUtil.generateUniqueReference;
import static com.ecommerce.utils.StringUtil.*;

@Service
public class ProductServiceImplementation implements IProductService {
    private final IProductRepository iProductRepository;
    private final IUserService iUserService;
    private final ModelMapper modelMapper;

    public ProductServiceImplementation(IProductRepository iProductRepository, IUserService iUserService,
                                        ModelMapper modelMapper) {
        this.iProductRepository = iProductRepository;
        this.iUserService = iUserService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Product> findAll() {
        return iProductRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return iProductRepository.findByIsDeletedFalse(pageable);
    }

    @Override
    public Product findById(Integer id) {
        Optional<Product> optionalProduct = iProductRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_NOT_FOUND);
        }

        return optionalProduct.get();
    }

    @Override
    public List<Product> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return iProductRepository.findByIds(ids);
    }

    @Override
    public Product findByReference(String reference) {
        if (reference == null || !reference.startsWith(PRODUCT_PREFIX)) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_REFERENCE);
        }

        Optional<Product> optionalProduct = iProductRepository.findByReference(reference);
        if (optionalProduct.isEmpty()) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_NOT_FOUND);
        }

        return optionalProduct.get();
    }

    @Override
    public List<Product> findProductsByReference(String reference) {
        if (reference == null || reference.isEmpty()) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_REFERENCE);
        }
        if (!reference.startsWith(PRODUCT_PREFIX)) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_REFERENCE);
        }

        return iProductRepository.findProductsByReference(reference);
    }

    @Override
    public List<Product> findProductsByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_NAME);
        }
        return iProductRepository.findProductsByName(name);
    }

    @Override
    public List<Product> searchProductsByNameOrReference(String searchTerm, String searchType) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_SEARCH_TERM);
        }
        if (searchType == null || (!searchType.equals("name") && !searchType.equals("reference"))) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_SEARCH_TYPE);
        }
        if (searchType.equals("name")) {
            return findProductsByName(searchTerm);
        } else {
            return findProductsByReference(searchTerm);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product save(ProductDto productDto) {
        if (!iUserService.isAdmin()) {
            throw new AuthenticationException(ERROR_NOT_ADMIN_USER_LOGGED_IN);
        }
        if (productDto == null) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID);
        }
        if (productDto.getId() != null || productDto.getReference() != null || productDto.getName() == null ||
                productDto.getDescription() == null || productDto.getPrice() == null ||
                productDto.getQuantity() == null) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_FIELD);
        }

        String reference = generateUniqueReference(PRODUCT_PREFIX);
        productDto.setReference(reference);

        if (checkProductExists(productDto)) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_ALREADY_EXISTS);
        }

        Product product = convertDtoToProduct(productDto);

        product.setUserId(iUserService.getCurrentUserId());

        validateAndThrow(product);

        return iProductRepository.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product update(ProductDto productDto) {
        if (!iUserService.isAdmin()) {
            throw new AuthenticationException(ERROR_NOT_ADMIN_USER_LOGGED_IN);
        }
        if (productDto == null) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID);
        }
        if (productDto.getReference() != null || productDto.getId() == null || productDto.getId() <= 0 ||
                productDto.getDeleted()) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_FIELD);
        }

        if (findProductAndCheckNameExists(productDto)) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_NOT_FOUND_OR_NAME_ALREADY_EXISTS);
        }

        Product product = findById(productDto.getId());
        updateProductFromDto(product, productDto);
        product.setUserId(iUserService.getCurrentUserId());

        validateAndThrow(product);

        return iProductRepository.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAllProducts(Collection<Product> products) {
        if (!iUserService.isAdmin()) {
            throw new AuthenticationException(ERROR_NOT_ADMIN_USER_LOGGED_IN);
        }
        if (products == null || products.isEmpty()) {
            throw new ProductException(ERROR_PRODUCT_INVALID);
        }

        List<String> allErrors = validateProducts(new ArrayList<>(products));
        if (!allErrors.isEmpty()) {
            throw new ProductException(String.join(", ", allErrors));
        }
        iProductRepository.saveAll(products);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (!iUserService.isAdmin()) {
            throw new AuthenticationException(ERROR_NOT_ADMIN_USER_LOGGED_IN);
        }
        if (id == null || id <= 0) {
            throw new ProductException(ProductExceptionMessages.ERROR_PRODUCT_INVALID_ID);
        }

        iProductRepository.markAsDeleted(id);
    }

    @Override
    public void validateAndThrow(Product product) {
        List<String> validationProductErrors = validateProduct(product);
        if (!validationProductErrors.isEmpty()) {
            throw new ProductException(String.join(", ", validationProductErrors));
        }
    }

    private Boolean checkProductExists(ProductDto productDto) {
        return iProductRepository.findByNameOrReference(removeExtraWhitespaces(productDto.getName()),
                productDto.getReference()).isPresent();
    }

    private Boolean findProductAndCheckNameExists(ProductDto productDto) {
        List<Product> products = iProductRepository.findByIdOrName(productDto.getId(), productDto.getName());
        if (products.size() != 1 || !products.get(0).getId().equals(productDto.getId())) {
            return true;
        }

        productDto.setReference(products.get(0).getReference());

        return false;
    }

    private void updateProductFromDto(Product product, ProductDto productDto) {
        modelMapper.map(productDto, product);
    }

    @Override
    public Product convertDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        modelMapper.map(productDto, product);
        return product;
    }

    @Override
    public List<Product> convertDtoToProduct(List<ProductDto> productDtos) {
        return productDtos.stream()
                .map(this::convertDtoToProduct)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto convertProductToDto(Product product) {
        ProductDto productDto = new ProductDto();
        modelMapper.map(product, productDto);
        return productDto;
    }

    @Override
    public List<ProductDto> convertProductToDto(List<Product> products) {
        return products.stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList());
    }

    private List<String> validateProduct(Product product) {
        List<String> errors = new ArrayList<>();

        if (product == null) {
            errors.add(ERROR_PRODUCT_INVALID);
            return errors;
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            errors.add(ProductExceptionMessages.ERROR_PRODUCT_INVALID_NAME);
        } else if (product.getName().length() > MAX_PRODUCT_NAME_LENGTH) {
            errors.add(ERROR_PRODUCT_INVALID_NAME_MAX_LENGTH);
        }
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            errors.add(ProductExceptionMessages.ERROR_PRODUCT_INVALID_DESCRIPTION);
        } else if (product.getDescription().length() > MAX_PRODUCT_DESCRIPTION_LENGTH) {
            errors.add(ERROR_PRODUCT_INVALID_DESCRIPTION_MAX_LENGTH);
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(ProductExceptionMessages.ERROR_PRODUCT_INVALID_PRICE);
        } else if (product.getPrice().compareTo(MAX_PRODUCT_PRICE) > 0) {
            errors.add(ERROR_PRODUCT_INVALID_PRICE_MAX_);
        }
        if (product.getQuantity() < 0) {
            errors.add(ProductExceptionMessages.ERROR_PRODUCT_INVALID_QUANTITY);
        } else if (product.getQuantity() > MAX_PRODUCT_QUANTITY) {
            errors.add(ERROR_PRODUCT_INVALID_QUANTITY_MAX);
        }
        if (product.getUserId() == null || product.getUserId() <= 0) {
            errors.add(ERROR_PRODUCT_INVALID_USER_ID);
        }

        product.setName(removeExtraWhitespaces(product.getName()));
        product.setDescription(removeExtraWhitespaces(product.getDescription()));

        return errors;
    }

    @Override
    public List<String> validateProducts(List<Product> products) {
        List<String> errors = new ArrayList<>();

        if (products == null || products.isEmpty()) {
            errors.add(ERROR_PRODUCT_INVALID);
            return errors;
        }

        for (Product product : products) {
            List<String> productErrors = validateProduct(product);
            errors.addAll(productErrors);
        }

        return errors;
    }

}
