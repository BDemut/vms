package com.example.vms.requestdetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vms.R
import com.example.vms.home.HomeScreen
import com.example.vms.home.HomeViewModel
import com.example.vms.home.Tab
import com.example.vms.home.requests.Request
import com.example.vms.home.requests.RequestType
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import com.example.vms.visitdetails.VisitDetailsActivity
import kotlinx.coroutines.launch

class RequestDetailsActivity : ComponentActivity() {
    //private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                Scaffold(
                    topBar = {
                        RequestDetailsTopBar(
                            requestName = stringResource(id = RequestType.HOST_CHANGE.description),
                            onBackPressed = { }
                        )
                    },
                    bottomBar = {
                        RequestDetailsButtons(onAccept = { /*TODO*/ }) {

                        }
                    }
                ) {
                    //TODO visit details
                }
            }
        }
    }

    companion object {
        private const val ARG_REQUEST_ID = "requestId"

        fun getLaunchIntent(context: Context, requestId: String): Intent {
            val intent = Intent(context, RequestDetailsActivity::class.java)
            intent.putExtra(ARG_REQUEST_ID, requestId)
            return intent
        }
    }
}

@Composable
private fun RequestDetailsScreen() {

}

@Composable
private fun RequestDetailsTopBar(
    requestName: String,
    onBackPressed: () -> Unit
) {
    TopAppBar {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back arrow"
            )
        }
        Text(
            text = requestName,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RequestDetailsButtons(
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onAccept) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_yes),
                contentDescription = stringResource(R.string.accept_request_button_content_description),
                tint = Color.Green
            )
        }
        IconButton(onClick = onDecline) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_no),
                contentDescription = stringResource(R.string.decline_request_button_content_description),
                tint = Color.Red
            )
        }
    }
}