package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
class User() {
    lateinit var userUUID: String
    lateinit var username: String
    lateinit var password: String
    lateinit var provider: String


    constructor(
        userUUID: String,
        username: String,
        password: String,
        provider: String
    ) : this() {
        this.userUUID=userUUID
        this.username = username
        this.password=password
        this.provider=provider
    }

    constructor( userUUID: String, username: String, password: String) : this() {
        this.userUUID = userUUID
        this.password = password
        this.username = username
    }


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}