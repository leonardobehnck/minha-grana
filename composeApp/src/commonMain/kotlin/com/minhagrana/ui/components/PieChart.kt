package com.minhagrana.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.minhagrana.entities.Category
import com.minhagrana.ui.theme.AppTheme

@Composable
fun PieChart(reportItems: Map<Category, Double>) {
    val entries = reportItems.entries.toList()
    val totalValue = entries.sumOf { it.value }
    val segments: List<Pair<Category, Float>> =
        if (totalValue > 0) {
            entries.map { (category, value) ->
                category to (value / totalValue * 360).toFloat()
            }
        } else {
            emptyList()
        }

    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val circleSize = minOf(canvasWidth, canvasHeight) * 0.85f
            val offset =
                Offset(
                    x = (canvasWidth - circleSize) / 2,
                    y = (canvasHeight - circleSize) / 2,
                )

            var startAngle = -90f

            segments.forEach { (category, sweepAngle) ->
                drawArc(
                    style = Stroke(width = circleSize * 0.25f),
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    size = Size(circleSize, circleSize),
                    topLeft = offset,
                )
                startAngle += sweepAngle
            }
        },
    )
}

@Preview
@Composable
fun PreviewCircularGraphic() {
    AppTheme {
        val sampleData =
            mapOf(
                Category(name = "Food") to 500.0,
                Category(name = "Rent") to 1200.0,
                Category(name = "Utilities") to 300.0,
            )
        PieChart(
            reportItems = sampleData,
        )
    }
}
