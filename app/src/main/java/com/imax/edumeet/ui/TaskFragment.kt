package com.imax.edumeet.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentTaskBinding
import com.imax.edumeet.models.ClassicQuestion
import com.imax.edumeet.models.TestClassic
import com.imax.edumeet.utils.getJsonDataFromAsset
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {

    private val binding by viewBinding(FragmentTaskBinding::bind)
    private val navArgs by navArgs<TaskFragmentArgs>()

    private var question: ClassicQuestion? = null

    private var currentQuestionId = 0
    private var result = 0

    private var listOfVariantButton = listOf<Button>()

    private var questions: List<ClassicQuestion>? = listOf()

    private var isSelected = false

    private var selectText: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonString = getJsonDataFromAsset(requireContext(), "test_classic.json")
        val classic = Gson().fromJson(jsonString, TestClassic::class.java)
        val testClassic = classic.tests_classic.singleOrNull { it.topic_id == 101 }
        questions = testClassic?.questions

        binding.apply {

            listOfVariantButton = listOf(
                btnOption1,
                btnOption2,
                btnOption3,
            )

            binding.progressBar.max = questions?.size ?: 10
            setQuestion(currentQuestionId)

            btnOption1.setOnClickListener {
                optionSelected(btnOption1)
            }
            btnOption2.setOnClickListener {
                optionSelected(btnOption2)
            }
            btnOption3.setOnClickListener {
                optionSelected(btnOption3)
            }

            btnNext.setOnClickListener {
                if (currentQuestionId == questions!!.size - 1) {
                    if (question?.correct_answer == selectText) result++
                    findNavController().navigate(
                        TaskFragmentDirections.actionTaskFragmentToResultFragment(
                            result
                        )
                    )
                } else {
                    if (isSelected) {
                        if (question?.correct_answer == selectText) result++
                        currentQuestionId++
                        setQuestion(currentQuestionId)
                    } else {
                        snackBar("You should select the option")
                    }
                }
            }

            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setQuestion(questionId: Int) {
        isSelected = false

        question = questions?.get(questionId)
        val options = questions?.get(questionId)?.options

        binding.tvQuestion.text = question?.question
        listOfVariantButton.forEachIndexed { index, button ->
            button.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.inputs_color
                )
            )
            button.text = options?.get(index) ?: "Nothing"
        }

    }

    private fun optionSelected(selectedButton: Button) = binding.apply {

        btnOption1.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                if (selectedButton == btnOption1) R.color.typography_1 else R.color.inputs_color
            )
        )
        btnOption2.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                if (selectedButton == btnOption2) R.color.typography_1 else R.color.inputs_color
            )
        )
        btnOption3.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                if (selectedButton == btnOption3) R.color.typography_1 else R.color.inputs_color
            )
        )

        selectText = selectedButton.text.toString()
        isSelected = true

    }
}