package dev.devlopment.passwordmanager

import PasswordViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dev.devlopment.passwordmanager.MVVM.PasswordViewModelFactory
import dev.devlopment.passwordmanager.Screens.PasswordManagerApp
import dev.devlopment.passwordmanager.ui.theme.PasswordManagerTheme


class MainActivity : ComponentActivity() {
    private val viewModel: PasswordViewModel by viewModels {
        PasswordViewModelFactory(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                PasswordManagerApp(viewModel)
            }
        }
        //showBiometricPrompt()
    }

//    private fun showBiometricPrompt() {
//        val biometricManager = BiometricManager.from(this)
//        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
//            BiometricManager.BIOMETRIC_SUCCESS -> {
//                // App can authenticate using biometrics
//                val executor = ContextCompat.getMainExecutor(this)
//                val biometricPrompt = BiometricPrompt(this@MainActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
//                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                        super.onAuthenticationError(errorCode, errString)
//                        // Handle the error case
//                    }
//
//                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                        super.onAuthenticationSucceeded(result)
//                        // Authentication succeeded
//                        // Proceed with your app logic here
//                    }
//
//                    override fun onAuthenticationFailed() {
//                        super.onAuthenticationFailed()
//                        // Handle the failure case
//                    }
//                })
//
//                val promptInfo = BiometricPrompt.PromptInfo.Builder()
//                    .setTitle("Biometric login for Password Manager")
//                    .setSubtitle("Log in using your biometric credential")
//                    .setNegativeButtonText("Use account password")
//                    .build()
//
//                biometricPrompt.authenticate(promptInfo)
//            }
//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
//            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
//            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                // No biometric features available, or none enrolled, proceed to the app
//                proceedToApp()
//            }
//        }
//    }
//
//    private fun proceedToApp() {
//        // Proceed with your app logic here
//    }

}
