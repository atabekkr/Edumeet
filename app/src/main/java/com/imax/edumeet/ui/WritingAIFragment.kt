package com.imax.edumeet.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentWritingAiBinding
import com.imax.edumeet.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WritingAIFragment : Fragment(R.layout.fragment_writing_ai) {

    private val binding by viewBinding(FragmentWritingAiBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val essay = binding.inputTopic.text.toString() + ": " + binding.etEssay.text.toString()
        val essay1 =
            "The Paradox of Homeland: A Crucible of Identity and Conflict  The concept of homeland, ostensibly straightforward, reveals itself to be a labyrinthine construct upon closer examination. It is a notion that transcends mere geographical boundaries, encompassing a complex interplay of historical, cultural, and psychological factors that shape both individual and collective identities. As I ruminate on my own homeland, I find myself grappling with the inherent contradictions and multifaceted nature of this deeply personal yet universally resonant concept.  At its most elemental level, one's homeland is inextricably linked to the physical environment in which one's formative years unfold. The topography of my native land—its undulating hills, verdant forests, and meandering rivers—has indelibly etched itself upon my psyche. The very air, redolent with the scent of native flora, serves as a powerful mnemonic device, instantly transporting me to moments of childhood wonder. Yet, to reduce the notion of homeland to mere sensory experiences would be a gross oversimplification.  The cultural tapestry of my homeland is a palimpsest, bearing the imprints of myriad civilizations and historical epochs. Our linguistic idiosyncrasies, gastronomic traditions, and social mores are the result of centuries of cultural cross-pollination, invasion, and assimilation. This rich heritage serves as both a source of pride and a wellspring of internal conflict. On one hand, it provides a sense of continuity and belonging; on the other, it can be a divisive force, particularly in an era of resurgent nationalism and ethnic tensions.  The concept of homeland is further complicated by the phenomenon of globalization and increased mobility. In an age where one can traverse continents with ease, the boundaries of what constitutes 'home' have become increasingly porous. Many individuals, myself included, find themselves straddling multiple cultural identities, leading to a sense of rootlessness or, conversely, a more expansive definition of homeland. This fluidity challenges the traditional notion of homeland as a fixed, immutable entity and raises profound questions about the nature of belonging in the 21st century.  Moreover, one's relationship with their homeland is often characterized by a peculiar ambivalence. It is a source of both comfort and frustration, pride and shame. The very aspects that endear us to our homeland—its quirks, traditions, and shared history—can also be the source of its most intractable problems. The collective narratives that bind us together can sometimes ossify into dogma, impeding progress and perpetuating cycles of conflict.  The political dimension of homeland adds yet another layer of complexity. Governments often exploit the emotive power of homeland to further their agendas, manipulating patriotic sentiment to garner support for policies that may not necessarily serve the best interests of the populace. This instrumentalization of homeland can lead to jingoism, xenophobia, and a distorted view of one's nation vis-à-vis the global community.  Paradoxically, it is often through distance—both physical and temporal—that one gains a more nuanced appreciation of their homeland. The perspective afforded by expatriation or historical study allows for a more critical examination of one's native land, tempering blind adulation with reasoned critique. This balanced view is essential for fostering genuine patriotism, one that acknowledges flaws while working constructively towards their amelioration.  In conclusion, the concept of homeland defies facile definition. It is at once deeply personal and inherently collective, a source of solace and strife, continuity and change. As we navigate an increasingly interconnected world, our understanding of homeland must evolve to accommodate the complexities of modern existence. Perhaps the truest expression of love for one's homeland lies not in unquestioning allegiance, but in a commitment to engage critically and constructively with its multifaceted nature, striving to reconcile its cont"
        viewModel.sendEssayToGPT(essay)

        binding.btnSend.setOnClickListener {
            lifecycleScope.launch {
                viewModel.gptResponse.collectLatest { response ->
                    response?.let {
                        binding.etResponse.setText(it)
                    }
                }
            }
        }
    }
}