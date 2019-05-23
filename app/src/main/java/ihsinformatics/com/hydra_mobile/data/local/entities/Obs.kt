package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey

class Obs(
    val conceptName: String,
    val value: String,
    val voided: Boolean


    ):AbstractModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}