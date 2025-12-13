package com.jason.alp_vp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jason.alp_vp.ui.theme.ALPVPTheme
import com.jason.alp_vp.ui.view.HomePage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ALPVPTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // TODO: Replace with proper navigation when login/register is ready
                    HomePage(
                        onBountyClick = { bountyId ->
                            // TODO: Navigate to bounty detail
                            println("Clicked bounty: $bountyId")
                        }
                    )
                }
            }
        }
    }
}