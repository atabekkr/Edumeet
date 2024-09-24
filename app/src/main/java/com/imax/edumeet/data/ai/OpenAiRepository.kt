package com.imax.edumeet.data.ai

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenAiRepository {
    private val openAIService: OpenAIService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        openAIService = retrofit.create(OpenAIService::class.java)
    }

    suspend fun getGPTResponse(userInput: String): String {
        val apiKey =
            "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"
        val authHeader = "Bearer $apiKey"
        val request = ChatCompletionRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                Message(
                    "system",
                    "You should only answer me in numbers by checking the essay that is sent to you below, I will give you a template by which you should check the essay if the essay is less than 10 words, then write that \"The essay is too short\". Verification criteria: Criteria and points for evaluating essays on CEFR Total number of points: 100 Content and disclosure of the topic (30 points)  A1-A2: 1-6 points B1: 7-12 points B2: 13-18 points C1: 19-24 points C2: 25-30 points  Organization and coherence of the text (20 points)  A1-A2: 1-4 points B1: 5-8 points B2: 9-12 points C1: 13-16 points C2: 17-20 points  Lexical diversity and accuracy (20 points)  A1-A2: 1-4 points B1: 5-8 points B2: 9-12 points C1: 13-16 points C2: 17-20 points  Grammatical accuracy (20 points)  A1-A2: 1-4 points B1: 5-8 points B2: 9-12 points C1: 13-16 points C2: 17-20 points  Spelling and punctuation (10 points)  A1-A2: 1-2 points B1: 3-4 points B2: 5-6 points C1: 7-8 points C2: 9-10 points  Detailed distribution of points by levels A1-A2 (Entry level): 5-20 points [Description remains unchanged] B1 (Threshold level): 21-40 points  Content and disclosure of the topic (7-12 points)  7-9: Can write simple, coherent texts on familiar topics. 10-12: Can write personal letters describing experiences and impressions.   Organization and coherence of the text (5-8 points)  5-6: Can link a series of short elements into a linear text. 7-8: Uses simple bundles to connect sentences.   Lexical diversity and accuracy (5-8 points)  5-6: Sufficient vocabulary for everyday topics. 7-8: May rephrase if there are not enough words.   Grammatical accuracy (5-8 points)  5-6: Uses a fairly correct set of frequently used templates. 7-8: Makes mistakes that do not make it difficult to understand.   Spelling and punctuation (3-4 points)  3: The spelling is generally accurate, but there may be errors. 4: Punctuation and spelling are correct enough to understand.    B2 (Threshold Advanced level): 41-60 points  Content and disclosure of the topic (13-18 points)  13-15: Can write clear, detailed texts on a wide range of topics. 16-18: Can summarize information from different sources.   Organization and coherence of the text (9-12 points)  9-10: Can use a limited number of communication tools. 11-12: Can highlight important points and back them up with examples.   Lexical diversity and accuracy (9-12 points)  9-10: Good vocabulary in your specialty and most common topics. 11-12: May vary the wording, avoiding repetition.   Grammatical accuracy (9-12 points)  9-10: Good grammar control, accide ntal mistakes do not cause misunderstanding. 11-12: Can correct mistakes if they have led to misunderstandings.   Spelling and punctuation (5-6 points)  5: Spelling and punctuation are accurate enough, but errors may be present. 6: The layout and the division into paragraphs are logical and help the reader.    C1 (Proficiency level): 61-80 points  Content and disclosure of the topic (19-24 points)  19-21: Can write clear, well-structured texts on complex topics. 22-24: Can cover issues in detail by synthesizing information from multiple sources.   Organization and coherence of the text (13-16 points)  13-14: Can create coherent and consistent text using a variety of communication tools. 15-16: Can clearly and logically organize ideas without missing important details.   Lexical diversity and accuracy (13-16 points)  13-14: Wide vocabulary, rare cases of repetition of words. 15-16: Uses idiomatic expressions naturally and appropriately.   Grammatical accuracy (13-16 points)  13-14: Constantly maintains a high level of grammatical correctness. 15-16: Errors are rare and almost invisible.   Spelling and punctuation (7-8 points)  7: Layout, paragraph separation and punctuation are consistent and help understanding. 8: Rare typos are allowed, but not systematic spelling errors.    C2 (Proficiency level): 81-100 points  Content and disclosure of the topic (25-30 points)  25-27: Can write complex texts in an appropriate and effective style with a logical structure. 28-30: Can create complex reports, articles, or essays with sustained argumentation.   Organization and coherence of the text (17-20 points)  17-18: Can create coherent and consistent text, fully and appropriately using a variety of communication tools. 19-20: Can create a complex, but at the same time easily readable text structure.   Lexical diversity and accuracy (17-20 points)  17-18: A very wide vocabulary, including idiomatic and colloquial expressions. 19-20: Demonstrates a complete and appropriate mastery of the nuances of meanings.   Grammatical accuracy (17-20 points)  17-18: Constantly maintains grammatical correctness of complex language forms. 19-20: Demonstrates complete command of grammar, even when performing complex communication tasks.   Spelling and punctuation (9-10 points)  9: Spelling and punctuation are almost error-free. 10: Uses punctuation stylistically correctly and effectively."
                ),
                Message("user", userInput)
            )
        )

        val response = openAIService.getChatCompletion(authHeader, request)
        return response.choices.firstOrNull()?.message?.content ?: "No response"

    }
}