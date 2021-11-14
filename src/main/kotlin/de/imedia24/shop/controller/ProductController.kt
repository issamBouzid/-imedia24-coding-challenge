package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdate
import de.imedia24.shop.exception.ProductException
import de.imedia24.shop.service.ProductService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @ApiOperation(value = "Get a product based on the attributed sku in the url ")
    @GetMapping("/products/{sku}", produces = ["application/json;charset=utf-8"])
    fun findProductsBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product $sku")

        val product = productService.findProductBySku(sku)
        return if(product == null) {
            throw ProductException("the product with sku : '$sku' was not found")
        } else {
            ResponseEntity.ok(product)
        }
    }

    @ApiOperation(value = "Get the list of Products based on their attributed skus in the url ")
    @GetMapping("/products/skus={skus}", produces = ["application/json;charset=utf-8"])
    fun findProductsBySkuList(
            @PathVariable("skus") skus: List<String>
    ):ResponseEntity<Map<String,ProductResponse>>{
        val products = HashMap<String,ProductResponse>()
        return if(skus.isEmpty()) {
              throw ProductException("the skus parameter is empty!")
        }else {
            for (sku in skus) {
                val productResponse = productService.findProductBySku(sku) ?: continue
                products[sku] = productResponse
            }
            if(products.isEmpty()) throw ProductException("no products exist with the given skus!")
            return ResponseEntity.ok(products)
        }
    }

    @ApiOperation(value = "Add a new product and fill its quantity in product stock table ")
    @PostMapping("/products")
    fun addProduct(
            @RequestBody productRequest: ProductRequest
    ):ResponseEntity<ProductResponse>{
        if(productRequest.sku.isEmpty()){
            throw ProductException("skus parameter must be not empty")
        }
        val productResponse = productService.createProduct(productRequest)
        return ResponseEntity(productResponse,HttpStatus.CREATED)
    }

    @ApiOperation(value = "update an existing product and update its quantity as well in product stock table ")
    @PatchMapping("/products/{sku}")
    fun updateProduct(
            @PathVariable("sku") sku : String,
            @RequestBody productToUpdate : ProductUpdate
    ):ResponseEntity<ProductResponse>{
        if(productService.findProductBySku(sku) == null){
            throw ProductException("the product with sku : '$sku' was not found")
        }
        val productUpdated = productService.updateProduct(sku,productToUpdate)
        return ResponseEntity.ok(productUpdated)
    }

}
