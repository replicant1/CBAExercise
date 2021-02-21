package com.bailey.rod.cbaexercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bailey.rod.cbaexercise.databinding.FragmentAtmOnMapBinding
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AtmOnMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AtmOnMapFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAtmOnMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val atmJson = AtmOnMapFragmentArgs.fromBundle(it).atmJson
            Timber.d("*******************************************")
            Timber.d("** Retrieved atmJson parameter = ${atmJson}")
            Timber.d("*******************************************")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("*** Into AtmOnMapFragment.onCreateView ***")
        binding = FragmentAtmOnMapBinding.inflate(layoutInflater, container, false)
        binding.btnMap.setOnClickListener {
            val action =
                AtmOnMapFragmentDirections.actionAtmOnMapFragmentToAccountOverviewFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AtmOnMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AtmOnMapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}