package com.example.employmentseekershubremastered.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employmentseekershubremastered.R
import com.example.employmentseekershubremastered.adapters.VacancyAdapter
import com.example.employmentseekershubremastered.databinding.FragmentVacanciesBinding
import com.example.employmentseekershubremastered.model.dto.main.SalaryDto
import com.example.employmentseekershubremastered.model.dto.main.VacancyDto


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class VacanciesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentVacanciesBinding
    private  val vacancyAdapter: VacancyAdapter = VacancyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentVacanciesBinding.inflate(inflater)

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Затычка в виде списка вакансий, пока нет общения с сервером

        val vacancies = listOf(
            VacancyDto(
                id = "1",
                vacancyTitle = "Software Engineer",
                companyId = "101",
                companyTitle = "TechCorp",
                countCandidates = 5,
                tags = listOf("Java", "Spring", "Kotlin"),
                description = "We are looking for a Software Engineer with experience in Java and Kotlin.",
                salary = SalaryDto(amount = 5000, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-01",
                isLiked = true
            ),
            VacancyDto(
                id = "2",
                vacancyTitle = "Data Scientist",
                companyId = "102",
                companyTitle = "Data Inc.",
                countCandidates = 8,
                tags = listOf("Python", "Machine Learning", "AI"),
                description = "Looking for a data scientist skilled in Python and ML techniques.",
                salary = SalaryDto(amount = 6000, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-02",
                isLiked = false
            ),
            VacancyDto(
                id = "3",
                vacancyTitle = "DevOps Engineer",
                companyId = "103",
                companyTitle = "OpsWorks",
                countCandidates = 12,
                tags = listOf("Docker", "Kubernetes", "AWS"),
                description = "DevOps Engineer with knowledge of Kubernetes and cloud platforms.",
                salary = SalaryDto(amount = 5500, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-03",
                isLiked = true
            ),
            VacancyDto(
                id = "4",
                vacancyTitle = "UI/UX Designer",
                companyId = "104",
                companyTitle = "DesignStudio",
                countCandidates = 3,
                tags = listOf("Figma", "Sketch", "UI/UX"),
                description = "Creative UI/UX designer with proficiency in Figma and Sketch.",
                salary = SalaryDto(amount = 4000, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-04",
                isLiked = false
            ),
            VacancyDto(
                id = "5",
                vacancyTitle = "Product Manager",
                companyId = "105",
                companyTitle = "ManageTech",
                countCandidates = 7,
                tags = listOf("Agile", "Scrum", "Leadership"),
                description = "Product Manager with experience in Agile methodologies and team leadership.",
                salary = SalaryDto(amount = 6500, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-05",
                isLiked = true
            ),
            VacancyDto(
                id = "6",
                vacancyTitle = "Marketing Specialist",
                companyId = "106",
                companyTitle = "MarketLeads",
                countCandidates = 9,
                tags = listOf("SEO", "Digital Marketing", "Content"),
                description = "Marketing Specialist with expertise in SEO and digital marketing strategies.",
                salary = SalaryDto(amount = 4500, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-06",
                isLiked = false
            ),
            VacancyDto(
                id = "7",
                vacancyTitle = "Backend Developer",
                companyId = "107",
                companyTitle = "CodeFactory",
                countCandidates = 4,
                tags = listOf("Node.js", "Express", "MongoDB"),
                description = "Looking for a backend developer proficient in Node.js and Express.",
                salary = SalaryDto(amount = 5200, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-07",
                isLiked = true
            ),
            VacancyDto(
                id = "8",
                vacancyTitle = "Frontend Developer",
                companyId = "108",
                companyTitle = "WebArt",
                countCandidates = 6,
                tags = listOf("React", "JavaScript", "HTML/CSS"),
                description = "Frontend developer with experience in React and modern web technologies.",
                salary = SalaryDto(amount = 4800, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-08",
                isLiked = false
            ),
            VacancyDto(
                id = "9",
                vacancyTitle = "Fullstack Developer",
                companyId = "109",
                companyTitle = "StackMakers",
                countCandidates = 10,
                tags = listOf("JavaScript", "Node.js", "React"),
                description = "Fullstack developer with strong skills in both frontend and backend technologies.",
                salary = SalaryDto(amount = 6000, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-09",
                isLiked = true
            ),
            VacancyDto(
                id = "10",
                vacancyTitle = "QA Engineer",
                companyId = "110",
                companyTitle = "QualityTesters",
                countCandidates = 11,
                tags = listOf("Testing", "Automation", "Selenium"),
                description = "QA Engineer with experience in automation testing and Selenium.",
                salary = SalaryDto(amount = 4700, inTime = "per month", currency = "USD"),
                postedTime = "2023-08-10",
                isLiked = false
            )

        )
        // Настраиваем поля RecyclerView: layoutManager, adapter
        with(binding.rvVacancies) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = vacancyAdapter
            vacancyAdapter.update(vacancies)
        }
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