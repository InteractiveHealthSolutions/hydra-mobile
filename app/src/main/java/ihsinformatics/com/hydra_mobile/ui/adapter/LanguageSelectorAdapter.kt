package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ihsinformatics.com.hydra_mobile.R


class LanguageSelectorAdapter(val context: Context, var languages: Array<String>, var flags: Array<Int>) : BaseAdapter() {


    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.language_selector_drop_down, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }


        vh.languageName.text = languages.get(position)
        vh.languageFlag.setImageDrawable(context.getDrawable(flags.get(position)))
        return view
    }

    override fun getItem(position: Int): Any? {

        return null

    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun getCount(): Int {
        return languages.size
    }

    private class ItemRowHolder(row: View?) {

        val languageName: TextView
        val languageFlag: ImageView


        init {
            this.languageName = row?.findViewById(R.id.languageName) as TextView
            this.languageFlag = row?.findViewById(R.id.languageFlag) as ImageView

        }
    }
}