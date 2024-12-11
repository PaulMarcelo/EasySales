package ec.com.pmyb.easysales.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class NumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text
        val parts = inputText.split(".")
        val formattedText = if (parts.size == 2) {
            "${parts[0].take(3)}.${parts[1].take(2)}"
        } else {
            parts[0].take(3)
        }
        return TransformedText(
            AnnotatedString(formattedText),
            OffsetMapping.Identity
        )
    }
}