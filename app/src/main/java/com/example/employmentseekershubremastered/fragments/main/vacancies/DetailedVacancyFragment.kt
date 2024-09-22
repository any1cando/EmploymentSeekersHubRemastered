package com.example.employmentseekershubremastered.fragments.main.vacancies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.ViewModel
import com.example.employmentseekershubremastered.adapters.VacancyAdapter
import com.example.employmentseekershubremastered.databinding.FragmentDetailedVacancyBinding
import com.example.employmentseekershubremastered.databinding.FragmentVacanciesBinding
import com.example.employmentseekershubremastered.model.dto.main.VacancyDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DetailedVacancyFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentDetailedVacancyBinding? = null
    private lateinit var viewModel: ViewModel
    private lateinit var vacancyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailedVacancyBinding.inflate(inflater)
        // Прячем кнопку, пока не прогрузится основная информация
        binding?.btnRespond?.visibility = View.GONE
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        // Получаем с помощью SafeArgs айди вакансии [Plugins + Parcelize]
        val args: DetailedVacancyFragmentArgs by navArgs()
        vacancyId = args.vacancy.id

        // Inflate the layout for this fragment
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCertainVacancy(vacancyId)

        // TODO: Сделать логику клика на кнопку "Respond"
        binding?.btnRespond?.setOnClickListener {

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    /** Метод, который позволяет получить информацию об определенной вакансии и сразу отрисовать ее в нужные поля */
    private fun getCertainVacancy(vacancyId: String) {
        viewModel.apiClient.getVacancyService().getVacancyById(vacancyId).enqueue(object : Callback<VacancyDto> {
            override fun onResponse(call: Call<VacancyDto>, response: Response<VacancyDto>) {
                if (response.isSuccessful) {
                    response.body()?.apply {
                        binding?.tvVacancyTitleDetailed?.text = vacancyTitle
                        binding?.tvCompanyTitleDetailed?.text = companyTitle
                        // Проверка: если список с тегами НЕ пуст - добавляем теги в LinearLayout
                        if (binding?.llTagsDetailed?.isNotEmpty() == true) addTags(response.body()!!.tags)
                        // Иначе: скрываем LinearLayout
                        else binding?.llTagsDetailed?.visibility = View.GONE
                        binding?.tvPostedTimeDetailed?.text = "Posted time $postedTime"
                        binding?.tvDescriptionDetailed?.text = "Description:"
                        binding?.tvDescriptionTextDetailed?.text = description
                        binding?.tvSalaryDetailed?.text = "${salary.amount} ${salary.currency} per ${salary.inTime}"
                        // Снова показываем кнопку, так как вся информация прогрузилась
                        binding?.btnRespond?.visibility = View.VISIBLE
                    }
                }
                // TODO: Сделать разные обработки с флагом на обновления фрагмента со всеми вакансиями
                else {
                    Toast.makeText(requireContext(), "Something's wrong with this vacancy. Try to refresh all vacancies", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
            }

            override fun onFailure(call: Call<VacancyDto>, t: Throwable) {
                Log.i("Status:", "OnResponse's fail")
                Log.i("Error:", t.message.toString())
                Toast.makeText(requireContext(), "Some error occurred with the server", Toast.LENGTH_LONG).show()
            }

        })
    }


    /** Метод, который заполняет теги в LinearLayout соответствующим списком 'vacancyTags' */
    private fun addTags(vacancyTags: List<String?>) {
        // Очистка существующих на текущий момент тегов в LinearLayout
        binding?.llTagsDetailed?.removeAllViews()
        // Добавление тегов
        for (tag in vacancyTags) {
            val tagView = LayoutInflater.from(binding?.root?.context)
                .inflate(R.layout.item_tag_vacancy, binding?.llTagsDetailed, false) as TextView
            tagView.text = tag
            binding?.llTagsDetailed?.addView(tagView)
        }
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