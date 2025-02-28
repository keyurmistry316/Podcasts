package com.audiobooks.podcasts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.audiobooks.podcasts.model.Podcast
import com.audiobooks.podcasts.ui.theme.PodcastsTheme

@Composable
fun PodcastListScreen(onShowDetails: (podcast: Podcast) -> Unit) {

    val viewModel: PodcastListViewModel = viewModel()
    val podcasts by viewModel.podcasts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }

            podcasts.isEmpty() -> {
                Text(
                    text = "No podcasts available",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                PodcastListUI(
                    onShowDetails = onShowDetails,
                    podcasts = podcasts
                )
            }
        }
    }


}

@Composable
private fun PodcastListUI(
    onShowDetails: (podcast: Podcast) -> Unit,
    podcasts: List<Podcast> = emptyList()
) {
    Column {
        Text(
            "Podcasts",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(20.dp, 40.dp, 0.dp, 0.dp)
        )

        PodcastList(podcasts = podcasts, onPodcastClick = { podcast ->
            onShowDetails(podcast)
        })
    }
}

@Composable
fun PodcastList(
    podcasts: List<Podcast>,
    onPodcastClick: (Podcast) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(podcasts) { podcast ->
            PodcastItem(podcast = podcast, onPodcastClick = onPodcastClick)

            if(podcasts.last() != podcast){
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 5.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun PodcastItem(
    podcast: Podcast,
    onPodcastClick: (Podcast) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPodcastClick(podcast) }
            .padding(5.dp)
    ) {
        // Podcast thumbnail
        AsyncImage(
            model = podcast.image,
            contentDescription = "Podcast thumbnail",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Podcast info
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 5.dp, 0.dp, 0.dp),
        ) {
            Text(
                text = podcast.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = podcast.publisher,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PodcastListUIPreview() {
    PodcastsTheme {
        PodcastListUI(
            onShowDetails = {}
        )
    }
}

