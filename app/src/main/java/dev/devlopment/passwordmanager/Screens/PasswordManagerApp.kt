package dev.devlopment.passwordmanager.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.devlopment.passwordmanager.MVVM.PasswordViewModel
import dev.devlopment.passwordmanager.Room.Password
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.devlopment.passwordmanager.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagerApp(viewModel: PasswordViewModel = viewModel()) {
    val passwords by viewModel.allPasswords.observeAsState(emptyList())
    var selectedPassword by remember { mutableStateOf<Password?>(null) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var isClickBottomSheetVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { true })

    if (isBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                coroutineScope.launch { bottomSheetState.hide() }
                isBottomSheetVisible = false
            },
            content = {
                PasswordModalBottomSheet(
                    password = selectedPassword,
                    onDismiss = {
                        coroutineScope.launch { bottomSheetState.hide() }
                        isBottomSheetVisible = false
                    },
                    onSave = { accountType, username, password ->
                        if (selectedPassword == null) {
                            viewModel.insert(
                                Password(
                                    accountType = accountType,
                                    username = username,
                                    password =  password
                                )
                            )
                        } else {
                            viewModel.update(
                                selectedPassword!!.copy(
                                    accountType = accountType,
                                    username = username,
                                    password =  password
                                )
                            )
                        }
                        coroutineScope.launch { bottomSheetState.hide() }
                        isBottomSheetVisible = false
                    },
                    onDecrypt = { encryptedPassword -> viewModel.decryptPassword(encryptedPassword) }
                )
            }
        )
    }

    if (isClickBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                coroutineScope.launch { bottomSheetState.hide() }
                isClickBottomSheetVisible = false
            },
            content = {
                ClickPasswordModalBottomSheet(
                    password = selectedPassword,
                    onDismiss = {
                        coroutineScope.launch { bottomSheetState.hide() }
                        isClickBottomSheetVisible = false
                    },
                    onEdit = {
                        isClickBottomSheetVisible = false
                        isBottomSheetVisible = true
                    },
                    onDelete = { selectedPassword?.let { viewModel.delete(password = it) }
                        coroutineScope.launch { bottomSheetState.hide() }
                        isClickBottomSheetVisible = false
                    },
                    onDecrypt = { password ->
                        viewModel.decryptPassword(password)
                    }
                )
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("Password Manager", fontWeight = FontWeight.Bold) })
                Divider(color = Color.Gray, thickness = 0.2.dp)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedPassword = null
                isBottomSheetVisible = true
                coroutineScope.launch { bottomSheetState.show() }
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            PasswordList(
                passwords,
                onClick = {
                    selectedPassword = it
                    isClickBottomSheetVisible = true
                    coroutineScope.launch { bottomSheetState.show() }
                }
            )
        }
    }
}



@Composable
fun ClickPasswordModalBottomSheet(
    password: Password?,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDecrypt: (String) -> String
) {
    var accountType by remember { mutableStateOf(password?.accountType) }
    var username by remember { mutableStateOf(password?.username) }
    var encryptedPassword by remember { mutableStateOf(password?.password) }
    var isPasswordVisible = remember { mutableStateOf(false) }
    var decryptedPassword by remember { mutableStateOf("********") }

    if (isPasswordVisible.value && encryptedPassword != null) {
        decryptedPassword = onDecrypt(encryptedPassword!!)
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Text("Account Details", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = Color(0xff3F7DE3))
        Spacer(modifier = Modifier.padding(25.dp))
        Text("Account Type", fontWeight = FontWeight.Light, color = Color.DarkGray)
        accountType?.let { Text(it, fontWeight = FontWeight.Bold, color = Color.Black, style = MaterialTheme.typography.headlineMedium) }
        Spacer(modifier = Modifier.padding(20.dp))
        Text("Username/Email", fontWeight = FontWeight.Light, color = Color.DarkGray)
        username?.let { Text(it, fontWeight = FontWeight.Bold, color = Color.Black, style = MaterialTheme.typography.headlineMedium) }
        Spacer(modifier = Modifier.padding(20.dp))
        Text("Password", fontWeight = FontWeight.Light, color = Color.DarkGray)
        Row {
            Text(decryptedPassword, fontWeight = FontWeight.Bold, color = Color.Black, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.baseline_remove_red_eye_24),
                contentDescription = "see password",
                modifier = Modifier.clickable { isPasswordVisible.value = true }
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
                    onEdit()
                },
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f),
                colors = ButtonDefaults.filledTonalButtonColors(Color.Black),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Edit", fontWeight = FontWeight.Bold, color = Color.White)
            }
            TextButton(
                onClick = {
                    onDelete()
                },
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f),
                colors = ButtonDefaults.filledTonalButtonColors(Color.Red),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Delete", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordModalBottomSheet(
    password: Password?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit,
    onDecrypt: (String) -> String
) {
    var accountType by remember { mutableStateOf(password?.accountType ?: "") }
    var username by remember { mutableStateOf(password?.username ?: "") }
    var passwordValue by remember { mutableStateOf("") }
    var action by remember { mutableStateOf("Add New Account") }

    LaunchedEffect(password) {
        if (password != null) {
            action = "Update Account"
            passwordValue = onDecrypt(password.password)
        }
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            value = accountType,
            onValueChange = { accountType = it },
            label = { Text("Account name", color = Color.Gray) },
            shape = RoundedCornerShape(8.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            value = username,
            onValueChange = { username = it },
            label = { Text("Username/Email", color = Color.Gray) },
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            value = passwordValue,
            onValueChange = { passwordValue = it },
            label = { Text("Password", color = Color.Gray) },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                onSave(accountType, username, passwordValue)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            colors = ButtonDefaults.filledTonalButtonColors(Color.Black),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(action, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}


