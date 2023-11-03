package com.example.ProjectAlias.Controller;


import com.example.ProjectAlias.Entity.CategoryEntity;
import com.example.ProjectAlias.Entity.ProductEntity;
import com.example.ProjectAlias.Repository.CategoryRepository;
import com.example.ProjectAlias.Repository.ProductRepository;
import com.example.ProjectAlias.Service.Imp.IProductService;
import com.example.ProjectAlias.payoad.request.ProductRequest;
import com.example.ProjectAlias.payoad.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin()
@RequestMapping("/api")
public class ProductController {

    private static final String UPLOAD_DIRECTORY = System.getProperty("products.dir") + "/src/main/front-end/allaia/img";

    @Autowired
    private ProductRepository productRepository;

   @Autowired
    private IProductService iProductService;

   @Autowired
   private CategoryRepository categoryRepository;



   @Value("${root.file.path}")
    private String rootPath;

   Logger logger =LoggerFactory.getLogger(ProductController.class);


   @GetMapping("all")
   public ResponseEntity<?> getAllProducts(){
       List<ProductEntity> products = iProductService.getAllProducts();
       if(products != null && !products.isEmpty()){
           return new ResponseEntity<>(products,HttpStatus.OK);
       }else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
   }


   @GetMapping("/products")
   public String searchProduct (@RequestParam String keyword, Model model){
       try{
           List<ProductEntity> productEntities = new ArrayList<>();
           if(keyword != null){
               productRepository.findAll().forEach(productEntities :: add);
           }else {
               productRepository.findByTitleContainingIgnoreCase(keyword).forEach(productEntities :: add);
               model.addAttribute("keyword",keyword);

           }
           model.addAttribute("products",productEntities);
       }catch (Exception e){
           model.addAttribute("message", e.getMessage());
       }
       return "products";
   }

@PostMapping("products/save")
public ResponseEntity<?>  saveProduct(@RequestBody ProductRequest productRequest){
       boolean result = iProductService.saveProduct(productRequest);
       if(result){
           return  new ResponseEntity<>(HttpStatus.CREATED);
       }else {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }

       //       try{
//           productRepository.save(product);
//           redirectAttributes.addFlashAttribute("message","The Product has been saved successfully!");
//       }catch(Exception e){
//           redirectAttributes.addAttribute("message", e.getMessage());
//       }
//       return "redirect:/products";
//       iProductService.saveProduct(product);
//       return new ResponseEntity<>("Product save Successfully", HttpStatus.OK);
}
    @PostMapping("product/edit/{id}")
    public ResponseEntity<?> editProduct(@PathVariable int id,
                                         @ModelAttribute("product") ProductRequest updatedProduct,
                                         @RequestParam("imageFile") MultipartFile imageProduct,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Errors in the submitted data.");
        }

