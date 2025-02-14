package com.toast.android.gamebase.sample.ui.access_info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.toast.android.gamebase.sample.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.toast.android.gamebase.sample.data.AccessInformation
import com.toast.android.gamebase.sample.data.AccessInformationRepository
import com.toast.android.gamebase.sample.ui.theme.*

@Composable
fun AccessInformationScreen(updateVersionInPreferenceAndState: () -> Unit) {
    val scrollState = rememberScrollState()

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.access_info_screen_padding_top)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.access_info_title),
                    color = Black,
                    fontSize = dimensionResource(id = R.dimen.access_info_title_font_size).value.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.access_info_sub_title_padding_top)),
                    text = stringResource(R.string.access_info_subtitle),
                    color = Grey500,
                    fontSize = dimensionResource(id = R.dimen.access_info_sub_title_font_size).value.sp,
                    fontWeight = FontWeight(300),
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.access_info_content_padding))
            ) {
                Text(
                    text = stringResource(R.string.access_info_list_title),
                    color = Black,
                    fontSize = dimensionResource(id = R.dimen.access_info_content_font_size).value.sp,
                    fontWeight = FontWeight(350),
                    textAlign = TextAlign.Start
                )
                AccessInformationRepository().getAccessInformation().forEach { AccessInformation ->
                    ListAccessInformation(information = AccessInformation)
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.access_info_button_spacer)))
            Button(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.access_info_button_height))
                    .fillMaxSize(),
                onClick = updateVersionInPreferenceAndState,
                colors = ButtonDefaults.buttonColors(backgroundColor = SkyBlue),
            ) {
                Text(
                    text = stringResource(R.string.button_ok),
                    color = White,
                    fontSize = dimensionResource(id = R.dimen.access_info_button_font_size).value.sp,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ListAccessInformation(information: AccessInformation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.access_info_list_padding)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.access_info_list_item_space))
    ) {
        Image(
            painter = painterResource(information.imgSrc),
            contentDescription = stringResource(information.contentDescription),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.access_info_image_icon_size))
                .height(dimensionResource(id = R.dimen.access_info_image_icon_size))
                .background(color = Grey300, shape = CircleShape)
                .padding(dimensionResource(id = R.dimen.access_info_image_icon_padding))
        )
        Column {
            Text(
                text = stringResource(information.title),
                color = Black,
                fontSize = dimensionResource(id = R.dimen.access_info_list_item_title_font_size).value.sp,
                fontWeight = FontWeight(300)
            )
            Text(
                text = stringResource(information.subTitle),
                color = Grey500,
                fontSize = dimensionResource(id = R.dimen.access_info_list_item_sub_title_font_size).value.sp,
                fontWeight = FontWeight(300)
            )
        }
    }
}