package com.ProgressBarCompose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.ProgressBarCompose.data.IntervalConfig

@Composable
fun BottomContent(
    expanded: Boolean,
    valueToApply: String,
    intervalsExpanded: Boolean,
    intervals: List<IntervalConfig>,
    onExpandChange: () -> Unit,
    onValueChange: (String) -> Unit,
    onApplyClick: () -> Unit,

    // Callbacks para INTERVALOS
    onIntervalsExpandChange: () -> Unit,
    onIntervalChangeValue: (Int, String) -> Unit,
    onIntervalSecondsChange: (Int, String) -> Unit,
    onIntervalEnabledChange: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Controles",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        ApplyValuesSection(
            expanded = expanded,
            valueToApply = valueToApply,
            onExpandChange = onExpandChange,
            onValueChange = onValueChange,
            onApplyClick = onApplyClick
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Aplicar Intervalos",
                            modifier = Modifier.weight(1f),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        IconButton(
                            onClick = onIntervalsExpandChange
                        ) {
                            Icon(
                                imageVector = if (intervalsExpanded) {
                                    Icons.Default.ArrowDropUp
                                } else {
                                    Icons.Default.ArrowDropDown
                                },
                                contentDescription = "Expandir o contraer intervalos"
                            )
                        }
                    }
                }

                // Sección de INTERVALOS.
                if (intervalsExpanded) {
                    intervals.forEach { interval ->

                        IntervalCard(
                            intervalId = interval.id,
                            changeValue = interval.changeValue,
                            intervalSeconds = interval.intervalSeconds,
                            enabled = interval.enabled,
                            intervalError = interval.validationError,
                            onChangeValue = { value ->
                                onIntervalChangeValue(
                                    interval.id,
                                    value
                                )
                            },
                            onIntervalSecondsChange = { value ->
                                onIntervalSecondsChange(
                                    interval.id,
                                    value
                                )
                            },
                            onEnabledChange = { enabled ->
                                onIntervalEnabledChange(
                                    interval.id,
                                    enabled
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}