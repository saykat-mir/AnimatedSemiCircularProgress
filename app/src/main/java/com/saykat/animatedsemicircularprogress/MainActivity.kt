package com.saykat.animatedsemicircularprogress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.saykat.animatedsemicircularprogress.ui.theme.AnimatedSemiCircularProgressTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimatedSemiCircularProgressTheme {
                AnimatedSemiCircleProgress()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimatedSemiCircularProgressTheme {
        AnimatedSemiCircleProgress()
    }
}