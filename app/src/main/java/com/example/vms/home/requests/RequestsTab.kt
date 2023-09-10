package com.example.vms.home.requests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestsTab(
    requestsFlow: Flow<PagingData<Request>>,
    onRequestClick: (String) -> Unit = {},
    onRequestAccept: (String) -> Unit = {},
    onRequestDecline: (String) -> Unit = {},
    onRefreshData: () -> Unit
) {
    val requests: LazyPagingItems<Request> = requestsFlow.collectAsLazyPagingItems()
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefreshData)
    Box(
        Modifier.pullRefresh(pullRefreshState)
    ) {
        if (requests.loadState.refresh == LoadState.Loading) {
            LoadingView(modifier = Modifier.align(Alignment.Center))
            refreshing = true
        } else {
            refreshing = false
        }
        RequestsList(
            requests = requests,
            onRequestClick = onRequestClick,
            onRequestAccept = onRequestAccept,
            onRequestDecline = onRequestDecline
        )
        if (requests.loadState.append == LoadState.NotLoading(true)
            && requests.itemCount == 0
        ) {
            NoRequests()
        }
        if (requests.loadState.refresh is LoadState.Error) {
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
fun RequestsList(
    requests: LazyPagingItems<Request>,
    onRequestClick: (String) -> Unit,
    onRequestAccept: (String) -> Unit,
    onRequestDecline: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(count = requests.itemCount) { index ->
            val request = requests[index]
            if (request != null) {
                RequestItem(
                    request = request,
                    onClick = onRequestClick,
                    onRequestAccept = onRequestAccept,
                    onRequestDecline = onRequestDecline
                )
            }
        }

        if (requests.loadState.append == LoadState.Loading) {
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
fun NoRequests() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.you_have_no_requests)
        )
    }
}

@Composable
fun RequestItem(
    request: Request,
    onClick: (String) -> Unit,
    onRequestAccept: (String) -> Unit,
    onRequestDecline: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(request.id) },
        shape = Shapes.medium
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RequestDescription(stringResource(request.type.description), request.visitName)
            RequestButtons(request.id, onRequestAccept, onRequestDecline)
        }
    }
}

@Composable
fun RequestDescription(
    description: String,
    visitName: String
) {
    Column {
        Text(
            modifier = Modifier.padding(8.dp),
            text = description,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
            text = visitName
        )
    }
}

@Composable
fun RequestButtons(
    id: String,
    onRequestAccept: (String) -> Unit,
    onRequestDecline: (String) -> Unit,
) {
    Row {
        IconButton(onClick = { onRequestAccept(id) }) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_yes),
                contentDescription = stringResource(R.string.accept_request_button_content_description),
                tint = Color.Green
            )
        }
        IconButton(onClick = { onRequestDecline(id) }) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_no),
                contentDescription = stringResource(R.string.decline_request_button_content_description),
                tint = Color.Red
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VisitorManagementSystemTheme {
        RequestsTab(
            requestsFlow = flowOf(PagingData.from(testRequests)),
            onRefreshData = {}
        )
    }
}

val testRequests = listOf(
    Request(
        "0", RequestType.HOST_CHANGE, "wizyta prezesa"
    ),
    Request(
        "1", RequestType.INSTANT_VISIT, "kurier DHL"
    ),
)