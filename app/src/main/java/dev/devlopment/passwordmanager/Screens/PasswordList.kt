package dev.devlopment.passwordmanager.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.devlopment.passwordmanager.Room.Password

@Composable
fun PasswordList(passwords: List<Password>, onClick: (Password) -> Unit) {
    LazyColumn {
        items(passwords) { password ->
            PasswordItem(password, onClick)
        }
    }
}

@Composable
fun PasswordItem(password: Password, onClick: (Password) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick(password) },
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.elevatedCardElevation(55.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.padding(start = 15.dp))
            Text(password.accountType, style = MaterialTheme.typography.headlineLarge, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(top = 35.dp, start = 16.dp, bottom = 35.dp))
            Text("********", style = MaterialTheme.typography.headlineMedium,color = Color.Gray)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onClick(password) }){
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }


    }
}
