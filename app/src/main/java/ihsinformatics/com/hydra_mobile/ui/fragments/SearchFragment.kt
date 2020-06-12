package ihsinformatics.com.hydra_mobile.ui.fragments


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation

import ihsinformatics.com.hydra_mobile.R
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.math.BigDecimal


class SearchFragment : Fragment(), View.OnClickListener {


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_search -> {
                navController.navigate(R.id.action_searchFragment_to_searchResultFragment)
            }
        }
    }

    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        view.btn_search.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}
