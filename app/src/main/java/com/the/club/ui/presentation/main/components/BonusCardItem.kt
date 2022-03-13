package com.the.club.ui.presentation.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.the.club.R
import com.the.club.common.ktx.mergeNumber
import com.the.club.common.ktx.toChunked
import com.the.club.data.remote.bonusCards.dto.toBitmap
import com.the.club.domain.model.BonusCard
import com.the.club.ui.commonComponents.toMainCardBrush
import com.the.club.ui.theme.*

@Composable
fun BonusCardItem(modifier: Modifier, bonusCard: BonusCard) {
    val brush =
        if (bonusCard.type == 2) OptCardColors.toMainCardBrush()
        else MainCardColors.toMainCardBrush()
    Column(
        modifier = modifier
            .background(brush = brush, shape = Shapes.large)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            // type
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(4.dp)
            ) {
                Text(
                    text = "The",
                    color =
                    if (bonusCard.type == 2) Color(0xFFf7e5bd)
                    else Color(0xFFE22576),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text =
                    if (bonusCard.type == 2) "Opt"
                    else "Club",
                    color =
                    if (bonusCard.type == 2) Color(0xFFf7e5bd)
                    else Color(0xFFE22576),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 24.dp, end = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.my_bonuses_capital),
                    color = Color.White,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = bonusCard.balance.toString(),
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .background(color = Color.White, shape = Shapes.large)
                .height(52.dp)
        ) {
            // barcode
            bonusCard.barcode?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "barcode",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp)
                        .align(Alignment.Center)
                )
            }
        }
        // number and status
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // card number
            Text(
                text = bonusCard.cardNumber,
                //text = ("${bonusCard.cardNumber}***").toChunked(),
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1F)
            )
            // card status: active or not
            Text(
                text =
                if (bonusCard.isActive) stringResource(id = R.string.card_active)
                else stringResource(id = R.string.card_blocked),
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun NoNetBonusCard(bonusCard: BonusCard) {
    val brush = GrayColorShades.toMainCardBrush()
    Column(
        modifier = Modifier
            .background(brush = brush, shape = Shapes.large)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            // type
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(4.dp)
            ) {
                Text(
                    text = "Club",
                    color = black,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text =
                    if (bonusCard.type == 2) "Opt"
                    else "Club",
                    color = black,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 24.dp, end = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.my_bonuses_capital),
                    color = black,
                    fontSize = 10.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = bonusCard.balance.toString(),
                    color = black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .background(color = Color.White, shape = Shapes.large)
                .height(52.dp)
            //
        ) {
            // barcode
            mergeNumber(bonusCard.cardNumber, bonusCard.cvs).toBitmap()?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "barcode",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp)
                )
            }
        }
        // number and status
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // card number
            Text(
                text = ("${bonusCard.cardNumber}***").toChunked(4),
                //text = bonusCard.cardNumber,
                color = black,
                fontSize = 16.sp,
                modifier = Modifier.weight(1F)
            )
            // card status: active or not
            Text(
                text =
                if (bonusCard.isActive) stringResource(id = R.string.card_active)
                else stringResource(id = R.string.card_blocked),
                color = black,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}