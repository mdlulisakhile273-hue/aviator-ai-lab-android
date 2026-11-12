package com.djymahewu.aviatorailab

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.Locale
import kotlin.math.sqrt

class MainActivity : Activity() {
    private val samples = mutableListOf<Double>()
    private lateinit var input: EditText
    private lateinit var results: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 10, 25)
        window.navigationBarColor = Color.rgb(12, 10, 25)

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 24)
            setBackgroundColor(Color.rgb(12, 10, 25))
        }
        scroll.addView(content)

        content.addView(label("AVIATOR AI LAB", 28, Color.WHITE, true))
        content.addView(label("Probability & statistics learning companion", 14, Color.LTGRAY))
        content.addView(label(
            "Educational tool only. It does not predict future game results, connect to betting sites, or recommend bets.",
            14, Color.rgb(255, 190, 120)
        ))

        input = EditText(this).apply {
            hint = "Enter a sample value, e.g. 2.45"
            setHintTextColor(Color.GRAY)
            setTextColor(Color.WHITE)
            setSingleLine(true)
            setPadding(14, 12, 14, 12)
            setBackgroundColor(Color.rgb(35, 29, 55))
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        content.addView(input, marginParams())
        content.addView(actionButton("Add sample") { addSample() })
        content.addView(actionButton("Analyse samples") { analyse() })
        content.addView(actionButton("Clear samples") {
            samples.clear()
            results.text = "No samples yet. Add values to learn about averages and variation."
        })

        results = label("No samples yet. Add values to learn about averages and variation.", 16, Color.WHITE)
        content.addView(results)
        content.addView(label(
            "Past observations do not determine the next random outcome. Statistics describe a sample; they are not a forecast.",
            14, Color.LTGRAY
        ))
        setContentView(scroll)
    }

    private fun label(text: String, size: Int, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            this.text = text
            textSize = size.toFloat()
            setTextColor(color)
            gravity = Gravity.START
            if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 10, 0, 10)
        }

    private fun marginParams() = LinearLayout.LayoutParams(-1, -2).apply {
        topMargin = 12
        bottomMargin = 8
    }

    private fun actionButton(text: String, action: () -> Unit) =
        Button(this).apply {
            this.text = text
            setOnClickListener { action() }
            layoutParams = marginParams()
        }

    private fun addSample() {
        val value = input.text.toString().trim().toDoubleOrNull()
        if (value == null || value <= 0.0 || value > 100000.0) {
            input.error = "Enter a positive number"
            return
        }
        samples.add(value)
        input.text.clear()
        results.text = "Added ${fmt(value)}. Samples recorded: ${samples.size}"
    }

    private fun analyse() {
        if (samples.isEmpty()) {
            results.text = "Add at least one sample first."
            return
        }
        val sorted = samples.sorted()
        val mean = samples.average()
        val median = if (sorted.size % 2 == 1) sorted[sorted.size / 2]
            else (sorted[sorted.size / 2 - 1] + sorted[sorted.size / 2]) / 2.0
        val variance = samples.map { (it - mean) * (it - mean) }.average()
        val below2 = samples.count { it < 2.0 }
        val from2to5 = samples.count { it >= 2.0 && it < 5.0 }
        val fivePlus = samples.count { it >= 5.0 }
        results.text = """
            SAMPLE SUMMARY
            Count: ${samples.size}
            Average: ${fmt(mean)}
            Median: ${fmt(median)}
            Minimum: ${fmt(sorted.first())}
            Maximum: ${fmt(sorted.last())}
            Standard deviation: ${fmt(sqrt(variance))}

            GROUPS
            Below 2: ${percent(below2)}
            2 to below 5: ${percent(from2to5)}
            5 and above: ${percent(fivePlus)}
        """.trimIndent()
    }

    private fun percent(count: Int) = "${fmt(count * 100.0 / samples.size)}% ($count)"
    private fun fmt(value: Double) = String.format(Locale.US, "%.2f", value)
}
