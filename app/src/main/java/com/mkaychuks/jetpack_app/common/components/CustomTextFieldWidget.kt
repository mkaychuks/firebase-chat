package com.mkaychuks.jetpack_app.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.mkaychuks.jetpack_app.ui.theme.PurpleBackground
import com.mkaychuks.jetpack_app.ui.theme.fontFamily

@Composable
fun CustomTextFieldWidget(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable() (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorText: String = "",
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        maxLines = 1,
        label = {
            Text(
                text = label, fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = PurpleBackground,
            )
        },
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = PurpleBackground,
        ),
        modifier = modifier.fillMaxWidth(),
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        visualTransformation = visualTransformation,
        isError = isError,
        supportingText = {
            Text(text = if (isError) errorText else "", fontFamily = fontFamily)
        }
    )
}