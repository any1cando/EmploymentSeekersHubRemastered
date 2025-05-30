package com.example.employmentseekershubremastered.fragments.entry.point

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.employmentseekershubremastered.ViewModel
import com.example.employmentseekershubremastered.MainActivity
import com.example.employmentseekershubremastered.SessionManager
import com.example.employmentseekershubremastered.databinding.FragmentAuthorizationBinding
import com.example.employmentseekershubremastered.model.dto.entry.point.UserAuthorizationRequest
import com.example.employmentseekershubremastered.model.dto.entry.point.UserTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class AuthorizationFragment : Fragment() {

    private var binding: FragmentAuthorizationBinding? = null
    private lateinit var viewModel: ViewModel
    private lateinit var sessionManager: SessionManager


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sessionManager = SessionManager(requireContext())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentAuthorizationBinding.inflate(inflater)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        restoreAuthDataWithViewModel()
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            // Делаю обработчик нажатия на текст "Sign in!", то есть перехожу на фрагмент регистрации.
            tvRegistration.setOnClickListener {
                (activity as MainActivity).navigateToFragment(RegistrationFragment(), true)
            }

            // Обрабатываем изменения в полях авторизации и добавляем их в ViewModel.
            etLogin.doOnTextChanged { textEmail, _, _, _ ->
                viewModel.emailAuthorization = textEmail.toString()
            }
            etPassword.doOnTextChanged { textPassword, _, _, _ ->
                viewModel.passwordAuthorization = textPassword.toString()
            }

            // Делаю обработчик нажатия на кнопку "Log in", то есть авторизации.
            btnAuthorization.setOnClickListener {
                authRequest()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    /** Метод, который возвращает из ViewModel данные об авторизации в поля фрагмента. */
    private fun restoreAuthDataWithViewModel() {
        binding?.etLogin?.setText(viewModel.emailAuthorization)
        binding?.etPassword?.setText(viewModel.passwordRegistration)
    }


    /** Метод, который отправляет запрос на авторизацию */
    private fun authRequest() {
        val authorizationInfo: UserAuthorizationRequest = UserAuthorizationRequest(
            email = viewModel.emailAuthorization.toString().trim(),
            password = viewModel.passwordAuthorization.toString().trim()
        )

        viewModel.apiClient.getAuthAndRegService().performAuthorization(authorizationInfo).enqueue(object : Callback<UserTokenResponse> {
            override fun onResponse(call: Call<UserTokenResponse>, response: Response<UserTokenResponse>) {
                if (response.isSuccessful) {  // Запрос успешный, код 200

                    sessionManager.saveAccessToken(response.body()?.accessToken)
                    sessionManager.saveRefreshToken(response.body()?.refreshToken)

                    // Очищаем поля VIewModel для авторизации после успешного запроса авторизации
                    deleteAuthAndRegDataWithViewModel()

                    // Переходим на главный раздел с навигацией
                    parentFragmentManager.popBackStack()
                    (activity as MainActivity).switchToMainInflaterWithNavController()
                }
                else {
                    when (response.code()) {
                        400 -> { Toast.makeText(requireContext(), "Error 400", Toast.LENGTH_SHORT).show() }
                        else -> { Toast.makeText(requireContext(), "Error 500", Toast.LENGTH_SHORT).show() }
                    }
                }
            }

            override fun onFailure(call: Call<UserTokenResponse>, t: Throwable) {
                Log.i("Status:", "OnResponse's fail")
                Log.i("Error:", t.message.toString())
                Toast.makeText(requireContext(), "Some error occurred with the internet", Toast.LENGTH_LONG).show()
            }

        })
    }


    /** Метод, который уничтожает все данные в ViewModel */
    private fun deleteAuthAndRegDataWithViewModel() {
        viewModel.firstNameRegistration = null
        viewModel.lastNameRegistration = null
        viewModel.emailRegistration = null
        viewModel.passwordRegistration = null
        viewModel.emailAuthorization = null
        viewModel.passwordAuthorization = null
    }
}