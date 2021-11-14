package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.entity.ProductStockEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.db.repository.ProductStockRepository
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.domain.product.ProductUpdate
import de.imedia24.shop.exception.ProductException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ProductService(private val productRepository: ProductRepository,
                     private val productStockRepository: ProductStockRepository   ) {

    fun findProductBySku(sku: String): ProductResponse? {
        val response = productRepository.findBySku(sku)?.toProductResponse()
        if(response != null){
            response.quantity = getProductQuantityFromStock(sku)
        }
       return response
    }

    fun createProduct(productRequest: ProductRequest): ProductResponse {
        productRepository.findAll().forEach {
            product ->
            run {
                if (product.sku == productRequest.sku)
                    throw ProductException("Product with sku ${productRequest.sku} already exist")
            }
        }
        val productEntity = ProductEntity(
                sku = productRequest.sku,
                name = productRequest.name,
                description = productRequest.description,
                price = productRequest.price,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
        )
        productStockRepository.save(ProductStockEntity(
                productId = productRequest.sku,
                quantity = productRequest.quantity
        ))

        val response = getResponseAfterSavingOrUpdating(productEntity)
        response.quantity = getProductQuantityFromStock(productRequest.sku)
        return response
    }

    fun updateProduct(sku: String, productTotUpdate: ProductUpdate): ProductResponse {
        val product = productRepository.findBySku(sku)
        val productEntity = ProductEntity(
                sku = sku,
                name= productTotUpdate.name,
                description= productTotUpdate.description,
                price= productTotUpdate.price,
                createdAt = product!!.createdAt,
                updatedAt = ZonedDateTime.now(),
        )
        productStockRepository.save(ProductStockEntity(
                productId = sku,
                quantity = productTotUpdate.quantity
        ))
        val response = getResponseAfterSavingOrUpdating(productEntity)
        response.quantity = getProductQuantityFromStock(sku)
        return response
    }


    /**
         utils method
     */
    fun getProductQuantityFromStock(productId:String):Int{
        return productStockRepository.findByProductId(productId)!!.quantity
    }
    fun getResponseAfterSavingOrUpdating(productEntity : ProductEntity):ProductResponse{
        return productRepository.save(productEntity).toProductResponse()
    }
}
