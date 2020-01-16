package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User() {
    lateinit var username: String
    lateinit var password: String

    constructor(
        username: String
//        fullName:String,
//        systemId: String,
//        retired:Boolean,
//        uuid: String

    ) : this() {
        this.username = username
    }

    constructor(username: String, password: String) : this() {
        this.password = password
        this.username = username
    }


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}