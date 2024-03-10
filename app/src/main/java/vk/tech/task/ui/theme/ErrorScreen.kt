package vk.tech.task.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vk.tech.task.R

@Composable
fun ErrorScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_error_24),
            contentDescription = "",
            tint = Color.Red,
            modifier = Modifier.size(width = 128.dp, height = 128.dp)
        )
        Text(
            text = "Ooops ... Something going wrong",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color.Red
        )
        Text(
            text = "Try again later",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color.Red
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorScreenPreview() {
    ErrorScreen()
}