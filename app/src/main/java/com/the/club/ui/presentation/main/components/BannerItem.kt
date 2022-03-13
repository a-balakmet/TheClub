package com.the.club.ui.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.the.club.domain.model.Banner
import com.the.club.ui.commonComponents.toAnimatedBrush
import com.the.club.ui.theme.GrayColorShades

@Composable
fun BannerItem(index: Int, maxIndex: Int, banner: Banner, onClick: (String) -> Unit) {
    val imageOptions = RequestOptions().override(1265, 347)
    banner.imageUrl?.let {
        GlideImage(
            imageModel = it,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (index == 0) 24.dp else 8.dp, end = if (index == maxIndex) 24.dp else 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .size(width = 284.dp, height = 103.dp)
                .clickable { banner.bannerLink?.let { bannerLink-> onClick(bannerLink) } }
                .background(brush = GrayColorShades.toAnimatedBrush()),
            requestOptions = { imageOptions },
            contentScale = ContentScale.FillBounds
        )
    }
}