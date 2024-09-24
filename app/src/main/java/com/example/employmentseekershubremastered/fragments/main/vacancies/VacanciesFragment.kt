package com.example.employmentseekershubremastered.fragments.main.vacancies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employmentseekershubremastered.FiltersDrawer
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.ViewModel
import com.example.employmentseekershubremastered.adapters.VacancyAdapter
import com.example.employmentseekershubremastered.databinding.FragmentVacanciesBinding
import com.example.employmentseekershubremastered.model.dto.main.VacancyDto
import com.example.employmentseekershubremastered.model.dto.main.filters.VacancyFilterListDto
import okhttp3.internal.wait
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
    private lateinit var filtersDrawer: FiltersDrawer

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
        // Создали инстанс класса, у которого есть методы отрисовки фильтров и Нашли LinearLayout, в который надо отрисовывать фильтры.
        filtersDrawer = FiltersDrawer(binding?.nvFilters?.getHeaderView(0)?.findViewById(R.id.llFilters), requireContext())

        // Получаем первые 20 вакансий
        getVacancies()

        getFilters()
    }


    // TODO: Сделать обработку нажатия на фильтры и кнопку применить
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


    /** Метод, который отправляет запрос на получения всех фильтров на сервер */
    private fun getFilters() {
        viewModel.apiClient.getVacancyService().getFilters().enqueue(object : Callback<VacancyFilterListDto> {
            override fun onResponse(call: Call<VacancyFilterListDto>, response: Response<VacancyFilterListDto>) {
                if (response.isSuccessful) {
                    filtersDrawer.drawFilters(response.body()!!)
                }
            }

            override fun onFailure(call: Call<VacancyFilterListDto>, t: Throwable) {
                Log.i("Status:", "OnResponse's fail")
                Log.i("Error:", t.message.toString())
                Toast.makeText(requireContext(), "Some error occurred with the internet", Toast.LENGTH_LONG).show()
            }

        })
    }


    /** Метод, который отправляет запрос на получение списка вакансий */
    private fun getVacancies() {
        viewModel.apiClient.getVacancyService().getVacancies().enqueue(object : Callback<List<VacancyDto>> {
            override fun onResponse(call: Call<List<VacancyDto>>, response: Response<List<VacancyDto>>) {
                if (response.isSuccessful) {
                    vacanciesList = response.body()!!  // Делаю вызов принудительным с "!!", так как с сервера точно придет какая-то база вакансий.
                    vacancyAdapter.update(vacanciesList)  // Обновляю список вакансий
                }
                else {
                    when (response.code()) {
                        400 -> println("TODO")  // Сделать повторную отправку метода
                        else -> println("TODO")
                    }
                }
            }

            override fun onFailure(call: Call<List<VacancyDto>>, t: Throwable) {
                Log.i("Status:", "OnResponse's fail")
                Log.i("Error:", t.message.toString())
                Toast.makeText(requireContext(), "Some error occurred with the internet", Toast.LENGTH_LONG).show()
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