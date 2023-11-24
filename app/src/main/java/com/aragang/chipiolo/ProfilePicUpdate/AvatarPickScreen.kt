package com.aragang.chipiolo.ProfilePicUpdate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragang.chipiolo.R
import com.aragang.chipiolo.SignInChipiolo.Login

@Composable
fun PickAvatarScreen(
    client: Login,
    onSuccess: () -> Unit,
) {

    val imagesList = listOf(
        R.drawable.avatar0,
        R.drawable.avatar1,
        R.drawable.avatar2,
        R.drawable.avatar3,
        R.drawable.avatar4,
        R.drawable.avatar5,
        R.drawable.avatar6,
        R.drawable.avatar7,
        R.drawable.avatar8,
        R.drawable.avatar9
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0, 97, 23, 255))
    ) {
        // formulario de email y password
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.pick_avatar),
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(vertical = 16.dp),
                fontWeight = FontWeight.Bold
            )

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
            ) {
                items(imagesList.size) { index ->
                    Image(
                        painter = painterResource(id = imagesList[index]),
                        contentDescription = "avatar$index",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .border(
                                BorderStroke(4.dp, Color.White),
                                CircleShape
                            )
                            .padding(10.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { client.updatePhotoUrl("avatar$index"); onSuccess() }
                    )
                }
            }

        }
    }
}