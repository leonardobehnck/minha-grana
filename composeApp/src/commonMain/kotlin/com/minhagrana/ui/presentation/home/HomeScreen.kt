package com.minhagrana.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.entities.Month
import com.minhagrana.entities.getDefaultColor
import com.minhagrana.ui.components.Balance
import com.minhagrana.ui.components.Header3
import com.minhagrana.ui.components.PieChart
import com.minhagrana.ui.processMonthDataByExpense
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen() {
    val month =
        Month(
            name = "Outubro",
            entries =
                listOf(
                    Entry(
                        name = "Filhos",
                        value = 150.0,
                        type = EntryType.EXPENSE,
                        category =
                            Category(
                                name = "Filhos",
                                color = getDefaultColor("Filhos"),
                            ),
                    ),
                    Entry(
                        name = "Filhos",
                        value = 150.0,
                        type = EntryType.EXPENSE,
                        category =
                            Category(
                                name = "Filhos",
                                color = getDefaultColor("Filhos"),
                            ),
                    ),
                    Entry(
                        name = "Gasolina",
                        value = 150.0,
                        type = EntryType.EXPENSE,
                        category =
                            Category(
                                name = "Transporte",
                                color = getDefaultColor("Transporte"),
                            ),
                    ),
                    Entry(
                        name = "Gatos",
                        value = 300.0,
                        type = EntryType.EXPENSE,
                        category =
                            Category(
                                color = getDefaultColor("Pets"),
                            ),
                    ),
                    Entry(
                        name = "Gatos",
                        value = 300.0,
                        type = EntryType.EXPENSE,
                        category =
                            Category(
                                name = "Saúde",
                                color = getDefaultColor("Saúde"),
                            ),
                    ),
                    Entry(
                        name = "Gatos",
                        value = 300.0,
                        type = EntryType.EXPENSE,
                        category =
                            Category(
                                name = "Saúde",
                                color = getDefaultColor("Saúde"),
                            ),
                    ),
                ),
        )

    Column(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.background),
    ) {
        Header3(
            title = "Leonardo",
        )
        Balance(
            balanceValue = 4000.0,
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(top = 16.dp)
                        .background(Color.White)
                        .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "relatório do mês",
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    val expenseByCategory = processMonthDataByExpense(month)

                    PieChart(
                        reportItems = expenseByCategory,
                    )
                }
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    val filteredCategories = month.entries.map { it.category }.toSet()
                    filteredCategories.forEachIndexed { _, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                modifier =
                                    Modifier
                                        .height(32.dp)
                                        .padding(start = 16.dp, top = 8.dp),
                                painter = painterResource(Res.drawable.compose_multiplatform),
                                contentDescription = null,
                                tint = item.color,
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                                text = item.name,
                            )
                        }
                    }
                }
            }
        }
    }
}
