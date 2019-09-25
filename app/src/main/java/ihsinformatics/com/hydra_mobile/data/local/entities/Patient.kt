package ihsinformatics.com.hydra_mobile.data.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Patient() {

    @PrimaryKey
    lateinit var uuid: String
    lateinit var identifier: String
    lateinit var givenName: String
    var age: Int = 0
    lateinit var gender: String
    lateinit var externalId: String
    lateinit var enrs: String

    constructor(
        identifier: String,
        givenName: String,
        age: Int,
        gender: String,
        externalId: String,
        enrs: String,
        voided: Boolean,
        uuid: String

    ) : this() {
        this.uuid = uuid
        this.age =age
        this.externalId =externalId
        this.enrs = enrs
        this.givenName = givenName
        this.gender = gender
        this.identifier =identifier
    }


}