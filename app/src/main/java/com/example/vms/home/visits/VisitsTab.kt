package com.example.vms.home.visits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.ui.theme.Shapes
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun VisitsTab(
    visits: List<Visit>,
    onVisitClick: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        visits.forEach {
            item {
                VisitItem(
                    visit = it,
                    onClick = onVisitClick
                )
            }
        }
    }
}

@Composable
fun VisitItem(
    visit: Visit,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(visit.id) },
        shape = Shapes.medium
    ) {
        Column {
            Text(
                modifier = Modifier.padding(8.dp),
                text = visit.title,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                text = "${visit.start.format(visitItemDateFormatter)}, ${visit.start.format(
                    visitItemTimeFormatter
                )} - ${visit.end.format(visitItemTimeFormatter)}"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VisitorManagementSystemTheme {
        VisitsTab(
            visits = testVisits,
        )
    }
}

private val visitItemDateFormatter = DateTimeFormatter.ofPattern("dd MMM")
private val visitItemTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val testVisits = listOf(
    Visit(
        0, "wizyta prezesa",
        LocalDateTime.now(), LocalDateTime.now().plusHours(1)
    ),
    Visit(
        0, "jakas inna wizyta",
        LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3)
    ),
)