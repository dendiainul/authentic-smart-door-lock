package com.authentic.smartdoor.dashboard.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ChartDataPoint(
    val label: String,
    val value: Int,
    val timestamp: Long
)

data class AnalyticsChartData(
    val dataPoints: List<ChartDataPoint>,
    val maxValue: Int,
    val totalAccess: Int
)

@Composable
fun AnalyticsChart(
    chartData: AnalyticsChartData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Chart Title
            Text(
                text = "Perkembangan Total Akses",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    fontSize = 16.sp
                )
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Chart Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (chartData.dataPoints.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Tidak ada data",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF9E9E9E)
                                )
                            )
                            Text(
                                text = "Data: ${chartData.totalAccess} akses",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF9E9E9E)
                                )
                            )
                        }
                    }
                } else {
                    // Custom Canvas Chart
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Chart Canvas
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 8.dp)
                        ) {
                            drawChart(chartData)
                        }
                        
                        // Labels
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            chartData.dataPoints.forEach { dataPoint ->
                                Text(
                                    text = dataPoint.label,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF6B6B6B),
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(Modifier.height(8.dp))
            
            // Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: ${chartData.totalAccess} akses",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF6B6B6B),
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = "Tertinggi: ${chartData.maxValue}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF6B6B6B),
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

private fun DrawScope.drawChart(chartData: AnalyticsChartData) {
    val canvasWidth = size.width
    val canvasHeight = size.height
    val padding = 20f
    val chartWidth = canvasWidth - (padding * 2)
    val chartHeight = canvasHeight - (padding * 2)
    
    if (chartData.dataPoints.isEmpty()) return
    
    val barWidth = (chartWidth / chartData.dataPoints.size) * 0.7f
    val barSpacing = (chartWidth / chartData.dataPoints.size) * 0.3f
    
    // Draw grid lines first
    val gridLines = 4
    for (i in 0..gridLines) {
        val y = padding + (chartHeight / gridLines * i)
        drawLine(
            color = Color(0xFFE0E0E0).copy(alpha = 0.3f),
            start = Offset(padding, y),
            end = Offset(padding + chartWidth, y),
            strokeWidth = 1f
        )
    }
    
    // Draw bars
    chartData.dataPoints.forEachIndexed { index, dataPoint ->
        val barHeight = if (chartData.maxValue > 0) {
            (dataPoint.value.toFloat() / chartData.maxValue.toFloat() * chartHeight).coerceAtLeast(2f)
        } else {
            2f
        }
        
        val x = padding + (index * (barWidth + barSpacing)) + (barSpacing / 2)
        val y = padding + chartHeight - barHeight
        
        // Draw bar with rounded corners effect
        drawRect(
            color = if (dataPoint.value > 0) Color(0xFF6C63FF) else Color(0xFFE0E0E0),
            topLeft = Offset(x, y),
            size = Size(barWidth, barHeight)
        )
    }
}

// Utility functions for generating chart data based on time period
object ChartDataGenerator {
    
    fun generateDailyData(accessLogs: List<com.authentic.smartdoor.dashboard.domain.model.AccessLog>): AnalyticsChartData {
        val hourlyData = mutableMapOf<Int, Int>()
        
        // Initialize all hours with 0
        for (hour in 0..23) {
            hourlyData[hour] = 0
        }
        
        // Count access logs by hour
        accessLogs.forEach { log ->
            val timestamp = getTimestampFromLog(log.timestamp)
            if (timestamp > 0) {
                val hour = getHourFromTimestamp(timestamp)
                hourlyData[hour] = hourlyData[hour]!! + 1
            }
        }
        
        // Create data points for every 3 hours (00, 03, 06, 09, 12, 15, 18, 21)
        val dataPoints = mutableListOf<ChartDataPoint>()
        for (hour in 0..23 step 3) {
            val value = hourlyData[hour] ?: 0
            dataPoints.add(
                ChartDataPoint(
                    label = String.format("%02d", hour),
                    value = value,
                    timestamp = getTimestampForHour(hour)
                )
            )
        }
        
        val maxValue = dataPoints.maxOfOrNull { it.value } ?: 0
        val totalAccess = dataPoints.sumOf { it.value }
        
        // Debug logging
        println("ChartDataGenerator.generateDailyData:")
        println("  Access logs count: ${accessLogs.size}")
        println("  Data points: ${dataPoints.size}")
        println("  Max value: $maxValue")
        println("  Total access: $totalAccess")
        dataPoints.forEach { point ->
            println("  ${point.label}: ${point.value}")
        }
        
        return AnalyticsChartData(dataPoints, maxValue, totalAccess)
    }
    
    fun generateWeeklyData(accessLogs: List<com.authentic.smartdoor.dashboard.domain.model.AccessLog>): AnalyticsChartData {
        val dailyData = mutableMapOf<String, Int>()
        
        // Initialize all days with 0
        val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
        days.forEach { day ->
            dailyData[day] = 0
        }
        
        // Count access logs by day of week
        accessLogs.forEach { log ->
            val timestamp = getTimestampFromLog(log.timestamp)
            if (timestamp > 0) {
                val dayOfWeek = getDayOfWeekFromTimestamp(timestamp)
                dailyData[dayOfWeek] = dailyData[dayOfWeek]!! + 1
            }
        }
        
        // Create data points for each day
        val dataPoints = days.map { day ->
            ChartDataPoint(
                label = day,
                value = dailyData[day] ?: 0,
                timestamp = getTimestampForDay(day)
            )
        }
        
        val maxValue = dataPoints.maxOfOrNull { it.value } ?: 0
        val totalAccess = dataPoints.sumOf { it.value }
        
        return AnalyticsChartData(dataPoints, maxValue, totalAccess)
    }
    
    fun generateMonthlyData(accessLogs: List<com.authentic.smartdoor.dashboard.domain.model.AccessLog>): AnalyticsChartData {
        val weeklyData = mutableMapOf<Int, Int>()
        
        // Initialize all weeks with 0
        for (week in 1..4) {
            weeklyData[week] = 0
        }
        
        // Count access logs by week of month
        accessLogs.forEach { log ->
            val timestamp = getTimestampFromLog(log.timestamp)
            if (timestamp > 0) {
                val week = getWeekOfMonthFromTimestamp(timestamp)
                if (week in 1..4) {
                    weeklyData[week] = weeklyData[week]!! + 1
                }
            }
        }
        
        // Create data points for each week
        val dataPoints = (1..4).map { week ->
            ChartDataPoint(
                label = "M$week",
                value = weeklyData[week] ?: 0,
                timestamp = getTimestampForWeek(week)
            )
        }
        
        val maxValue = dataPoints.maxOfOrNull { it.value } ?: 0
        val totalAccess = dataPoints.sumOf { it.value }
        
        return AnalyticsChartData(dataPoints, maxValue, totalAccess)
    }
    
    // Helper functions to extract data from access logs
    private fun getTimestampFromLog(timestamp: String): Long {
        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(timestamp)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
    
    private fun getHourFromTimestamp(timestamp: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
    
    private fun getDayOfWeekFromTimestamp(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Sen"
            Calendar.TUESDAY -> "Sel"
            Calendar.WEDNESDAY -> "Rab"
            Calendar.THURSDAY -> "Kam"
            Calendar.FRIDAY -> "Jum"
            Calendar.SATURDAY -> "Sab"
            Calendar.SUNDAY -> "Min"
            else -> "Sen"
        }
    }
    
    private fun getWeekOfMonthFromTimestamp(timestamp: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.WEEK_OF_MONTH)
    }
    
    private fun getTimestampForHour(hour: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    private fun getTimestampForDay(day: String): Long {
        val calendar = Calendar.getInstance()
        val dayOfWeek = when (day) {
            "Sen" -> Calendar.MONDAY
            "Sel" -> Calendar.TUESDAY
            "Rab" -> Calendar.WEDNESDAY
            "Kam" -> Calendar.THURSDAY
            "Jum" -> Calendar.FRIDAY
            "Sab" -> Calendar.SATURDAY
            "Min" -> Calendar.SUNDAY
            else -> Calendar.MONDAY
        }
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        return calendar.timeInMillis
    }
    
    private fun getTimestampForWeek(week: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.WEEK_OF_MONTH, week)
        return calendar.timeInMillis
    }
}
