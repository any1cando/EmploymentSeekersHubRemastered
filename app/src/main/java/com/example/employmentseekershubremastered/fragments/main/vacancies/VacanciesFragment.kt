package com.example.employmentseekershubremastered.fragments.main.vacancies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employmentseekershubremastered.ViewModel
import com.example.employmentseekershubremastered.adapters.VacancyAdapter
import com.example.employmentseekershubremastered.databinding.FragmentVacanciesBinding
import com.example.employmentseekershubremastered.model.dto.main.VacancyDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class VacanciesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentVacanciesBinding? = null
    private lateinit var viewModel: ViewModel
    private val vacancyAdapter: VacancyAdapter = VacancyAdapter()

    // Создаем пока что пустой список для вакансий, который потом заполним.
    private var vacanciesList: List<VacancyDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentVacanciesBinding.inflate(inflater)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        // Настраиваем поля RecyclerView: layoutManager, adapter, а также обработчик нажатия на вакансию
        setupDefaultAdapterSettings()

        // Inflate the layout for this fragment
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getVacancies()
    }


    override fun onResume() {
        super.onResume()

        binding?.ibFilters?.setOnClickListener {
            binding?.dlFilters?.openDrawer(GravityCompat.START)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    /** Метод, который делает настройку адаптера по умолчанию (кликер, layoutManager, adapter) */
    private fun setupDefaultAdapterSettings() {
        vacancyAdapter.setOnItemClickListener { vacancy -> clickOnVacancy(vacancy) }
        with(binding?.rvVacancies) {
            this?.layoutManager = LinearLayoutManager(requireContext())
            this?.adapter = vacancyAdapter
        }
    }


    /** Метод, который отправляет запрос на получение списка вакансий */
    private fun getVacancies() {
        viewModel.apiClient.getVacancyService().getVacancies().enqueue(object : Callback<List<VacancyDto>> {
            override fun onResponse(call: Call<List<VacancyDto>>, response: Response<List<VacancyDto>>) {
                if (response.isSuccessful) {
                    vacanciesList = response.body()!!  // Делаю вызов принудительным с "!!", так как с сервера точно придет какая-то база вакансий.
                    vacancyAdapter.update(vacanciesList)  // Обновляю список вакансий
                }
            }

            override fun onFailure(call: Call<List<VacancyDto>>, t: Throwable) {
                Log.i("Status:", "OnResponse's fail")
                Log.i("Error:", t.message.toString())
                Toast.makeText(requireContext(), "Some error occurred with the server", Toast.LENGTH_LONG).show()
            }

        })
    }


    /** Метод, который описывает поведение клика на вакансию */
    private fun clickOnVacancy(vacancy: VacancyDto) {
        /** VacanciesFragmentDirections - автоматически генирируемый класс, который создается после подключения 'SafeArgs'.
        Вызываем метод 'action...', который я указал в навигации, и передаем туда 'vacancy',
        так как в навигации мы указали, что должен прийти аргумент формата VacancyDto */
        val action = VacanciesFragmentDirections.actionVacanciesFragmentToDetailedVacancyFragment(vacancy)
        findNavController().navigate(action)
    }


    companion object {
        @JvmStatic fun newInstance(param1: String, param2: String) =
                VacanciesFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}