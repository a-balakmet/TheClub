package com.the.club.ui.presentation.promotions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.glide.GlideImage
import com.the.club.domain.model.promos.Promotion
import com.the.club.ui.commonComponents.toAnimatedBrush
import com.the.club.ui.theme.GrayColorShades

@Composable
fun PromotionItem(promotion: Promotion, onClick: (Long) -> Unit) {
    val imageOptions = RequestOptions().override(1265, 517)
    GlideImage(
        imageModel = promotion.imageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .height(142.dp)
            .clickable { onClick(promotion.id) }
            .background(brush = GrayColorShades.toAnimatedBrush()),
        requestOptions = { imageOptions },
        contentScale = ContentScale.FillBounds
    )
}