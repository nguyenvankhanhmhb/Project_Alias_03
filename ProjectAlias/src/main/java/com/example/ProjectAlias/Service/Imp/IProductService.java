package com.example.ProjectAlias.Service.Imp;

import com.example.ProjectAlias.Entity.ProductEntity;
import com.example.ProjectAlias.payoad.request.ProductRequest;
import com.example.ProjectAlias.payoad.response.ProductResponse;

import java.util.List;

public interface IProductService {
    List<ProductEntity> getAllProducts();
    List<ProductEntity> searchProducts(String keyword);

    boolean saveProduct(ProductRequest product);
    ProductEntity getProductById(int id);

    void deleteProductById(int id);
    List<ProductResponse> getProductByCategoryId(String hostName, int id);
    boolean addProduct(ProductRequest productRequest);

    ProductResponse getDetailProduct(int id);

    boolean clearCache();

}
