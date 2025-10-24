package com.barakat.banquemasrlogin

import android.R.attr.onClick
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barakat.banquemasrlogin.ui.theme.BanqueMasrLogInTheme
import com.barakat.banquemasrlogin.ui.theme.Blue
import com.barakat.banquemasrlogin.ui.theme.Red
import java.util.Locale
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BanqueMasrLogInTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginDesign(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    fun changeAppLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        resources.updateConfiguration(configuration, resources.displayMetrics)

        recreate()
    }

}

@Composable
fun LoginDesign(modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.bm_icon),
                contentDescription = "Banque Masr logo"
            )
            Text(
                text = stringResource(R.string.arabic),
                color = Red,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                modifier = Modifier.clickable {
                    val currentLang = Locale.getDefault().language
                    val newLang = if (currentLang == "ar") "en" else "ar"

                    if (context is MainActivity) {
                        context.changeAppLanguage(newLang)
                    }
                }
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Text(
            text = stringResource(R.string.forgot_password),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(top = 28.dp)
        )
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Red),
            shape = RoundedCornerShape(10.dp),
            enabled = password.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp)

        ) {
            Text(
                text = stringResource(R.string.login),
                fontWeight = FontWeight.Bold
            )
        }
        val annotatedText = buildAnnotatedString {
            append("${stringResource(R.string.need_help)} ")

            pushStringAnnotation(
                tag = "CONTACT",
                annotation = "19888"
            )

            withStyle(
                style = SpanStyle(
                    color = Red,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(stringResource(R.string.contact_us))
            }

            pop()
        }

        // نستخدم ClickableText بدل Text
        ClickableText(
            text = annotatedText,
            modifier = Modifier.padding(top = 24.dp),
            onClick = { offset ->
                annotatedText
                    .getStringAnnotations(
                        tag = "CONTACT",
                        start = offset,
                        end = offset
                    )
                    .firstOrNull()
                    ?.let { annotation ->
                        // نستدعي الدالة اللي حضرتك مجهزها
                        openDialScreen(context, annotation.item)
                    }
            }
        )
        Divider(
            modifier = Modifier
                .padding(top = 48.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            ProductItem(R.drawable.our_products, stringResource(R.string.our_products))
            ProductItem(R.drawable.exchange_rate, stringResource(R.string.exchange_rate))
            ProductItem(R.drawable.security_tips, stringResource(R.string.security_tips))
            ProductItem(R.drawable.nearest_branch_or_atm, stringResource(R.string.nearest_branch))
        }
    }
}

@Composable
fun ProductItem(imageRes: Int, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = title,
            modifier = Modifier.size(70.dp)
        )
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            softWrap = true,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }

}
private fun openDialScreen(context: Context , phoneNumber: String){
    val i = Intent(Intent.ACTION_DIAL)
    i.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(i)
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    LoginDesign()
}
