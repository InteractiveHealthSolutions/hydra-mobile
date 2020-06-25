package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class Person{

    private var age: Int =0
    private var dead: Boolean =false
    private lateinit var display: String
    private lateinit var gender: String
    private var birthdate: String=" "
    private lateinit var attributes:List<PersonAttribute>



    constructor(a:Int, d:Boolean,dis:String,g:String,birth:String, attributes:List<PersonAttribute>)
    {
        this.age=a
        this.dead=d
        this.display=dis
        this.gender=g
        this.birthdate=birth
        this.attributes=attributes
    }


    fun setAge(a:Int)
    {
        this.age=a
    }

    fun setDead(dead:Boolean)
    {
        this.dead=dead
    }
    fun setGender(gender:String)
    {
        this.gender=gender
    }
    fun setDisplay(display:String)
    {
        this.display=display
    }

    fun getBirthDate():String{
        return birthdate
    }

    fun setBirthDate(b:String)
    {
        birthdate=b
    }

    fun getAge():Int
    {
        return age
    }

    fun getDead():Boolean
    {
        return dead
    }

    fun getGender():String
    {
        return gender
    }

    fun getDisplay():String
    {
        return display
    }

    fun getAttributes():List<PersonAttribute>
    {
        return attributes
    }



}