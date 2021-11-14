package de.imedia24.shop.domain.product

import de.imedia24.shop.db.entity.ProductEntity
import java.math.BigDecimal
import java.time.ZonedDateTime

data class ProductUpdate (

    val name: String,
    val description: String,
    val price: BigDecimal,
    val quantity: Int
)
