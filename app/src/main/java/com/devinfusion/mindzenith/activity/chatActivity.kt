import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devinfusion.mindzenith.databinding.ActivityChatBinding
import com.google.android.material.snackbar.Snackbar
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier.Options
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier.Entity
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier.Entity.Label
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier.Input
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier.Output
import java.nio.ByteBuffer
import java.util.Locale

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            val userText = binding.editText.text.toString()
            if (userText.isNotEmpty()) {
                performSentimentAnalysis(userText)
            } else {
                Snackbar.make(binding.root, "Please enter some text", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSentimentAnalysis(text: String) {
        val sentiment = analyzeSentiment(text)
        getRandomSolution(sentiment)
        logResponse(sentiment)

    }

    private fun getRandomSolution(sentiment: String): String {
        val solutions = when (sentiment) {
            "Negative" -> listOf(
                "If you're feeling down, consider reaching out to someone you trust for support. Engaging in activities that make you happy can also help improve your mood.",
                "Remember that challenges are temporary. Focus on self-care and things that bring you joy.",
                "Expressing your feelings through art or writing can be therapeutic during tough times."
            )
            "Neutral" -> listOf(
                "Staying neutral can be a chance to explore new interests. Consider taking up a hobby you've always wanted to try.",
                "Take a break and enjoy a moment of mindfulness. Even small pauses can lead to big benefits.",
                "Balance is key. Plan your day to include a mix of work, relaxation, and exploration."
            )
            "Positive" -> listOf(
                "Embrace the positive energy and let it inspire you to achieve your goals.",
                "Your positivity can be contagious. Share it by inspiring someone else today.",
                "Celebrate your successes, no matter how small. Each step forward is an accomplishment."
            )
            else -> listOf(
                "It's okay not to have all the answers. Give yourself time and space to discover your feelings.",
                "Sometimes uncertainty can lead to new insights. Engage in activities that promote self-discovery.",
                "Be patient with yourself. Exploring new hobbies can help you connect with your emotions."
            )
        }

        return solutions.random()
    }



    private fun analyzeSentiment(text: String): String {
        val model = AutoModelForSequenceClassification.fromPretrained("nlptown/bert-base-multilingual-uncased-sentiment")
        val tokenizer = AutoTokenizer.fromPretrained("nlptown/bert-base-multilingual-uncased-sentiment")

        val tokens = tokenizer.encodePlus(text, return_tensors = "pt")
        val inputIds = tokens["input_ids"] as TensorBuffer
        val attentionMask = tokens["attention_mask"] as TensorBuffer

        val outputs = model.forward(inputIds, attentionMask)
        val predictedSentiment = outputs.logits.argmax()

        return when (predictedSentiment) {
            0 -> "Negative"
            1 -> "Neutral"
            2 -> "Positive"
            else -> "Unknown"
        }
    }

    private fun logResponse(sentiment: String) {
        Snackbar.make(binding.root, "Predicted Sentiment: $sentiment", Snackbar.LENGTH_SHORT).show()
    }
}
