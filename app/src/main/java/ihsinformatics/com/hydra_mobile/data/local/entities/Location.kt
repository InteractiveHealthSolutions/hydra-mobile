package ihsinformatics.com.hydra_mobile.data.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.LocationTag
import ihsinformatics.com.hydra_mobile.utils.Converters


@Entity
class Location {

    private var name: String = ""
    private var country: String = ""
    private var stateProvince: String = ""
    private var cityVillage: String = ""
    private var primaryContact: String = ""
    private var address1: String = ""
    private var address2: String = ""
    private var address3: String = ""
    private var description: String = ""
    @PrimaryKey var uuid: String
    @TypeConverters(Converters::class) lateinit var tags: List<LocationTag>


    constructor(
        name: String, country: String, stateProvince: String, cityVillage: String, primaryContact: String, address1: String, address2: String, address3: String, description: String, uuid: String, tags: List<LocationTag>
               )  {
        this.name = name
        this.country = country
        this.stateProvince = stateProvince
        this.cityVillage = cityVillage
        this.primaryContact = primaryContact
        this.address1 = address1
        this.address2 = address2
        this.address3 = address3
        this.description = description
        this.uuid = uuid
        this.tags = tags

    }

//    fun getUuid(): String? {
//        return uuid
//    }
//
//    fun setUuid(uuid: String?) {
//        this.uuid = uuid!!
//    }



    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name!!
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description!!
    }

    fun getAddress1(): String? {
        return address1
    }

    fun setAddress1(address1: String?) {
        this.address1 = address1!!
    }

    fun getAddress2(): String? {
        return address2
    }

    fun setAddress2(address2: String?) {
        this.address2 = address2!!
    }

    fun getCityVillage(): String? {
        return cityVillage
    }

    fun setCityVillage(cityVillage: String?) {
        this.cityVillage = cityVillage!!
    }

   fun getStateProvince(): String? {
        return stateProvince
    }

    fun setStateProvince(stateProvince: String?) {
        this.stateProvince = stateProvince!!
    }

    fun getCountry(): String? {
        return country
    }

    fun setCountry(country: String?) {
        this.country = country!!
    }


    fun getAddress3(): String? {
        return address3
    }

    fun setAddress3(address3: String) {
        this.address3 = address3
    }


    fun getPrimaryContact(): String? {
        return this.primaryContact
    }

    fun setPrimaryContact(primaryContact: String) {
        this.primaryContact = primaryContact
    }
}