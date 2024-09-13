package com.example.employmentseekershubremastered.fragments.entry.point

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.employmentseekershubremastered.MainActivity
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.databinding.FragmentAuthorizationBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AuthorizationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorizationBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Делаю обработчик нажатия на текст "Sign in!", то есть перехожу на фрагмент регистрации.
        binding.tvRegistration.setOnClickListener {
            (activity as MainActivity).navigateToFragment(RegistrationFragment())
        }

        // Делаю обработчик нажатич на кнопку "Log in", то есть авторизации.
        binding.btnAuthorization.setOnClickListener {
//            try {
//
//            } catch (e: Exception) {
//
//            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthorizationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}