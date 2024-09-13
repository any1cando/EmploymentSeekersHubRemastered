package com.example.employmentseekershubremastered

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.employmentseekershubremastered.databinding.ActivityMainBinding
import com.example.employmentseekershubremastered.databinding.ActivityMainWithBottomNavigationBinding
import com.example.employmentseekershubremastered.fragments.entry.point.AuthorizationFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingWithBottomNavigation: ActivityMainWithBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инфлейтим binding для макета с реализацией фрагментов через FragmentManager/FragmentTransaction
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инфлейтим binding для макета с навигацией фрагментов через нижнее меню (с Navigation Jetpack)
        bindingWithBottomNavigation = ActivityMainWithBottomNavigationBinding.inflate(layoutInflater)

        // Открываем фрагмент авторизации по умолчанию
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.authorizationAndRegistrationFragmentContainer, AuthorizationFragment())
                .commit()
        }

    }


    // Метод, с помощью которого можно переходить между фрагментами
    // По умол
    fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authorizationAndRegistrationFragmentContainer, fragment)
        // Проверяем true/false - и от этого решаем, будет ли транзакция сохранена в стек
        if (addToBackStack) fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}