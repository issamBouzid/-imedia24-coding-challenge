package de.imedia24.shop.db.repository

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.entity.ProductStockEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductStockRepository : CrudRepository<ProductStockEntity, String> {

    fun findByProductId(productId: String): ProductStockEntity?
}