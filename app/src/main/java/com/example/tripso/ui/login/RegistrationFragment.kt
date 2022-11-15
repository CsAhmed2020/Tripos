package com.example.tripso.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tripso.data.DataStateResult
import com.example.tripso.databinding.FragmentRegestrationBinding


class RegistrationFragment : Fragment() {

    private val loginViewModel : LoginViewModel by activityViewModels()

    private var _binding: FragmentRegestrationBinding? = null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentRegestrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingProgressBar = binding.loading
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        binding.tvGoLogin.setOnClickListener {
            findNavController().navigate(RegistrationFragmentDirections.actionShowLoginFragment())
        }

        binding.signup.setOnClickListener {
            loginViewModel.signUp()
        }

        loginViewModel.dataStateResult.observe(viewLifecycleOwner){state ->
            when(state){
                is DataStateResult.Error -> {
                    loadingProgressBar.visibility = View.GONE
                }
                is DataStateResult.Loading -> loadingProgressBar.visibility = View.VISIBLE
                is DataStateResult.Success -> {
                    loadingProgressBar.visibility = View.GONE
                    findNavController().navigate(RegistrationFragmentDirections.actionShowHomeFragment())
                }
            }
        }

    }



}