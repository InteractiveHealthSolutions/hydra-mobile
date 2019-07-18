package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Obs(
    val conceptName: String,
    val value: String,
    val creator:Long,
   // val user: User,
    val encounterId:Long,
   // val encounter:Encounter,
    val conceptId:Long,
   // val concept:Concept,
    val voided: Boolean,
    val uuid:String
    //Todo: relation mapping

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}