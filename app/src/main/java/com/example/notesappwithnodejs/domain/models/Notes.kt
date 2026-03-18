package com.example.notesappwithnodejs.domain.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappwithnodejs.R
import com.example.notesappwithnodejs.components.CustomFilterChip
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


data class Notes(
    val _id: String? = null,
    val notesTitle: String?,
    val noteDescription: String?,
    val notesPriority: String?,
    val date: String ?= null
)

@Composable
fun NotesCard(notes: Notes, modifier: Modifier = Modifier ) {

    val chipColors = remember(notes.notesPriority) {
        when (notes.notesPriority) {
            "High"   -> Color(0xFFE53935)
            "Medium" -> Color(0xFFFFA726)
            else     -> Color(0xFF43A047)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.dark_blue))
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.medium1_blue)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row {
                    CustomFilterChip(
                        label = notes.notesPriority ?: "Low", color = chipColors, selected = false
                    ){}
                }

                Text(
                    text = notes.notesTitle ?: "Title",
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = notes.noteDescription ?: "Description",
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = notes.date?.let { formateTimestamp(it) } ?: "No Date",
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End
                )


            }

        }
    }
}

fun formateTimestamp(timestamps: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(timestamps, formatter)
        val zoneId = ZoneId.of("Asia/Kolkata")
        val isDateTime = zonedDateTime.withZoneSameInstant(zoneId)
        val day = isDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = isDateTime.monthValue.toString().padStart(2, '0')
        val year = isDateTime.year.toString()
        "$day-$month-$year"
    } catch (e: Exception) {
        "Invalid date"
    }
}
