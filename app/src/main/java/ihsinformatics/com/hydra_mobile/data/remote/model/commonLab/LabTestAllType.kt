package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LabTestAllType(
    val display: String,
    val name: String,
    val testGroup: String,
    @PrimaryKey val uuid: String
)