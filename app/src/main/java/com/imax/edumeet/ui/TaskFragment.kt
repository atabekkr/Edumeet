package com.imax.edumeet.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.service.autofill.FieldClassification.Match
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
import com.imax.edumeet.models.MatchingAnswers
import com.imax.edumeet.models.MatchingQuestion
import com.imax.edumeet.models.TestClassic
import com.imax.edumeet.models.TestMatching
import com.imax.edumeet.models.TestTrueFalse
import com.imax.edumeet.models.TrueFalseQuestion
import com.imax.edumeet.utils.Constants
import com.imax.edumeet.utils.getJsonDataFromAsset
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {

    private val binding by viewBinding(FragmentTaskBinding::bind)
    private val navArgs by navArgs<TaskFragmentArgs>()

    private var questionClassic: ClassicQuestion? = null
    private var questionTrueFalse: TrueFalseQuestion? = null
    private var questionMatching: MatchingQuestion? = null

    private var currentQuestionId = 0
    private var result = 0

    private var listOfVariantButton = listOf<Button>()

    private var classicTestQuestions: List<ClassicQuestion>? = listOf()
    private var trueFalseTestQuestions: List<TrueFalseQuestion>? = listOf()
    private var matchingQuestions: List<MatchingQuestion>? = null

    private var matchingAnswers : Set<MatchingAnswers>? = null

    private var sizeOfQuestions = 0
    private var type = 0

    private var isSelected = false

    private var selectText: String = ""
    private var selectedBoolean: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonClassicString = getJsonDataFromAsset(requireContext(), "test_classic.json")
        val classic = Gson().fromJson(jsonClassicString, TestClassic::class.java)
        val testClassic = classic.tests_classic.singleOrNull { it.topic_id == navArgs.topicId }
        classicTestQuestions = testClassic?.questions

        val jsonTrueFalseString = getJsonDataFromAsset(requireContext(), "test_true_false.json")
        val trueFalse = Gson().fromJson(jsonTrueFalseString, TestTrueFalse::class.java)
        val testTrueFalse =
            trueFalse.true_false_tests.singleOrNull { it.topic_id == navArgs.topicId }
        trueFalseTestQuestions = testTrueFalse?.questions

        val jsonMatchingString = getJsonDataFromAsset(requireContext(), "test_matching.json")
        val matching = Gson().fromJson(jsonMatchingString, TestMatching::class.java)
        val testMatching =
            matching.matching_tests.singleOrNull { it.topic_id == navArgs.topicId }
        matchingQuestions = testMatching?.questions
        matchingAnswers = testMatching?.answers?.toSet()

        binding.apply {

            listOfVariantButton = listOf(
                btnOption1,
                btnOption2,
                btnOption3,
            )

            if (classicTestQuestions != null) {
                binding.progressBar.max = classicTestQuestions!!.size
                sizeOfQuestions = classicTestQuestions!!.size
                setQuestionClassic(currentQuestionId)
                type = Constants.CLASSIC
            } else if (trueFalseTestQuestions != null) {
                binding.progressBar.max = trueFalseTestQuestions!!.size
                sizeOfQuestions = trueFalseTestQuestions!!.size
                setQuestionTrueFalse(currentQuestionId)
                type = Constants.TRUEFALSE
            } else {
                if (matchingQuestions != null) {
                    binding.progressBar.max = matchingQuestions!!.size
                    sizeOfQuestions = matchingQuestions!!.size
                    setQuestionMatching(currentQuestionId)
                    type = Constants.MATCHING
                }
            }

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
                if (currentQuestionId == sizeOfQuestions - 1) {
                    binding.progressBar.progress++
                    var matchingCorrect = false
                    questionMatching?.correct_answer_id?.let { id ->
                        matchingCorrect = matchingAnswers?.single { it.id == id }?.answer == selectText
                    }
                    if (questionClassic?.correct_answer == selectText || questionTrueFalse?.correct_answer == selectedBoolean || matchingCorrect) result++
                    findNavController().navigate(
                        TaskFragmentDirections.actionTaskFragmentToResultFragment(
                            result
                        )
                    )
                } else {
                    if (isSelected) {
                        binding.progressBar.progress++
                        var matchingCorrect = false
                        questionMatching?.correct_answer_id?.let { id ->
                            matchingCorrect = matchingAnswers?.single { it.id == id }?.answer == selectText
                        }
                        if (questionClassic?.correct_answer == selectText || questionTrueFalse?.correct_answer == selectedBoolean || matchingCorrect) result++
                        currentQuestionId++
                        when (type) {
                            Constants.CLASSIC -> setQuestionClassic(currentQuestionId)
                            Constants.TRUEFALSE -> setQuestionTrueFalse(currentQuestionId)
                            Constants.MATCHING -> setQuestionMatching(currentQuestionId)
                        }
                    } else {
                        snackBar("You should select the option")
                    }
                }
            }

            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setQuestionClassic(questionId: Int) {
        isSelected = false
        binding.tvCount.text =
            getString(R.string.tablo, "${questionId + 1}", classicTestQuestions?.size.toString())

        questionClassic = classicTestQuestions?.get(questionId)
        val options = classicTestQuestions?.get(questionId)?.options

        binding.tvQuestion.text = questionClassic?.question
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

    private fun setQuestionMatching(questionId: Int) {
        isSelected = false
        binding.tvCount.text =
            getString(R.string.tablo, "${questionId + 1}", matchingQuestions?.size.toString())

        questionMatching = matchingQuestions?.get(questionId)

        val options = mutableSetOf<MatchingAnswers>()
        val correct = matchingAnswers?.single { it.id == questionMatching?.correct_answer_id }
        correct?.let { options.add(it) }
        matchingAnswers?.shuffled()?.forEach {
            if (options.size < 3) {
                options.add(it)
            }
        }

        binding.tvQuestion.text = questionMatching?.question
        listOfVariantButton.forEachIndexed { index, button ->
            button.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.inputs_color
                )
            )
            button.text = options.elementAt(index).answer
        }

    }

    private fun setQuestionTrueFalse(questionId: Int) {

        isSelected = false
        binding.tvCount.text =
            getString(R.string.tablo, "${questionId + 1}", trueFalseTestQuestions?.size.toString())

        questionTrueFalse = trueFalseTestQuestions?.get(questionId)

        binding.tvQuestion.text = questionTrueFalse?.question

        listOfVariantButton.forEachIndexed { index, button ->
            button.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.inputs_color
                )
            )
            when (index) {
                0 -> {
                    button.text = "True"
                }

                1 -> {
                    button.text = "False"
                }

                2 -> {
                    button.visibility = View.GONE
                }
            }
        }

    }

    private fun optionSelected(selectedButton: Button) = binding.apply {

        selectedBoolean = selectedButton.text == "True"

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