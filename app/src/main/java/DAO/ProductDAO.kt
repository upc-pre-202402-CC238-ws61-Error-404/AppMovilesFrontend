package DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import Entities.Product

@Dao
interface ProductDAO {
    @Insert
    fun insertProduct(product: Product)

    @Query("SELECT * FROM products WHERE sowingId = :sowingId")
    fun getProductsBySowingId(sowingId: Int): List<Product>
}
