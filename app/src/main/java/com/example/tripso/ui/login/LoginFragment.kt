package com.example.tripso.ui.login

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tripso.R
import com.example.tripso.databinding.FragmentLoginBinding
import com.example.tripso.data.DataStateResult
import com.example.tripso.domain.util.Constants


class LoginFragment : Fragment() {

    private val loginViewModel : LoginViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        binding.tvLoginSignup.setOnClickListener {
        findNavController().navigate(LoginFragmentDirections.actionShowSignUpFragment())
        }

        loginButton.setOnClickListener {
            loginViewModel.logIn()
        }

        loginViewModel.dataStateResult.observe(viewLifecycleOwner){state ->
            when(state){
                is DataStateResult.Error -> {
                    loadingProgressBar.visibility = View.GONE
                }
                is DataStateResult.Loading -> loadingProgressBar.visibility = View.VISIBLE
                is DataStateResult.Success ->{
                    loadingProgressBar.visibility = View.GONE
                    if (!Constants.LogOutTag) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }

                }
            }
        }


    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}