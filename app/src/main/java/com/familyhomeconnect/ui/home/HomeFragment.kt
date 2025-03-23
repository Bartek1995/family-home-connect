package com.familyhomeconnect.ui.home

import HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.familyhomeconnect.MainActivity
import com.familyhomeconnect.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.isLoggedIn.observe(viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                binding.textHome.text = "Jesteś zalogowany!"
                binding.loginButton.visibility = View.GONE
            } else {
                binding.textHome.text = "Nie jesteś zalogowany."
                binding.loginButton.visibility = View.VISIBLE
            }
        }

        binding.loginButton.setOnClickListener {
            (activity as? MainActivity)?.authClient?.signIn(
                onSuccess = { email ->
                    Snackbar.make(binding.root, "Zalogowano jako: $email", Snackbar.LENGTH_LONG).show()
                },
                onError = { error ->
                    Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                }
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
