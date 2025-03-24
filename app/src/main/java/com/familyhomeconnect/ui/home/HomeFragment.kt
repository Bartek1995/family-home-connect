package com.familyhomeconnect.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.familyhomeconnect.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // HomeViewModel odpowiada tylko za prezentację informacji o domu
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Obserwuj dane o domu i aktualizuj UI
        homeViewModel.houseInfo.observe(viewLifecycleOwner) { house ->
            // Załóżmy, że masz TextView do wyświetlania adresu lub innych danych domu
            binding.textHome.text = "Adres: ${house.address}"
            // Dodaj inne elementy interfejsu według potrzeb
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
