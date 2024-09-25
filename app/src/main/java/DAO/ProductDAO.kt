package DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import Entities.Product
import Entities.Sowing
import androidx.room.Update

@Dao
interface ProductDAO {
    @Insert
    fun insertProduct(product: Product)

    @Query("SELECT * FROM products WHERE sowingId = :sowingId")
    fun getProductsBySowingId(sowingId: Int): List<Product>

    @Query("DELETE FROM products WHERE id = :productId")
    fun deleteProduct(productId: Int)

    @Update
    fun updateProduct(product: Product)
}
