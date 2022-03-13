package com.the.club.ui.presentation.clubs.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import com.the.club.R
import com.the.club.domain.model.clubs.Club
import com.the.club.ui.commonComponents.toAnimatedBrush
import com.the.club.ui.theme.GrayColorShades
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.gray
import com.the.club.ui.theme.green

@Composable
fun ClubItem(club: Club, onClick: (Club) -> Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(club) }
    ) {
        Image(
            painter = rememberGlidePainter(
                request = club.imageUrl,
                previewPlaceholder = R.drawable.ic_logo
            ),
            contentDescription = "club image",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush = GrayColorShades.toAnimatedBrush()),
            contentScale = ContentScale.Crop
        )
        Text(
            text = club.name,
            style = Typography.body1,
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(horizontal = 16.dp)
        )
        if (club.isMember) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "membership",
                tint = green,
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            )
        }
        Spacer(modifier = Modifier.weight(1F))
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "open clubs",
            tint = gray,
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
    }
}