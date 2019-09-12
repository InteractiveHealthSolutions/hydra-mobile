package ihsinformatics.com.hydra_mobile.ui.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.ui.fragments.DynamicFragment


class DynamicFragmentAdapter(fm: FragmentManager, private val mNumOfTabs: Int, var phases: List<Phases>) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val b = Bundle()
        b.putInt("position", position)
        b.putInt("PhaseId", phases[position].id)
        val frag = DynamicFragment.newInstance()
        frag.arguments = b
        return frag
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
