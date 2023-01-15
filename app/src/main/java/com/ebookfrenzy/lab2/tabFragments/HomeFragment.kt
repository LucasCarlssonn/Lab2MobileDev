package com.ebookfrenzy.lab2.tabFragments

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ebookfrenzy.lab2.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewFilm1.movementMethod = LinkMovementMethod.getInstance()
        binding.textViewFilm2.movementMethod = LinkMovementMethod.getInstance()
        binding.textViewFilm3.movementMethod = LinkMovementMethod.getInstance()
        binding.textViewFilm4.movementMethod = LinkMovementMethod.getInstance()
        binding.textViewFilm5.movementMethod = LinkMovementMethod.getInstance()


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}