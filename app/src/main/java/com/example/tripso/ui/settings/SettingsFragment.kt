package com.example.tripso.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tripso.R
import com.example.tripso.data.DataStateResult
import com.example.tripso.databinding.FragmentSettingsBinding
import com.example.tripso.domain.util.Constants
import com.example.tripso.domain.util.Utils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding ? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsViewModel= settingsViewModel
        binding.lifecycleOwner = this
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.arabic_rb){
                Utils.setAppLanguage(this.requireContext(),"ar")
            }else{
                Utils.setAppLanguage(this.requireContext(),"en")
            }
        }

        binding.resetPassBtn.setOnClickListener {
            Snackbar.make(this.requireView(),getString(R.string.check_email),Snackbar.LENGTH_LONG).show()
            settingsViewModel.resetPassword()
        }

        settingsViewModel.logOutState.observe(viewLifecycleOwner){logOutState ->
            when (logOutState){
                is DataStateResult.Error -> {
                    Toast.makeText(this.context,R.string.error_logout,Toast.LENGTH_SHORT).show()
                }
                is DataStateResult.Loading -> {}
                is DataStateResult.Success -> {
                    Constants.LogOutTag = true
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }
    }

}