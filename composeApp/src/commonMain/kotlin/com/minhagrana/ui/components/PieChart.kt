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
    val totalValue = reportItems.values.sum()
    val proportions = reportItems.values.map { it / totalValue }
    val angles = proportions.map { it * 360f }

    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val circleSize = 400f
            val offset =
                Offset(
                    x = (canvasWidth - circleSize) / 2,
                    y = (canvasHeight - circleSize) / 2,
                )

            var startAngle = -90f

            angles.forEachIndexed { index, sweepAngle ->
                drawArc(
                    style = Stroke(width = 100f),
                    color = reportItems.keys.elementAt(index).color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle.toFloat(),
                    useCenter = false,
                    size = Size(circleSize, circleSize),
                    topLeft = offset,
                )
                startAngle += sweepAngle.toFloat()
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
