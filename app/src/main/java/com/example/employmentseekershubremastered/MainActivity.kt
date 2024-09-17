package com.example.employmentseekershubremastered

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.employmentseekershubremastered.databinding.ActivityMainBinding
import com.example.employmentseekershubremastered.databinding.ActivityMainWithBottomNavigationBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingWithBottomNavigation: ActivityMainWithBottomNavigationBinding
    private lateinit var viewModel: ViewModel
    private lateinit var navController: NavController
    private val fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        // Инфлейтим binding для макета с реализацией фрагментов через FragmentManager/FragmentTransaction
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Инфлейтим binding для макета с навигацией фрагментов через нижнее меню (с Navigation Jetpack)
        bindingWithBottomNavigation = ActivityMainWithBottomNavigationBinding.inflate(layoutInflater)
        switchToMainInflaterWithNavController()

    // TODO: Временно скипаю авторизацию, пока делаю главный экран
        /** setContentView(binding.root)

         Открываем фрагмент авторизации по умолчанию
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                .add(R.id.authorizationAndRegistrationFragmentContainer, AuthorizationFragment())
                .commit()
        } */
    }


    // Метод, с помощью которого можно переходить между фрагментами
    fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authorizationAndRegistrationFragmentContainer, fragment)
        // Проверяем true/false - и от этого решаем, будет ли транзакция сохранена в стек
        if (addToBackStack) fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    /** Метод, с помощью которого мы переходим в главный раздел (фрагменты, связанные с вакансиями) */
    fun switchToMainInflaterWithNavController() {

        // Меняем на макет с отображением фрагментов по навигации снизу
        setContentView(bindingWithBottomNavigation.root)

        // Используем post, чтобы убедиться, что макет полностью инициализирован
        bindingWithBottomNavigation.navHostFragmentContainer.post {
            // Настраиваем navController и BottomNavigationView
            navController = findNavController(bindingWithBottomNavigation.navHostFragmentContainer.id)
            bindingWithBottomNavigation.viewBottomNavigation.setupWithNavController(navController)
        }
    }
}