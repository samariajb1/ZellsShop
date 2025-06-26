package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
public class CategoriesController {

    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }
    // create an Autowired controller to inject the categoryDao and ProductDao

    @GetMapping
    public List<Category> getAll() { //open to all users
       return categoryDao.getAllCategories();
    }

  @GetMapping("{id}")   // open to all users
    public ResponseEntity<Category> getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if (category == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(category);
    }

    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products") //
    public List<Product> getProductsByCategory(@PathVariable int categoryId) {
        // get a list of product by categoryId
        return  productDao.listByCategoryId(categoryId); //TODO: Dont know if this right or not
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) throws SQLException {
        Category created = categoryDao.create(category);
        return  ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCategory(@PathVariable int id, @RequestBody Category category) {
        categoryDao.update(id, category);
      return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void>  deleteCategory(@PathVariable int id) {
      categoryDao.delete(id);
    return ResponseEntity.noContent().build();
    }
}
