package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.Address
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address)

    @Update
    fun updateAddress(address: Address)

    @Delete
    fun deleteAddress(address: Address)

    @Query("SELECT * FROM Address WHERE id == :id")
    fun getAddressById(id: Int): List<Address>

    @Query("SELECT * FROM Address")
    fun getAllAddress(): List<Address>
}