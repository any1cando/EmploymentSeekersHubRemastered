package com.example.employmentseekershubremastered

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.employmentseekershubremastered.model.dto.main.filters.CheckBoxDto
import com.example.employmentseekershubremastered.model.dto.main.filters.RangeDto
import com.example.employmentseekershubremastered.model.dto.main.filters.SealedFilterDto
import com.example.employmentseekershubremastered.model.dto.main.filters.VacancyFilterListDto
import com.google.android.material.slider.RangeSlider

class FiltersDrawer(private val container: LinearLayout?, private val context: Context, viewModelStoreOwner: ViewModelStoreOwner) {

    private val viewModel = ViewModelProvider(viewModelStoreOwner).get(ViewModel::class.java)

    /** Метод, в который передается объект, состоящий из всех фильтров и который отрисовывает все фильтры в LinearLayout в выдвижном меню */
    fun drawFilters(responseObjectFilters: VacancyFilterListDto) {
        responseObjectFilters.filters.forEach { filtersGroup ->
            // Делаем заголовок для текущей группы фильтров
            val titleGroup = drawTitleGroup(filtersGroup.title)
            container?.addView(titleGroup)

            filtersGroup.filters.forEach { oneOfFilters ->
                when (oneOfFilters) {
                    is SealedFilterDto.CheckBoxFilterDto -> {
                        drawCheckBoxGroup(oneOfFilters.data, filtersGroup.title)
                    }
                    is SealedFilterDto.RangeFilterDto -> {
                        drawRangeSalary(oneOfFilters.data)
                    }
                    is SealedFilterDto.SearchFilterDto -> {
                        /** Ничего не делаем, так как к нам приходят только чекбоксы и слайдер для зарплаты
                        А не убираем мы эту проверку, так как в фабрике у нас есть распарс и
                        сборка для Search поисковика, а значит и в блоке 'when' должна быть эта обработка.
                        Можно в принципе сделать проверку на чекбокс и просто 'else' написать, но хз */
                    }
                }
            }
        }
        drawApplyButton()
    }


    /** Метод, который создает TextView как заголовок для группы фильтров */
    private fun drawTitleGroup(title: String): TextView {
        val titleTextView = TextView(container?.context).apply {
            text = title
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        return titleTextView
    }


    /** Метод, который создает группу из CheckBox */
    private fun drawCheckBoxGroup(data: List<CheckBoxDto>, groupName: String) {

        data.forEach { checkBoxDto ->
            val checkBox = CheckBox(context).apply {
                text = checkBoxDto.title
                setOnCheckedChangeListener { _, isChecked ->
                    handleCheckBoxSelection(groupName, checkBoxDto, isChecked)
                }
            }
            container?.addView(checkBox)
        }
    }


    /** Метод, который создает ползунок для зарплаты RangeSlider */
    private fun drawRangeSalary(rangeData: RangeDto) {
        val rangeSlider = RangeSlider(context).apply {
            // Устанавливаем минимальное и максимальное значения диапазона
            valueFrom = rangeData.from.toFloat()
            valueTo = rangeData.to.toFloat()

            // Устанавливаем начальные значения ползунков на максимальные и минимальные значения
            values = listOf(valueFrom, valueTo)

            // Задаем шаг изменения значений
            stepSize = 100f

            // Форматируем отображение значений
            setLabelFormatter { value ->
                "${value.toInt()}$"
            }

            addOnChangeListener { _, _, _ ->
                viewModel.selectedRangeFilter = RangeDto(
                    from = values[0].toInt(),
                    to = values[1].toInt()
                )
            }

        }
        container?.addView(rangeSlider)
    }


    /** Метод, который отрисовывает кнопку "Применить фильтры" */
    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun drawApplyButton() {
        val applyFiltersButton = Button(context).apply {
            text = "Apply Filters"
            setTextColor(ContextCompat.getColor(context, R.color.mainWhite))
            setBackgroundColor(ContextCompat.getColor(context, R.color.blueActive))

            isAllCaps = false
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER // Центрируем кнопку
                setMargins(0, 32, 0, 0) // Задаем отступы
            }
            setPadding(32, 16, 32, 16)
        }
        container?.addView(applyFiltersButton)
    }


    /** Метод (слушатель), который проверяет состояние нажатия на кнопку и от этого уже либо добавляет его в список, либо удаляет*/
    private fun handleCheckBoxSelection(groupName: String, checkBoxDto: CheckBoxDto, isChecked: Boolean) {
        // Метод getOrPut проверяет, существует ли уже список фильтров для данной группы.
        // Если нет, он создает новый пустой список.
        val currentFilterList = viewModel.selectedCheckBoxFilters.getOrPut(groupName) { mutableListOf() }

        if (isChecked) currentFilterList.add(checkBoxDto)  // Если CheckBox выбран, добавляем его в список

        else { currentFilterList.remove(checkBoxDto) // Если CheckBox отменен, удаляем его из списка
            // Если после удаления список пуст, удаляем запись из карты
            if (currentFilterList.isEmpty()) {
                viewModel.selectedCheckBoxFilters.remove(groupName)
            }
        }
    }
}