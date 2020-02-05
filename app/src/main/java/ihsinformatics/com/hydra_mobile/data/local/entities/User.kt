package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class User() {
    lateinit var username: String
    lateinit var password: String
    lateinit var provider: String


    constructor(
        username: String,
        password: String,
        provider: String
    ) : this() {
        this.username = username
        this.password=password
        this.provider=provider
    }

    constructor(username: String, password: String) : this() {
        this.password = password
        this.username = username
    }


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}