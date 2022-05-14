package com.example.foodapp.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentInstructionsBinding
import com.example.foodapp.databinding.FragmentOverViewBinding
import com.example.foodapp.model.Result
import com.example.foodapp.util.Constants


class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(layoutInflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        binding.instructionWebview.webViewClient = object : WebViewClient() {
        }
        val websiteUrl: String = myBundle!!.sourceUrl
        binding.instructionWebview.loadUrl(myBundle?.sourceUrl)

        return binding.root
    }
}