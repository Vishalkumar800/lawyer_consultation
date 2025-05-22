package com.rach.lawyerapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rach.lawyerapp.R

val interFontFamily = FontFamily(
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_semibold, FontWeight.SemiBold)
)

val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_regular, FontWeight.Normal)
)

val kufamFontFamily = FontFamily(
    Font(R.font.kufam_semi_bold,FontWeight.SemiBold)
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    displayLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    titleLarge = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    displayMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp
    ),
    titleMedium = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp
    ),
    displaySmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    )

)

val SemiboldAndInter = TextStyle(
    fontFamily = interFontFamily,
    fontWeight = FontWeight.SemiBold
)

val boldRaiText =  TextStyle(
    fontFamily = kufamFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 22.sp
)

val interJiFontBold = TextStyle(
    fontSize = 30.sp,
    fontFamily = interFontFamily,
    fontWeight = FontWeight.Bold
)

val interJiSemiBold = TextStyle(
    fontSize = 26.sp,
    fontFamily = interFontFamily,
    fontWeight = FontWeight.SemiBold
)

val buttonText =  TextStyle(
    fontSize = 18.sp,
    fontFamily = poppinsFontFamily,
    fontWeight = FontWeight.Medium
)

