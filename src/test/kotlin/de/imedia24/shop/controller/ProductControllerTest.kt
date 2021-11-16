@file:Suppress("UnusedImport")

package de.imedia24.shop.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.service.ProductService
import io.mockk.mockk
import org.aspectj.lang.annotation.Before
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.math.BigDecimal
import java.time.ZonedDateTime

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ProductControllerTest {

	@Autowired
	lateinit var mockMvc: MockMvc
	@Autowired
	lateinit var objectMapper: ObjectMapper


	@Test
	@Order(1)
	fun `should create some products` (){
		val requestProd1 = ProductRequest(sku = "prod1", name = "phone1", description = "brand new 1", price = BigDecimal(87000),quantity = 1000)
		val requestProd2 = ProductRequest(sku = "prod2", name = "phone2", description = "brand new 2", price = BigDecimal(26700),quantity = 1500)

		val listOfProducts = listOf<ProductRequest>(requestProd1,requestProd2)
		listOfProducts.forEach{
			productRequest ->
			run {
					mockMvc.post("/products"){
					contentType = MediaType.APPLICATION_JSON
					content = objectMapper.writeValueAsString(productRequest)
				}.andDo { print() }
				.andExpect {
					status { isCreated() }
				}
			}
		}
	}

	@Nested
	@DisplayName("Get Products /products/skus=sku1,sku2,sku3")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	inner class GetProductsByListOfSkus {
		@Test
		fun `should get an empty list of products for an empty request parameter list of skus` (){
			val skus = ""
			mockMvc.get("/products/skus=$skus")
					.andDo { print() }
					.andExpect {
						status { isBadRequest() }
						jsonPath("$.message"){ value("the skus parameter is empty!")}
					}
		}

		@Test
		fun `non of the filled skus correspond to a product` (){

			val notExistingSku1 = "notExist1"
			val notExistingSku2 = "notExist2"
			mockMvc.get("/products/skus=$notExistingSku1,$notExistingSku2")
					.andDo { print() }
					.andExpect {
						status { isBadRequest() }
						jsonPath("$.message"){ value("no products exist with the given skus!")}
					}
		}

		@Test
		fun `should retrieve products that correspond to the given request parameter list of skus, and ignore not existing ones` (){

			val existingSku1 = "prod1"
			val existingSku2 = "prod2"
			val notExistingSku = "prod3"
			mockMvc.get("/products/skus=$existingSku1,$existingSku2,$notExistingSku")
					.andDo { print() }
					.andExpect {
						status { isOk() }
						jsonPath("$.prod1.sku"){ value("prod1")}
						jsonPath("$.prod2.sku"){ value("prod2")}
					}
		}


	}

}
