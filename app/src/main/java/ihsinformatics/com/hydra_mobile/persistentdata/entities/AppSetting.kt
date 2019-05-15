package ihsinformatics.com.hydra_mobile.persistentdata.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class AppSetting(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ip: String,
    val port: String,
    val ssl: Boolean
)