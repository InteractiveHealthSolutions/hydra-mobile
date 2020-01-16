package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LabTestType(
//    @Nullable val description: String,
    val display: String,
    val name: String,
//    @Nullable val requiresSpecimen: Boolean,
//    @Nullable val resourceVersion: String,
//    @Nullable val shortName: String,
    val testGroup: String,
    @PrimaryKey val uuid: String
)