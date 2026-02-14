package com.minhagrana.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minhagrana.ui.getCurrentDate
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    iconRightVisibility: Boolean = true,
    onDateSelected: (String) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by rememberSaveable { mutableStateOf(getCurrentDate()) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Data",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
        if (iconRightVisibility) {
            Icon(
                modifier =
                    Modifier
                        .padding(top = 1.dp)
                        .size(12.dp)
                        .align(Alignment.CenterVertically),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Box(
            modifier =
                Modifier
                    .width(160.dp)
                    .height(36.dp)
                    .noRippleClickable { showDatePicker = true },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 10.dp),
                text = selectedDate,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        AnimatedVisibility(showDatePicker) {
            DatePickerDialog(
                modifier =
                    Modifier
                        .shadow(elevation = 10.dp),
                shape = RoundedCornerShape(1.dp),
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Text(
                        text = "Cancelar",
                        modifier =
                            Modifier
                                .padding(10.dp)
                                .noRippleClickable { showDatePicker = false },
                    )

                    Text(
                        text = "OK",
                        modifier =
                            Modifier
                                .padding(10.dp)
                                .noRippleClickable {
                                    datePickerState.selectedDateMillis?.let { millis ->
                                        val instant = Instant.fromEpochMilliseconds(millis)
                                        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                                        val day = localDate.day.toString().padStart(2, '0')
                                        val month = localDate.month.number.toString().padStart(2, '0')
                                        val year = localDate.year
                                        selectedDate = "$day/$month/$year"
                                        onDateSelected(selectedDate)
                                        showDatePicker = false
                                    }
                                },
                    )
                },
            ) {
                androidx.compose.material3.DatePicker(datePickerState)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePickerDocked() {
    DatePicker(
        onDateSelected = {
            println(it)
        },
    )
}
