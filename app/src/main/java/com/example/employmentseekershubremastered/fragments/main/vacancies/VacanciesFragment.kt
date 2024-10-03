package com.example.employmentseekershubremastered.fragments.main.vacancies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.employmentseekershubremastered.FiltersDrawer
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.ViewModel
import com.example.employmentseekershubremastered.adapters.VacancyAdapter
import com.example.employmentseekershubremastered.databinding.FragmentVacanciesBinding
import com.example.employmentseekershubremastered.model.dto.main.VacancyDto
import com.example.employmentseekershubremastered.model.dto.main.filters.PageInfoDto
import com.example.employmentseekershubremastered.model.dto.main.filters.SealedFilterDto
import com.example.employmentseekershubremastered.model.dto.main.filters.VacancyFilterDto
import com.example.employmentseekershubremastered.model.dto.main.filters.VacancyFilterListDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val LOAD_DATA_SIZE = 10  // количество вакансий, которые мне надо отгружать при прокрутке вниз
private const val START_DATA_SIZE = 20  // количество вакансий, которое отрисовывается у меня при открытии этого фрагмента


class VacanciesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentVacanciesBinding? = null
    private lateinit var viewModel: ViewModel
    private val vacancyAdapter: VacancyAdapter = VacancyAdapter()
    private lateinit var filtersDrawer: FiltersDrawer
    private var searchQuery: String? = null  // Переменная, которая держит в себе значение, которое сейчас в поисковой строке
    private var vacanciesList: MutableList<VacancyDto> = mutableListOf() // Пустой список для вакансий, который потом заполним
    // Переменные для управления загрузкой новых вакансий
    private var isLoading = false
    private var isLastPage = false
    private var page: Int = 0  // Переменная для подгрузки других вакансий со следующих страниц.

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
        filtersDrawer = FiltersDrawer(binding?.nvFilters?.getHeaderView(0)?.findViewById(R.id.llFilters), requireContext(), requireActivity())

        getFilters()
        // Получаем первые 20 вакансий
        sendFilters(VacancyFilterListDto(emptyList(), PageInfoDto(page, START_DATA_SIZE, emptyList())))
    }


    // TODO: Сделать обработку нажатия на фильтры и кнопку применить
    override fun onResume() {
        super.onResume()

        // Обработчик поисковика (сделать только на Sumbit)
        binding?.svVacancies?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query?.trim()
                page = 0
                isLoading = false
                isLastPage = false
                vacanciesList.clear()
                sendFilters(onApplyFilters(START_DATA_SIZE))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText?.trim()
                return true
            }

        })


        // Открываем боковое меню с фильтрами
        binding?.ibFilters?.setOnClickListener {
            binding?.dlFilters?.openDrawer(GravityCompat.START)
        }

        binding?.dlFilters?.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Не нужно обрабатывать
            }

            override fun onDrawerOpened(drawerView: View) {
                val container = drawerView.findViewById<LinearLayout>(R.id.llFilters)
                val applyFiltersButton = container.getChildAt(container.childCount - 1) as Button

                applyFiltersButton.setOnClickListener {
                    page = 0
                    isLoading = false
                    isLastPage = false
                    vacanciesList.clear()
                    val requestFilters = onApplyFilters(START_DATA_SIZE)
                    sendFilters(requestFilters)
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                // Не нужно обрабатывать
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Не нужно обрабатывать
            }
        })
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


            this?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        // Это условие проверяет, виден ли последний элемент в списке.
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            Log.d("VacanciesFragment", "Loading more items")
                            loadMoreVacancies()
                        }
                    }
                }
            })
        }
    }


    /** Метод, который подгружает дополнительные вакансии при прокрутке вниз */
    private fun loadMoreVacancies() {
        isLoading = true
        page += 1
        sendFilters(onApplyFilters(LOAD_DATA_SIZE))
    }


    /** Метод, который отправляет запрос на получения всех фильтров на сервер */
    private fun getFilters() {
        viewModel.apiClient.getVacancyService().getFilters().enqueue(object : Callback<VacancyFilterListDto> {
            override fun onResponse(call: Call<VacancyFilterListDto>, response: Response<VacancyFilterListDto>) {
                if (response.isSuccessful) {
                    filtersDrawer.drawFilters(response.body()!!)
                }
                else {
                    // TODO: Responses
                }
            }

            override fun onFailure(call: Call<VacancyFilterListDto>, t: Throwable) {
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


    /** Метод, который формирует фильтры на текущий момент. Срабатывает при нажатии на кнопку "Применить фильтры" или при нажатии на кнопку "Поиск" (лупа) */
    private fun onApplyFilters(size: Int): VacancyFilterListDto {
        if (page == 0) {
            vacanciesList.clear()
        }

        val filtersList = mutableListOf<VacancyFilterDto>()

        viewModel.selectedCheckBoxFilters.forEach { (title, selectedItems) ->
            if (selectedItems.isNotEmpty()) {
                filtersList.add(
                    VacancyFilterDto(title = title,
                        filters = listOf(SealedFilterDto.CheckBoxFilterDto(
                            type = "checkBox", data = selectedItems)
                        )
                    )
                )
            }
        }

        viewModel.selectedRangeFilter?.let {
            filtersList.add(
                VacancyFilterDto(
                    title = "Salary Range",
                    filters = listOf(
                        SealedFilterDto.RangeFilterDto(
                            type = "range",
                            data = it
                        )
                    )
                )
            )
        }

        if (searchQuery != null && searchQuery != "") {
            filtersList.add(VacancyFilterDto(
                title = "Search",
                filters = listOf(SealedFilterDto.SearchFilterDto(
                    type = "search",
                    data = searchQuery!!)
                ))
            )
        }

        // Формируем финальный объект
        return VacancyFilterListDto(filters = filtersList.toList(), PageInfoDto(page, size, emptyList()))
    }


    /** Метод, который отправляет фильтры на сервер и получает ответ в виде списка вакансий, которые отрисовываются в RecyclerView */
    private fun sendFilters(selectedFilters: VacancyFilterListDto) {
        viewModel.apiClient.getVacancyService().sendFilters(selectedFilters).enqueue(object : Callback<List<VacancyDto>> {
            override fun onResponse(call: Call<List<VacancyDto>>, response: Response<List<VacancyDto>>) {
                if (response.isSuccessful) {
                    if (page == 0) vacanciesList.clear()
                    val newVacancies = response.body() ?: emptyList()
                    vacanciesList.addAll(newVacancies)
                    vacancyAdapter.update(vacanciesList.toList())
                    isLoading = false

                    if (newVacancies.isEmpty() || newVacancies.size < LOAD_DATA_SIZE) isLastPage = true
                }
                else isLastPage = false
            }

            override fun onFailure(call: Call<List<VacancyDto>>, t: Throwable) {
                isLoading = false
            }
        })
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