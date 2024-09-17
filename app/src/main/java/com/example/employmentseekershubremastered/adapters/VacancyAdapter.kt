package com.example.employmentseekershubremastered.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.databinding.ItemRvVacancyBinding
import com.example.employmentseekershubremastered.model.dto.main.VacancyDto

class VacancyAdapter (): RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {
    private var vacancies: List<VacancyDto> = emptyList()
    private var onItemClick: ((VacancyDto) -> Unit)? = null
    inner class VacancyViewHolder(binding: ItemRvVacancyBinding): RecyclerView.ViewHolder(binding.root) {
        val tvVacancyTitle = binding.tvVacancyTitle
        val tvCompanyTitle = binding.tvCompanyTitle
        val tvCountCandidates = binding.tvCountCandidates
        val llTags: LinearLayout = binding.llTags
        val tvDescription = binding.tvDescription
        val tvSalary = binding.tvSalary
        val tvPostedTime = binding.tvPostedTime

        /** Вызываем блок инициализации при создании нашего ViewHolder, в котором делаем установку клика на корневой элемент */
        init {
            binding.root.setOnClickListener {
                /** Узнаем текущую позицию элемента. Это значение обновляется динамически, так как есть подгрузка элементов. */
                val vacancyPosition = adapterPosition
                /** Проверка позиции элемента на валидность: не была ли она удалена + существовала ли она */
                if (vacancyPosition != RecyclerView.NO_POSITION)
                    /** Вызываем переданный обработчик клика и передаем в него нашу выбранную вакансию */
                    onItemClick?.invoke(vacancies[vacancyPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemRvVacancyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancyData = vacancies[position]

        holder.tvVacancyTitle.text = vacancyData.vacancyTitle
        holder.tvCompanyTitle.text = vacancyData.companyTitle
        holder.tvCountCandidates.text = "${vacancyData.countCandidates.toString()} applicants"
        holder.tvDescription.text = vacancyData.description
        holder.tvSalary.text = "${vacancyData.salary.amount}$ per month"
        holder.tvPostedTime.text = vacancyData.postedTime

    }

    override fun getItemCount(): Int = vacancies.size

    /** Метод, который обновляет данные для отрисовки в адаптере */
    fun update(data: List<VacancyDto>) {
        vacancies = data
        notifyDataSetChanged()
    }

    /** Метод, который устанавливает кликер для вакансии, логику которого мы передаем как функцию в настройке адаптера в VacanciesFragment */
    fun setOnItemClickListener(listener: (VacancyDto) -> Unit) {
        onItemClick = listener
    }
}