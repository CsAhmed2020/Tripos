package com.example.tripso.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.tripso.R
import com.example.tripso.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel : ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileViewModel = profileViewModel

        binding.editBtn.setOnClickListener {
            binding.userName.isEnabled = true
            binding.userPhone.isEnabled = true
        }

        profileViewModel.saved.observe(viewLifecycleOwner){
            if (it){
                Snackbar.make(this.requireView(),getString(R.string.saved), Snackbar.LENGTH_SHORT).show()
                profileViewModel.updateSaveState(false)
            }
        }
    }
}