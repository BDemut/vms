package com.example.vms.home.requests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.R
import com.example.vms.ui.theme.Shapes
import com.example.vms.ui.theme.VisitorManagementSystemTheme


@Composable
fun RequestsTab(
    requests: List<Request>,
    onRequestClick: (String) -> Unit = {},
    onRequestAccept: (String) -> Unit = {},
    onRequestDecline: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        requests.forEach {
            item {
                RequestItem(
                    request = it,
                    onClick = onRequestClick,
                    onRequestAccept = onRequestAccept,
                    onRequestDecline = onRequestDecline
                )
            }
        }
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
            requests = testRequests,
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