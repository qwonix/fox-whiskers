package ru.qwonix.android.foxwhiskers.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.qwonix.android.foxwhiskers.ProfileNavigationDirections
import ru.qwonix.android.foxwhiskers.R
import ru.qwonix.android.foxwhiskers.databinding.FragmentProfileBinding
import ru.qwonix.android.foxwhiskers.entity.Client
import ru.qwonix.android.foxwhiskers.repository.ApiResponse
import ru.qwonix.android.foxwhiskers.viewmodel.CoroutinesErrorHandler
import ru.qwonix.android.foxwhiskers.viewmodel.ProfileViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val TAG = "ProfileFragment"

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "open ProfileFragment")
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.isLoading = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.tryLoadClientAndAuthenticate(object : CoroutinesErrorHandler {
            override fun onError(message: String) {
                Toast.makeText(
                    context,
                    "Нет подключения к интернету :(",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_profileFragment_to_phoneNumberInputFragment)
            }
        })

        profileViewModel.clientAuthenticationResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Failure -> {
                    Log.e(TAG, "fail to load profile code: ${it.code} – ${it.errorMessage}")
                    findNavController().navigate(R.id.action_profileFragment_to_phoneNumberInputFragment)
                }

                is ApiResponse.Loading -> Log.i(TAG, "loading")

                is ApiResponse.Success -> {
                    Log.i(TAG, "Successful client load – ${it.data}")

                    if (it.data != null) {
                        if (profileViewModel.isRequiredForEdit(it.data)) {
                            findNavController().navigate(
                                ProfileFragmentDirections.actionProfileFragmentToProfileEditingFragment(
                                    it.data
                                )
                            )
                        }
                        binding.client = it.data
                        binding.isLoading = false
                    }
                }
            }
        }

        binding.goToOrdersButton.setOnClickListener {
            when (val clientResponse = profileViewModel.clientAuthenticationResponse.value) {
                is ApiResponse.Failure -> {
                    Log.e(TAG, "code: ${clientResponse.code} – ${clientResponse.errorMessage}")
                }

                is ApiResponse.Loading -> Log.i(TAG, "loading")

                is ApiResponse.Success -> {
                    findNavController().navigate(
                        ProfileNavigationDirections.actionGlobalOrderReceiptFragment(
                            clientResponse.data!!
                        )
                    )
                }

                else -> {}
            }

        }

        binding.logoutButton.setOnClickListener {
            Log.i(TAG, "logout – ${binding.client}")
            profileViewModel.logout(object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    TODO("Not yet implemented")
                }
            })
            findNavController().navigate(R.id.action_profileFragment_to_phoneNumberInputFragment)
        }

        binding.editButton.setOnClickListener {
            Log.i(TAG, "edit – ${binding.client}")
            val client: Client = binding.client!!

            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToProfileEditingFragment(
                    client
                )
            )
        }
    }
}