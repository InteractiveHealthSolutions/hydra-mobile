package ihsinformatics.com.hydra_mobile.persistentdata.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Users(
    @PrimaryKey(autoGenerate = true)
    val id: Int

)