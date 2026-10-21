
package com.example.dsy1105_009v_2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.dsy1105_009v_2026.ui.theme.DSY1105009V2026Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DSY1105009V2026Theme {
                Surface(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    AppNav()
                }
            }
        }
    }
}