        try {
            ProductEntity product = iProductService.getProductById(id);
            if (product != null) {
                // Cập nhật thông tin sản phẩm từ updatedProduct vào sản phẩm tương ứng
                product.setName(updatedProduct.getName());
                product.setPrice(updatedProduct.getPrice());
                product.setQuanity(updatedProduct.getQuantity());
                product.setDiscount(updatedProduct.getDiscount());
                product.setDesc(updatedProduct.getDesc());

                // Xử lý và cập nhật ảnh sản phẩm (nếu có)
                if (imageProduct != null && imageProduct.getSize() > 0) {
                    String fileName = imageProduct.getOriginalFilename();
                    String rootFolder = rootPath;
                    Path pathRoot = Paths.get(rootFolder);
                    if (!Files.exists(pathRoot)) {
                        Files.createDirectory(pathRoot);
                    }
                    Files.copy(imageProduct.getInputStream(), pathRoot.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    updatedProduct.setImageId(fileName.toString());
//                    product.setImage(fileName); // Cập nhật tên file ảnh mới
                }

                // Gọi phương thức trong service hoặc repository để cập nhật thông tin sản phẩm
                iProductService.addProduct(updatedProduct);

                return ResponseEntity.ok("Product Update Successful");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update product");
        }
    }

@GetMapping("/{id}")
public ResponseEntity<?> getProductById(@PathVariable int id){
       ProductEntity product = iProductService.getProductById(id);
       if(product != null){
           return new ResponseEntity<>(product,HttpStatus.OK);
       }else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
}
    @DeleteMapping("products/delete/{id}")
    public String deleteProductById(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
       try{
           productRepository.deleteById(id);
           redirectAttributes.addFlashAttribute("message", "The product with id=" + id + "has been delete successfully!");

       }catch (Exception e){
           redirectAttributes.addFlashAttribute("mesage", e.getMessage());
       }
        return "redirect:/products";
    }

   @GetMapping("/{id}")
    public ResponseEntity<?> getDetailProduct(@PathVariable int id){
       BaseResponse response = new BaseResponse();
       response.setData(iProductService.getDetailProduct(id));

       return new ResponseEntity<>(response, HttpStatus.OK);
   }

   @GetMapping("/clear-cache")
    public ResponseEntity<?> clearCache(){
       iProductService.clearCache();

       return new ResponseEntity<>("", HttpStatus.OK);
   }

   @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductByCategory(HttpServletRequest request, @PathVariable int id){
       String hostName = request.getHeader("host");

       BaseResponse response = new BaseResponse();
       response.setData(iProductService.getProductByCategoryId(hostName,id));
       return new ResponseEntity<>(response,HttpStatus.OK);
   }

   @GetMapping("/file/{filename}")
    public ResponseEntity<?> downloadFilePrduct(@PathVariable String filename) throws FileNotFoundException {
       try {

           Path path = Paths.get(rootPath);

           Path pathFile = path.resolve(filename);
           Resource resource = new UrlResource(pathFile.toUri());
           if(resource.exists() || resource.isReadable()){
               return ResponseEntity.ok()
                       .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                       .body(resource);
           }else {
               throw new FileNotFoundException("Khong tim thay file");
           }
       } catch (Exception e) {
           //loi
           throw new FileNotFoundException("khong tim thay file");
       }
   }

@PostMapping("/product/create")
public ResponseEntity<?>  createProduct (ProductRequest newProduct,
                                          @RequestParam("imageFile") MultipartFile imageProduct,
                                          @RequestParam("categoryId") int categoryId,
                                          BindingResult result,Model model) throws IOException {

    if (result.hasErrors()) {
//        model.addAttribute("product", newProduct);
//        model.addAttribute("cate", categoryRepository.findAll());

        return new ResponseEntity<>("Invilid data submitted", HttpStatus.BAD_REQUEST);
    }

    Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
    if (category.isPresent()) {
        CategoryEntity categoryEntity = category.get();
        newProduct.setCategoryId(categoryEntity.getId_category());//Gán Id của category

//    StringBuilder fileNames = null;
    StringBuilder fileNames;
    if (imageProduct != null && imageProduct.getSize() > 0) {

        fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imageProduct.getOriginalFilename());
        fileNames.append(imageProduct.getOriginalFilename());
        Files.write(fileNameAndPath, imageProduct.getBytes());
        newProduct.setImageId(fileNames.toString());
    } else {
        return new ResponseEntity<>("File not found or invalid", HttpStatus.BAD_REQUEST);

    }
    // Tạo một đối tượng Product từ dữ liệu trong ProductRequest
    ProductRequest productRequest = new ProductRequest();
    productRequest.setName(newProduct.getName());
    productRequest.setPrice(newProduct.getPrice());
    productRequest.setDesc(newProduct.getDesc());


    // Lưu sản phẩm vào cơ sở dữ liệu
    iProductService.saveProduct(newProduct);
    // Trả về kết quả
    if (fileNames != null) {
        return new ResponseEntity<>(fileNames.toString(), HttpStatus.OK);
    } else {
        return new ResponseEntity<>("File not found or invalid", HttpStatus.BAD_REQUEST);
    }
 }else{
        return new ResponseEntity<>("Category not found", HttpStatus.BAD_REQUEST);
    }
   }

    @PostMapping("")
    public ResponseEntity<?> addProduct(@PathVariable ProductRequest productRequest) {
        String fileName = productRequest.getFile().getOriginalFilename();
        try {
            // Tạo thư mục lưu trữ nếu chưa tồn tại

            String rootFolder = rootPath;
            Path pathRoot = Paths.get(rootFolder);
            if (!Files.exists(pathRoot)) {
                Files.createDirectory(pathRoot);

            }

            Files.copy(productRequest.getFile().getInputStream(), pathRoot.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            // Tạo đối tượng Product từ dữ liệu trong ProductRequest
            ProductRequest product = new ProductRequest();
            product.setName(product.getName());
            product.setPrice(product.getPrice());

            iProductService.addProduct(product);
            return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add product", HttpStatus.BAD_REQUEST);

        }
    }
}
