package de.imedia24.shop.db.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "productStock")
data class ProductStockEntity(
    @Id
    @Column(name = "product_id", nullable = false)
    val productId: String,

    @Column(name = "quantity", nullable = false)
    val quantity: Int = 0
)
