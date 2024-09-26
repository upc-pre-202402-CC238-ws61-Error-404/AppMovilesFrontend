package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.chaquitaclla_appmovil_android.statistics.StatisticsService
import com.example.chaquitaclla_appmovil_android.statistics.beans.StatisticBar
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsActivity : BaseActivity() {
    private lateinit var statisticsService: StatisticsService
    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_statistics, findViewById(R.id.container))

        //Here we are starting the bottom navigation view with the statistics item selected
        //once the activity is created
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_statistics

        statisticsService = StatisticsService(this)
        barChart = findViewById(R.id.bar_chart)
        pieChart = findViewById(R.id.pie_chart)
        progressBar = findViewById(R.id.progressBar)

        fetchData()
    }

    private fun fetchData() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val statisticBars = withContext(Dispatchers.IO) {
                    statisticsService.getQuantityOfCropsByDB()
                }
                Log.d("com.example.chaquitaclla_appmovil_android.StaticsActivity", "Statistic Bars: $statisticBars")
                setupBarChart(statisticBars)

                val pieEntries = withContext(Dispatchers.IO) {
                    statisticsService.getQuantityOfControlsBySowingIdByDB()
                }
                Log.d("com.example.chaquitaclla_appmovil_android.StaticsActivity", "Pie Entries: $pieEntries")
                setupPieChart(pieEntries)
            } catch (e: Exception) {
                Log.e("com.example.chaquitaclla_appmovil_android.StaticsActivity", "Error loading data", e)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupBarChart(statisticBars: List<StatisticBar>) {
        val entries = statisticBars.mapIndexed { index, statisticBar ->
            BarEntry(index.toFloat(), statisticBar.value)
        }
        val colors = listOf(
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA,
            Color.CYAN, Color.GRAY, Color.DKGRAY, Color.LTGRAY, Color.BLACK
        )
        val dataSet = BarDataSet(entries, "Crops")
        dataSet.colors = colors
        val barData = BarData(dataSet)

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(statisticBars.map { it.name })
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawLabels(true)

        barChart.data = barData
        barChart.description.isEnabled = false // Disable the description label
        barChart.setFitBars(true) // Make the x-axis fit exactly all bars
        barChart.invalidate() // Refresh the chart
    }

    private fun setupPieChart(pieEntries: List<PieEntry>) {
        val dataSet = PieDataSet(pieEntries, "Controls per Sowing")

        val colors = listOf(
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA,
            Color.CYAN, Color.GRAY, Color.DKGRAY, Color.LTGRAY, Color.BLACK
        )
        dataSet.colors = colors

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate() // Refresh the chart
    }
}