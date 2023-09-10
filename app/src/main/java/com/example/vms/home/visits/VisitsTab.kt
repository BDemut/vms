package com.example.vms.home.visits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.vms.R
import com.example.vms.ui.ErrorMessage
import com.example.vms.ui.LoadingView
import com.example.vms.ui.theme.Shapes
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VisitsTab(
    visitsFlow: Flow<PagingData<Visit>>,
    onVisitClick: (String) -> Unit,
    onRefreshData: () -> Unit
) {
    val visits: LazyPagingItems<Visit> = visitsFlow.collectAsLazyPagingItems()
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefreshData)
    Box(
        Modifier.pullRefresh(pullRefreshState)
    ) {
        if (visits.loadState.refresh == LoadState.Loading) {
            LoadingView(modifier = Modifier.align(Alignment.Center))
            refreshing = true
        } else {
            refreshing = false
        }
        VisitsList(visits = visits, onVisitClick)
        if (visits.loadState.append == LoadState.NotLoading(true)
            && visits.itemCount == 0
        ) {
            NoVisits()
        }
        if (visits.loadState.refresh is LoadState.Error) {
            ErrorMessage(
                modifier = Modifier.align(Alignment.Center),
                onRetry = onRefreshData
            )
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = false,
            state = pullRefreshState
        )
    }
}

@Composable
fun VisitsList(
    visits: LazyPagingItems<Visit>,
    onVisitClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(count = visits.itemCount) { index ->
            val visit = visits[index]
            if (visit != null) {
                VisitItem(
                    visit = visit,
                    onClick = onVisitClick
                )
            }
        }

        if (visits.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun NoVisits() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.you_have_no_visits)
        )
    }
}

@Composable
fun VisitItem(
    visit: Visit,
    onClick: (String) -> Unit
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
                text = "${visit.start.format(visitItemDateFormatter)} • ${
                    visit.start.format(
                        visitItemTimeFormatter
                    )
                } - ${visit.end.format(visitItemTimeFormatter)}"
            )
            if (visit.isCancelled) {
                CancelledChip()
            }
        }
    }
}

@Preview
@Composable
fun CancelledChip() {
    Surface(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colors.error
    ) {
        Text(
            text = stringResource(id = R.string.visit_cancelled),
            modifier = Modifier.padding(10.dp, 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VisitorManagementSystemTheme {
        VisitsTab(
            visitsFlow = flowOf(PagingData.from(testVisits)),
            onVisitClick = {},
            onRefreshData = {}
        )
    }
}

private val visitItemDateFormatter = DateTimeFormatter.ofPattern("dd MMM")
private val visitItemTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val testVisits = listOf(
    Visit(
        "0", "wizyta prezesa",
        LocalDateTime.now(), LocalDateTime.now().plusHours(1),
        false
    ),
    Visit(
        "1", "jakas inna wizyta",
        LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
        true
    ),
)