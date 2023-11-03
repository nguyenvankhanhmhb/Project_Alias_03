package com.example.ProjectAlias.Service;


import com.example.ProjectAlias.Entity.*;
import com.example.ProjectAlias.Repository.ImageRepository;
import com.example.ProjectAlias.Repository.ProductRepository;
import com.example.ProjectAlias.Service.Imp.IProductService;
import com.example.ProjectAlias.payoad.request.ProductRequest;
import com.example.ProjectAlias.payoad.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Value("${host.name}")
    private String hostName;

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductEntity> searchProducts(String keyword) {
        return productRepository.findByTitleContainingIgnoreCase(keyword);
    }

    @Override
    public boolean saveProduct(ProductRequest product) {
        try {
            // Tạo một đối tượng ProductEntity từ dữ liệu trong ProductRequest
            ProductEntity productEntity = new ProductEntity();
            productEntity.setName(product.getName());
            productEntity.setPrice(product.getPrice());
            productEntity.setQuanity(product.getQuantity());
            // Các bước khác...

            // Lưu trữ ProductEntity đã được tạo
            productRepository.save(productEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public ProductEntity getProductById(int id) {
        Optional<ProductEntity> optional=productRepository.findById(id);
        ProductEntity product=null;
        if(optional.isPresent()) {
            product=optional.get();
        }else {
            throw new RuntimeException("Product not found for id::"+id);
        }
        return product;
    }

    @Override
    public void deleteProductById(int id) {
        this.productRepository.deleteById(id);
    }

    @Override
    @Cacheable("getProductByCategory")
    public List<ProductResponse> getProductByCategoryId(String hostName, int id) {
        System.out.println("kiem tra");
        List<ProductEntity> list = productRepository.findByCategory(id);
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (ProductEntity data: list){
            ProductResponse productResponse =new ProductResponse();
            productResponse.setId(data.getId());
            productResponse.setName(data.getName());
            productResponse.setPrice(data.getPrice());

            ImageEntity image = imageRepository.findById(data.getImage().getId()).orElse(null);
            if(image != null){
                productResponse.setImage("http://" + hostName + "/product/file/" + image.getId());
            }
            productResponseList.add(productResponse);

        }
        return productResponseList;
    }

    @Override
    public boolean addProduct(ProductRequest productRequest) {
        try{
            ProductEntity productEntity = new ProductEntity();
            productEntity.setName(productRequest.getName());
            productEntity.setImage(productEntity.getImage());
            productEntity.setPrice(productRequest.getPrice());
            productEntity.setQuanity(productEntity.getQuanity());

            ColorEntity colorEntity = new ColorEntity();
            colorEntity.setId(productRequest.getColorId());

            SizeEntity sizeEntity = new SizeEntity();
            sizeEntity.setId(productRequest.getSizeId());

            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(productRequest.getCategoryId());

            productEntity.setColor(colorEntity);
            productEntity.setSize(sizeEntity);
            productEntity.setCategory(categoryEntity);

            productRepository.save(productEntity);
            return true;


        }catch (Exception e){
            return false;

        }
    }

    @Override
    public ProductResponse getDetailProduct(int id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        ProductResponse productResponse = new ProductResponse();
        if(product.isPresent()){
            productResponse.setId(product.get().getId());

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setId(productResponse.getId());
            productResponse.setImage(productResponse.getImage());

            productResponse.setName(product.get().getName());
            productResponse.setPrice(product.get().getPrice());
            productResponse.setDesc(product.get().getDesc());

        }
        return productResponse;
    }

    @Override
    @CacheEvict(value = "getProductByCategory", allEntries = true)
    public boolean clearCache() {
        return true;
    }
}
