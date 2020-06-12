package ihsinformatics.com.hydra_mobile.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ihsinformatics.com.hydra_mobile.HydraApp
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.ComponentFormsObject
import ihsinformatics.com.hydra_mobile.data.repository.ComponentFormJoinRepository
import ihsinformatics.com.hydra_mobile.data.repository.ComponentRepository
import ihsinformatics.com.hydra_mobile.data.repository.FormRepository
import ihsinformatics.com.hydra_mobile.ui.adapter.PhaseComponentAdapter
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PhaseComponentJoinViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import kotlinx.android.synthetic.main.dynamic_fragment_layout.view.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.find


class DynamicFragment : BaseFragment() {

    private lateinit var componentFormsObjectList: ArrayList<ComponentFormsObject>
    var adapter = PhaseComponentAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dynamic_fragment_layout, container, false)
        val bundle = this.arguments
        val phaseId = bundle!!.getString("PhaseUUID", "null")
        initViews(view, phaseId)
        return view
    }

    @SuppressLint("WrongConstant") private fun initViews(view: View, phaseId: String) {
        componentFormsObjectList = ArrayList()
        getPhases(phaseId)
        val recyclerView = view.rv_phase_container as RecyclerView
        val swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)

        recyclerView.setHasFixedSize(true)
        //adapter = PhaseComponentAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            Toast.makeText(activity, "Refreshed", Toast.LENGTH_SHORT).show()
            swipeContainer.isRefreshing = false
        })

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light)


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

    private fun getPhases(phaseId: String) {

        val phasesComponentJoinViewModel = ViewModelProviders.of(this).get(PhaseComponentJoinViewModel::class.java)
        val formModel = FormRepository(HydraApp.context!!)


        runBlocking {


            var currentWorkflowUUID = GlobalPreferences.getinstance(context).findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, "-1")
             var componentResults = phasesComponentJoinViewModel.getComponentsByPhaseandWorkflow(phaseId, currentWorkflowUUID)

            for (i in componentResults) {
                var componentObj = phasesComponentJoinViewModel.getComponentByComponentUUID(i.componentUUID)
                var forms = formModel.getAllFormsByFilter(i.componentUUID, phaseId, currentWorkflowUUID)

                componentFormsObjectList.add(ComponentFormsObject(componentObj.name, componentObj.uuid, forms))
            }


        }
        adapter.setPhaseComponentList(componentFormsObjectList)

    }




companion object {
    fun newInstance(): DynamicFragment {
        return DynamicFragment()
    }
}
}
