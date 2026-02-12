package com.minhagrana.ui.presentation.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.entities.Category
import com.minhagrana.entities.Entry
import com.minhagrana.entities.EntryType
import com.minhagrana.entities.Month
import com.minhagrana.ui.components.BalanceItem
import com.minhagrana.ui.components.DialogEntryItem
import com.minhagrana.ui.components.EntryItem
import com.minhagrana.ui.components.Header1
import com.minhagrana.ui.components.MonthChanger
import com.minhagrana.ui.theme.AppTheme
import com.minhagrana.ui.theme.primaryLight

@Composable
fun EntriesScreen(
    showBottomSheetNav: Boolean,
    onEntrySelected: () -> Unit = {},
    onEntriesByYearSelected: () -> Unit = {},
) {
    var showBottomSheet by remember { mutableStateOf(showBottomSheetNav) }

    val month =
        Month(
            name = "Outubro",
            income = 5000.0,
            expense = 3000.0,
            entries =
                listOf(
                    Entry(
                        name = "Gasolina",
                        value = 200.0,
                        type = EntryType.INCOME,
                    ),
                    Entry(
                        name = "Gasolina",
                        value = 200.0,
                    ),
                    Entry(
                        name = "Gasolina",
                        value = 100.0,
                    ),
                    Entry(
                        name = "Gasolina",
                        value = 22200.0,
                    ),
                    Entry(
                        name = "Gasolina",
                        value = 1000.0,
                    ),
                    Entry(
                        name = "Salário",
                        value = 5500.0,
                        type = EntryType.INCOME,
                        category =
                            Category(
                                name = "Salário",
                                color = primaryLight,
                            ),
                    ),
                    Entry(
                        name = "Salário",
                        value = 100.0,
                    ),
                ),
        )
    Scaffold(
        modifier =
            Modifier.background(MaterialTheme.colorScheme.background),
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
        ) {
            Header1(
                title = "Meu balanço",
                actionText = "Ver balanço anual >",
                onClick = { onEntriesByYearSelected() },
            )
            MonthChanger(
                onNextPressed = { /*TODO*/ },
                onPreviousPressed = { /*TODO*/ },
                month = "Outubro",
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier =
                    Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxSize(),
            ) {
                month.entries.forEach {
                    EntryItem(
                        entry = it,
                        onClick = onEntrySelected,
                    )
                }
                Column(
                    modifier = Modifier.padding(top = 20.dp),
                ) {
                    BalanceItem(
                        month = month,
                    )
                }
                DialogEntryItem(
                    title = "Novo lançamento",
                    onConfirmSelected = {},
                    showBottomSheet = showBottomSheet,
                    onDismiss = { showBottomSheet = false },
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewEntriesScreen() {
    AppTheme {
        EntriesScreen(
            showBottomSheetNav = true,
        )
    }
}
