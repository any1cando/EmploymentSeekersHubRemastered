package com.example.employmentseekershubremastered.fragments.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.employmentseekershubremastered.databinding.FragmentEditProfileDataBinding


class EditProfileDataFragment : Fragment() {

    private var _binding: FragmentEditProfileDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentEditProfileDataBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.btnConfirmEditData.setOnClickListener {

        }
    }


    fun changeData() {

    }

}