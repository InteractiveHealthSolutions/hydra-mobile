package ihsinformatics.com.hydra_mobile.ui.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.ui.fragments.DynamicFragment


class DynamicFragmentAdapter(
    fm: FragmentManager,
    private val mNumOfTabs: Int,
    var workflowPhasesList: List<WorkflowPhasesMap>
) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val b = Bundle()
        b.putInt("position", position)
        b.putInt("PhaseId", 101)
        // b.putInt("PhaseId", workflowPhasesList[position].id)
        val frag = DynamicFragment.newInstance()
        frag.arguments = b
        return frag
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
