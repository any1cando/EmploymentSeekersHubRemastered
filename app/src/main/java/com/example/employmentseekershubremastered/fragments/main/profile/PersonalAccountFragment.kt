package com.example.employmentseekershubremastered.fragments.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.databinding.FragmentDetailedVacancyBinding
import com.example.employmentseekershubremastered.databinding.FragmentPersonalAccountBinding


class PersonalAccountFragment : Fragment() {

    private var _binding: FragmentPersonalAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPersonalAccountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.tvEditData.setOnClickListener {
            val action = PersonalAccountFragmentDirections.actionPersonalAccountFragmentToEdtiProfileDataFragment()
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}