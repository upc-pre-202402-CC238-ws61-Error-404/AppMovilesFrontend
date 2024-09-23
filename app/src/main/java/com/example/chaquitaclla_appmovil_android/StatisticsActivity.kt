package com.example.chaquitaclla_appmovil_android

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.chaquitaclla_appmovil_android.statistics.StatisticsService
import com.example.chaquitaclla_appmovil_android.statistics.beans.StatisticBar
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticsActivity : AppCompatActivity() {
    private lateinit var statisticsService: StatisticsService
    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        statisticsService = StatisticsService()
        barChart = findViewById(R.id.bar_chart)
        pieChart = findViewById(R.id.pie_chart)

        lifecycleScope.launch {
            val statisticBars = withContext(Dispatchers.IO) {
                statisticsService.getQuantityOfCrops()
            }
            Log.d("com.example.chaquitaclla_appmovil_android.StaticsActivity", "Statistic Bars: $statisticBars")
            setupBarChart(statisticBars)

            val pieEntries = withContext(Dispatchers.IO) {
                statisticsService.getQuantityOfControlsBySowingId()
            }
            Log.d("com.example.chaquitaclla_appmovil_android.StaticsActivity", "Pie Entries: $pieEntries")
            setupPieChart(pieEntries)
        }
    }

    /**
     * Setup the Bar Chart with the given StatisticBars
     * @param statisticBars List of StatisticBars
     * @see StatisticBar
     * This function allows to setup the Bar Chart with the given StatisticBars
     */
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
        barChart.data = barData
        barChart.invalidate() // Refresh the chart
    }


    /**
     * Setup the Pie Chart with the given PieEntries
     * @param pieEntries List of PieEntries
     * @see PieEntry
     * This function allows to setup the Pie Chart with the given PieEntries
     * TODO: The name of sowings(crops) is determined by their ID.
     */
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