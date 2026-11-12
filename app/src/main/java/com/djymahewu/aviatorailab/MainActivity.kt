package com.djymahewu.aviatorailab

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.Locale
import kotlin.random.Random

class MainActivity : Activity() {
    private var totalTosses = 0
    private var heads = 0
    private var tails = 0
    private lateinit var resultView: TextView
    private lateinit var historyView: TextView
    private lateinit var assistantView: TextView

    private val bg = Color.rgb(10, 9, 24)
    private val panel = Color.rgb(26, 23, 48)
    private val purple = Color.rgb(164, 112, 255)
    private val cyan = Color.rgb(65, 220, 235)
    private val muted = Color.rgb(180, 177, 200)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg

        val scroll = ScrollView(this)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 24, 20, 28)
            setBackgroundColor(bg)
        }
        scroll.addView(root)

        root.addView(text("DJY MAHEWU PRESENTS", 11, cyan, true))
        root.addView(text("AVIATOR AI LAB", 29, Color.WHITE, true))
        root.addView(text("EXPERIMENT • LEARN • UNDERSTAND", 11, muted, true))
        root.addView(space(16))

        val intro = card()
        intro.addView(text("WELCOME TO THE LAB", 15, cyan, true))
        intro.addView(text(
            "Explore probability, randomness and statistics through simple experiments. This is a learning simulator, not a game predictor.",
            14, Color.WHITE
        ))
        root.addView(intro)

        root.addView(sectionTitle("01  •  SIMULATION LAB"))
        val sim = card()
        sim.addView(text("COIN TOSS EXPERIMENT", 16, Color.WHITE, true))
        sim.addView(text("A fair coin has a theoretical 50% chance of landing on heads and 50% on tails.", 13, muted))
        sim.addView(actionButton("Run 10 tosses", purple) { runExperiment(10) })
        sim.addView(actionButton("Run 100 tosses", purple) { runExperiment(100) })
        sim.addView(actionButton("Run 1,000 tosses", purple) { runExperiment(1000) })
        sim.addView(actionButton("Reset experiment", Color.rgb(75, 68, 100)) { resetExperiment() })
        resultView = text("Your experiment results will appear here.", 14, Color.WHITE)
        sim.addView(resultView)
        root.addView(sim)

        root.addView(sectionTitle("02  •  HISTORY LAB"))
        val history = card()
        history.addView(text("SESSION SUMMARY", 16, Color.WHITE, true))
        historyView = text("No experiments recorded in this session yet.", 14, muted)
        history.addView(historyView)
        root.addView(history)

        root.addView(sectionTitle("03  •  AI LEARNING DESK"))
        val ai = card()
        ai.addView(text("WHY DOES RANDOMNESS LOOK UNEVEN?", 16, cyan, true))
        assistantView = text(
            "Even when outcomes are equally likely, short experiments can produce uneven results. With more trials, the observed proportions often move closer to the theoretical probability, but they are not guaranteed to match exactly.",
            14, Color.WHITE
        )
        ai.addView(assistantView)
        ai.addView(actionButton("Explain independent events", cyan) {
            assistantView.text = "Independent events do not change one another's probabilities. For example, in a fair coin experiment, the result of one toss does not change the 50% chance of heads on the next toss. A streak does not make the opposite result 'due'."
        })
        ai.addView(actionButton("Explain sample size", cyan) {
            assistantView.text = "A small sample can vary noticeably from the theoretical probability. Larger samples often show a more stable proportion, but random variation never disappears completely and no exact future result is guaranteed."
        })
        root.addView(ai)

        root.addView(sectionTitle("ABOUT THIS BUILD"))
        val about = card()
        about.addView(text("AVIATOR AI LAB  •  VERSION 2.0", 13, Color.WHITE, true))
        about.addView(text(
            "Offline educational prototype. No live-game connection, outcome forecasts, betting signals or betting recommendations. Experiments use locally generated random coin tosses.",
            12, muted
        ))
        root.addView(about)

        setContentView(scroll)
    }

    private fun text(value: String, size: Int, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            text = value
            textSize = size.toFloat()
            setTextColor(color)
            setPadding(0, 7, 0, 7)
            if (bold) setTypeface(typeface, Typeface.BOLD)
        }

    private fun card() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(16, 14, 16, 14)
        background = GradientDrawable().apply {
            setColor(panel)
            cornerRadius = 22f
            setStroke(1, Color.rgb(58, 49, 88))
        }
        layoutParams = LinearLayout.LayoutParams(-1, -2).apply {
            bottomMargin = 12
        }
    }

    private fun sectionTitle(value: String) = text(value, 13, purple, true).apply {
        setPadding(2, 16, 2, 8)
    }

    private fun space(height: Int) = TextView(this).apply {
        this.height = height
    }

    private fun actionButton(value: String, color: Int, action: () -> Unit) =
        Button(this).apply {
            text = value
            setTextColor(Color.WHITE)
            backgroundTintList = android.content.res.ColorStateList.valueOf(color)
            isAllCaps = false
            textSize = 14f
            setOnClickListener { action() }
            layoutParams = LinearLayout.LayoutParams(-1, -2).apply {
                topMargin = 5
                bottomMargin = 5
            }
        }

    private fun runExperiment(count: Int) {
        repeat(count) {
            if (Random.nextBoolean()) heads++ else tails++
        }
        totalTosses += count
        val headRate = heads * 100.0 / totalTosses
        val tailRate = tails * 100.0 / totalTosses
        resultView.text = String.format(
            Locale.US,
            "LATEST RUN: %d tosses\nSESSION TOTAL: %d\nHeads: %d (%.1f%%)\nTails: %d (%.1f%%)\n\nTheoretical probability: 50%% each. Your observed results can differ because of random variation.",
            count, totalTosses, heads, headRate, tails, tailRate
        )
        historyView.text = String.format(
            Locale.US,
            "Experiments completed: %d\nTotal simulated tosses: %d\nHeads: %d\nTails: %d",
            if (totalTosses == 0) 0 else 1, totalTosses, heads, tails
        )
    }

    private fun resetExperiment() {
        totalTosses = 0
        heads = 0
        tails = 0
        resultView.text = "Experiment reset. Choose a number of tosses to begin again."
        historyView.text = "No experiments recorded in this session yet."
    }
}
