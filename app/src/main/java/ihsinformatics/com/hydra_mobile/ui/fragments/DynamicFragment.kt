package ihsinformatics.com.hydra_mobile.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.PhasesComponent
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.ComponentFormsObject
import ihsinformatics.com.hydra_mobile.ui.adapter.PhaseComponentAdapter
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PhasesViewModel
import kotlinx.android.synthetic.main.dynamic_fragment_layout.view.*


class DynamicFragment : Fragment() {

    private lateinit var componentFormsObjectList: ArrayList<ComponentFormsObject>
    lateinit var adapter: PhaseComponentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dynamic_fragment_layout, container, false)
        val bundle = this.arguments
        val phaseId = bundle!!.getInt("PhaseId", 0)
        initViews(view, phaseId)
        return view
    }

    @SuppressLint("WrongConstant")
    private fun initViews(view: View, phaseId: Int) {
        componentFormsObjectList = ArrayList()
        getPhases(phaseId)
        val recyclerView = view.rv_phase_container as RecyclerView
        recyclerView.setHasFixedSize(true)
        adapter = PhaseComponentAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getPhases(phaseId: Int) {

        val componentList = ViewModelProviders.of(this).get(PhasesViewModel::class.java)
        componentList.getComponentByPhaseId(phaseId).observe(this, Observer<List<Component>> {
            for (i in it.indices) {
                val componentId = it[i].id
                val componentName = it[i].name
                val formModel = ViewModelProviders.of(this).get(PhasesViewModel::class.java)
                formModel.getAllComponentForms().observe(this, Observer<List<ComponentForm>> { formList ->
                    for (j in formList.indices) {
                        if (componentId == formList[j].component.id) {
                            componentFormsObjectList.add(
                                ComponentFormsObject(
                                    componentName,
                                    componentId,
                                    formList[j].formList
                                )
                            )
                        }
                    }

                    adapter.setPhaseComponentList(componentFormsObjectList)
                })

                // adapter.setPhaseComponentList(componentFormsObjectList)
            }
        })
    }

    companion object {
        fun newInstance(): DynamicFragment {
            return DynamicFragment()
        }
    }
}
