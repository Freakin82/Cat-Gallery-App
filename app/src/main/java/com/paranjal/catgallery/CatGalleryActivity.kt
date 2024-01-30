package com.paranjal.catgallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class CatGalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatGallery()
        }
    }
}

@Composable
fun CatGallery() {
    var catImages by remember { mutableStateOf<List<CatImage>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            try {
                val result = fetchCatImages()
                catImages = result
            } catch (e: Exception) {
                isError = true
            } finally {
                isLoading = false
            }
        }
    }

    MaterialTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (isLoading) {
                // Show loading indicator
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator()
                }
            } else if (isError) {
                // Show error message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text("Error loading cat images.")
                }
            } else {
                // Display the Cat Gallery
                LazyColumn {
                    items(catImages) { catImage ->
                        CatImageItem(catImage)
                    }
                }
            }
        }
    }
}

@Composable
fun CatImageItem(catImage: CatImage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display cat image using Coil library
        Image(
            painter = rememberAsyncImagePainter(catImage.url),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display additional information (dummy text for illustration)
        Text("Cat ID: ${catImage.id}")
        Text("Dimensions: ${catImage.width} x ${catImage.height}")
    }
}

suspend fun fetchCatImages(): List<CatImage> {
    val url = "https://api.thecatapi.com/v1/images/search?limit=10"
    return withContext(Dispatchers.IO) {
        val response = java.net.URL(url).readText()
        // Parse the response JSON and create a list of CatImage objects
        // You can use a JSON parsing library like Gson or kotlinx.serialization
        // For simplicity, I'll provide a basic example assuming a well-formed JSON response
        val catImages = mutableListOf<CatImage>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getString("id")
            val imageUrl = jsonObject.getString("url")
            val width = jsonObject.getInt("width")
            val height = jsonObject.getInt("height")
            catImages.add(CatImage(id, imageUrl, width, height))
        }
        catImages
    }
}

@Composable
@Preview(showBackground = true)
fun CatGalleryPreview() {
    CatGallery()
}