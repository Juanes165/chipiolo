package com.aragang.chipiolo.Profile

import android.graphics.Picture
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aragang.chipiolo.R
import com.aragang.chipiolo.avatarList
import com.aragang.chipiolo.returnImageResource

@Composable
fun ProfilePicture(profilePictureUrl: String, size: Dp, clickableFun: () -> Unit = {}) {
    if(profilePictureUrl !in avatarList) {
        AsyncImage(
            model = profilePictureUrl,
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(
                    BorderStroke(4.dp, Color.White),
                    CircleShape
                )
                .padding(4.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { clickableFun() },
            contentScale = ContentScale.Crop
        )
    }
    else{
        val image = returnImageResource(profilePictureUrl)
        Image(
            painter = painterResource(image),
            contentDescription = "Default profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .border(
                    BorderStroke(4.dp, Color.White),
                    CircleShape
                )
                .padding(4.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { clickableFun() }
        )
    }
}