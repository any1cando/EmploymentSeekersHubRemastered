package com.example.employmentseekershubremastered

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.employmentseekershubremastered.model.dto.main.filters.CheckBoxDto
import com.example.employmentseekershubremastered.model.dto.main.filters.RangeDto
import com.example.employmentseekershubremastered.model.dto.main.filters.SealedFilterDto
import com.example.employmentseekershubremastered.model.dto.main.filters.VacancyFilterListDto
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider

class FiltersDrawer(private val container: LinearLayout?, private val context: Context) {


    /** Метод, в который передается объект, состоящий из всех фильтров и который отрисовывает все фильтры в LinearLayout в выдвижном меню */
    fun drawFilters(responseObjectFilters: VacancyFilterListDto) {
        responseObjectFilters.filters.forEach { filtersGroup ->
            // Делаем заголовок для текущей группы фильтров
            val titleGroup = drawTitleGroup(filtersGroup.title)
            container?.addView(titleGroup)

            filtersGroup.filters.forEach { oneOfFilters ->
                when (oneOfFilters) {
                    is SealedFilterDto.CheckBoxFilterDto -> {
                        drawCheckBoxGroup(oneOfFilters.data)
                    }
                    is SealedFilterDto.RangeFilterDto -> {
                        drawRangeSalary(oneOfFilters.data)
                    }
                    is SealedFilterDto.SearchFilterDto -> {
                        // TODO: Что делать? :)
                    }
                }

            }
        }
    }


    /** Метод, который создает TextView как заголовое для группы фильтров */
    private fun drawTitleGroup(title: String): TextView {
        val titleTextView = TextView(container?.context).apply {
            text = title
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        return titleTextView
    }


    /** Метод, который создает группу из CheckBox */
    private fun drawCheckBoxGroup(data: List<CheckBoxDto>) {
        data.forEach { checkBoxDto ->
            val checkBox = CheckBox(context).apply {
                text = checkBoxDto.title
            }
            container?.addView(checkBox)
        }
    }


    /** Метод, который создает ползунок для зарплаты Slider */
    // TODO: Доделать логику этого метода
    private fun drawRangeSalary(data: RangeDto) {
        val rangeSlider = RangeSlider(context).apply {
            // Устанавливаем минимальное и максимальное значения диапазона
            valueFrom = 0.toFloat()
            valueTo = 2000000.toFloat()

            // Убедимся, что выбранные значения попадают в допустимый диапазон
            val startValue = data.from.coerceIn(0, 2000000).toFloat()
            val endValue = data.to.coerceIn(0, 2000000).toFloat()

            // Устанавливаем начальные значения ползунков (выбранный диапазон)
            values = listOf(startValue, endValue)

            // Задаем шаг изменения значений (например, 100)
            stepSize = 100f

            // Форматируем отображение значений
            setLabelFormatter { value ->
                "${value.toInt()}$"
            }
        }

        // Добавляем созданный RangeSlider в контейнер
        container?.addView(rangeSlider)

        // Добавляем обработчик изменения значений ползунков
        rangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val selectedFrom = values[0]
            val selectedTo = values[1]

            // Используйте selectedFrom и selectedTo для фильтрации вакансий
        }
    }




}