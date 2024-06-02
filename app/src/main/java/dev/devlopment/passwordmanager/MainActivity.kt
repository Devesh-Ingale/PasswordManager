package dev.devlopment.passwordmanager

import PasswordViewModel
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.devlopment.passwordmanager.MVVM.PasswordViewModelFactory
import dev.devlopment.passwordmanager.Screens.PasswordManagerApp
import dev.devlopment.passwordmanager.Security.BiometricPromptManager
import dev.devlopment.passwordmanager.ui.theme.PasswordManagerTheme

class MainActivity : AppCompatActivity() {
    private val viewModel: PasswordViewModel by viewModels {
        PasswordViewModelFactory(application)
    }

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                val biometricResult by promptManager.promptResult.collectAsState(initial = null)
                val enrollLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = {

                    }
                )
                LaunchedEffect(biometricResult) {
                    if (biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet){
                        if (Build.VERSION.SDK_INT >= 30){
                            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                putExtra(
                                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                                )
                            }
                            enrollLauncher.launch(enrollIntent)
                        }
                    } else if (biometricResult == null) {
                        promptManager.showBiometricPrompt(
                            title = "Password",
                            description = "Authenticate to continue"
                        )
                    }
                }
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        biometricResult?.let { result ->
                            when (result) {
                                is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                                    PasswordManagerApp(viewModel)
                                }
                                else -> {
                                    val message = when (result) {
                                        is BiometricPromptManager.BiometricResult.AuthenticationError -> result.error
                                        BiometricPromptManager.BiometricResult.AuthenticationFailed -> "Authentication failed"
                                        BiometricPromptManager.BiometricResult.AuthenticationNotSet -> "Authentication not set"
                                        BiometricPromptManager.BiometricResult.FeatureUnavailable -> "Feature unavailable"
                                        BiometricPromptManager.BiometricResult.HardwareUnavailable -> "Hardware unavailable"
                                        else -> "Unknown error"
                                    }
                                    Text(text = message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
