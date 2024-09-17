package com.example.employmentseekershubremastered.fragments.main.vacancies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.databinding.FragmentDetailedVacancyBinding
import com.example.employmentseekershubremastered.databinding.FragmentVacanciesBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DetailedVacancyFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentDetailedVacancyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailedVacancyBinding.inflate(inflater)

        val args: DetailedVacancyFragmentArgs by navArgs()
        val vacancy = args.vacancy

        // TODO: Сделать отображение информации из переменной 'vacancy' в соответствующие поля


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailedVacancyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}