package ihsinformatics.com.hydra_mobile.widgets

import android.view.View

abstract class Widgets
{
    abstract fun getView(): View
    abstract fun getID(): String
    abstract fun getmyQuestion(): String
    abstract fun getVisibility(): Boolean
    abstract fun setVisiblity(v:Boolean)
    abstract fun getValue():String


}