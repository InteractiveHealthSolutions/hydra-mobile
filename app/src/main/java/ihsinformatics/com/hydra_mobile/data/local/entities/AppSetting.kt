package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class AppSetting(
    val ip: String,
    val port: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}