package com.example.employmentseekershubremastered.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.databinding.ItemRvVacancyBinding
import com.example.employmentseekershubremastered.model.dto.main.VacancyDto

class VacancyAdapter: RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {
    private var vacancies: List<VacancyDto> = emptyList()
    inner class VacancyViewHolder(binding: ItemRvVacancyBinding): RecyclerView.ViewHolder(binding.root) {
        val tvVacancyTitle = binding.tvVacancyTitle
        val ibtnLike: ImageButton = binding.ibtnLike
        val tvCompanyTitle = binding.tvCompanyTitle
        val tvCountCandidates = binding.tvCountCandidates
        val llTags: LinearLayout = binding.llTags
        val tvDescription = binding.tvDescription
        val tvSalary = binding.tvSalary
        val tvPostedTime = binding.tvPostedTime
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
        holder.tvCountCandidates.text = vacancyData.countCandidates.toString()
        holder.tvDescription.text = vacancyData.description
        holder.tvSalary.text = vacancyData.salary.amount.toString()
        holder.tvPostedTime.text = "Posted ${vacancyData.postedTime}"

        if (vacancyData.isLiked) holder.ibtnLike.setImageResource(R.drawable.ic_vector_empty_like_red)
        else holder.ibtnLike.setImageResource(R.drawable.ic_vector_empty_like)
    }

    override fun getItemCount(): Int = vacancies.size

    fun update(data: List<VacancyDto>) {
        vacancies = data
        notifyDataSetChanged()
    }
}