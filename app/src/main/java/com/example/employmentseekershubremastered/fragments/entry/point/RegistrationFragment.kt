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
import com.example.employmentseekershubremastered.SessionManager
import com.example.employmentseekershubremastered.databinding.FragmentRegistrationBinding
import com.example.employmentseekershubremastered.model.dto.entry.point.UserRegistrationRequest
import com.example.employmentseekershubremastered.model.dto.entry.point.UserTokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.regex.Pattern

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistrationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentRegistrationBinding? = null
    private lateinit var viewModel: ViewModel
    private lateinit var selectedRole: String
    private lateinit var sessionManager: SessionManager


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sessionManager = SessionManager(requireContext())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater)
        // Инициализация ViewModel через контекст фрагмента.
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        // Заполнение полей, если в ViewModel есть какие-то данные
        restoreRegDataWithViewModel()
        // Конечная отрисовка для фрагмента
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обрабатываем изменения в полях регистрации и добавляем их в ViewModel.
        binding?.etFirstNameUserRegistration?.doOnTextChanged { textFirstName, _, _, _ ->
            viewModel.firstNameRegistration = textFirstName.toString()
        }
        binding?.etLastNameUserRegistration?.doOnTextChanged { textLastName, _, _, _ ->
            viewModel.lastNameRegistration = textLastName.toString()
        }
        binding?.etEmailUserRegistration?.doOnTextChanged { textEmail, _, _, _ ->
            viewModel.emailRegistration = textEmail.toString()
        }
        binding?.etPasswordUserRegistration?.doOnTextChanged { textPassword, _, _, _ ->
            viewModel.passwordRegistration = textPassword.toString()
        }

        // Сделать обработчик нажатия на кнопку "Sign Up!", то есть регистрации.
        binding?.btnConfirmRegistration?.setOnClickListener {
            try { regNewUser() }
            catch (e: Exception) { Log.i("Request error", e.message.toString())}
            finally { Toast.makeText(requireContext(), "Button has been clicked!", Toast.LENGTH_SHORT).show() }
        }
    }


    /** Вызываем метод "onDestroyView()" для того, чтобы очистить binding и избежать утечек памяти.  */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    /** Метод, который регистрирует нового пользователя. Он проверяет валидность всех 4 полей.
     * Если все корректно, далее запускается метод regRequest(), который отправляет запрос на
     * регистрацию на сервер.*/
    private fun regNewUser() {
        // Проверяем, корректно введено поле "Email"
        if (!isValidEmail(binding?.etEmailUserRegistration?.text.toString().trim())) {
            binding?.etEmailUserRegistration?.error = "Invalid email format!"
            Toast.makeText(requireContext(), "Try enter normal email :)", Toast.LENGTH_SHORT).show()
        }
         // Проверяем, корректно ли заполнено поле "Password"
        else if (!isValidPassword(binding?.etPasswordUserRegistration?.text.toString().trim())) {
            binding?.etPasswordUserRegistration?.error = "Invalid password format!"
            Toast.makeText(requireContext(), "@string/warning_correct_password_format", Toast.LENGTH_LONG).show()
        }
        // Проверяем, не пусто ли поле "First name"
        if (binding?.etFirstNameUserRegistration?.toString()?.trim()!!.isEmpty()) {
            binding?.etFirstNameUserRegistration?.error = "Field 'Your first name' can't be empty"
        }
        // Проверяем, не пусто ли поле "Last name"
        else if (binding?.etLastNameUserRegistration?.toString()?.trim()!!.isEmpty()) {
            binding?.etLastNameUserRegistration?.error = "Field 'Your last name' can't be empty"
        }
        // Запустили метод, который отправляет запрос на регистрацию
        else { regRequest() }
    }


    /** Метод, который собирает объект класса <UserRegistrationRequest>, а потом отправляет запрос
     * на сервер с помощью объекта класса ApiClient().
     * Также обрабатываются три исхода: успешный ответ, неуспешный ответ и ошибка со стороны сервера.
     * * При успешном ответе два токена из response.body() сохраняются в локальную БД. */
    private fun regRequest() {

        val registrationInfo: UserRegistrationRequest = UserRegistrationRequest(
            firstName = binding?.etFirstNameUserRegistration?.text.toString().trim(),
            lastName = binding?.etLastNameUserRegistration?.text.toString().trim(),
            email = binding?.etEmailUserRegistration?.text.toString().trim(),
            password = binding?.etPasswordUserRegistration?.text.toString().trim()
        )

        viewModel.apiClient.getAuthAndRegService().performRegistration(registrationInfo).enqueue(object : Callback<UserTokenResponse> {
            override fun onResponse(call: Call<UserTokenResponse>, response: Response<UserTokenResponse>) {
                if (response.isSuccessful) {
                    sessionManager.saveAccessToken(response.body()?.accessToken)
                    sessionManager.saveRefreshToken(response.body()?.refreshToken)
                    Toast.makeText(requireContext(), "User has been registrated!", Toast.LENGTH_LONG).show()

                    // Очищаем поля VIewModel для регистрации после успешного запроса регистрации
                    deleteRegDataWithViewModel()

                    // Удаляем текущий фрагмент регистрации и возвращаемся к фрагменту авторизации
                    parentFragmentManager.popBackStack()
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
                Toast.makeText(requireContext(), "Some error occurred with the server", Toast.LENGTH_LONG).show()
            }

        })
    }


    /** Метод, который проверяет корректность введенной почты. */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    /** Метод, который проверяет минимальные требования введенного пароля.
     * Это минимум 1 цифра, 1 маленкая буква, 1 большяа буква, 1 специальный символ, а также
     * пароль должен содержать минимум 8 символов.
    */
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        return pattern.matcher(password).matches()
    }


    /** Метод, который возвращает из ViewModel данные о регистрации в поля фрагмента. */
    private fun restoreRegDataWithViewModel() {
        binding?.etFirstNameUserRegistration?.setText(viewModel.firstNameRegistration)
        binding?.etLastNameUserRegistration?.setText(viewModel.lastNameRegistration)
        binding?.etEmailUserRegistration?.setText(viewModel.emailRegistration)
        binding?.etPasswordUserRegistration?.setText(viewModel.passwordRegistration)
    }


    private fun deleteRegDataWithViewModel() {
        viewModel.firstNameRegistration = null
        viewModel.lastNameRegistration = null
        viewModel.emailRegistration = null
        viewModel.passwordRegistration = null
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}