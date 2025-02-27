package com.audiobooks.podcasts.ui

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.audiobooks.podcasts.model.Podcast
import com.audiobooks.podcasts.ui.theme.PodcastsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailsScreen(podcast: Podcast, onBackPress: () -> Boolean) {

    var isFavorite by rememberSaveable { mutableStateOf(podcast.isFavorite) }
    val typography = MaterialTheme.typography.bodyMedium
    val color = MaterialTheme.colorScheme.onSurfaceVariant


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Back",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPress() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = podcast.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = podcast.publisher,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Podcast image with rounded corners
            AsyncImage(
                model = podcast.image,
                contentDescription = "Podcast thumbnail",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Favorite button
            Button(
                onClick = {
                    isFavorite = isFavorite.not()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text(
                    text = if (isFavorite) "Favourited" else "Favourite",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Podcast description
            AndroidView(
                factory = { context ->
                    TextView(context).apply {
                        this.movementMethod = LinkMovementMethod.getInstance()
                    }
                },
                update = { textView ->
                    textView.text = HtmlCompat.fromHtml(
                        podcast.description,
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
                    textView.textSize = typography.fontSize.value
                    textView.setTextColor(color.toArgb())
                    textView.gravity = android.view.Gravity.CENTER
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

