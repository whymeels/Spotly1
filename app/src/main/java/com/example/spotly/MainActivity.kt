package com.example.spotly

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.spotly.data.SpotlyDatabase
import com.example.spotly.data.dao.PlaceWithCategory
import com.example.spotly.data.entity.CityEntity
import kotlinx.coroutines.CancellationException

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF5B3FC4),
                    background = Color(0xFFFAF9F6),
                    surface = Color.White
                )
            ) {
                SpotlyCatalog()
            }
        }
    }
}

@Composable
private fun SpotlyCatalog() {
    val context = LocalContext.current.applicationContext

    var cities by remember {
        mutableStateOf<List<CityEntity>>(emptyList())
    }

    var places by remember {
        mutableStateOf<List<PlaceWithCategory>>(emptyList())
    }

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }

    var selectedCityId by rememberSaveable {
        mutableIntStateOf(1)
    }

    var search by rememberSaveable {
        mutableStateOf("")
    }

    var selectedPlaceId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(reloadKey) {
        isLoading = true
        error = null

        try {
            val dao = SpotlyDatabase
                .getInstance(context)
                .placeDao()

            cities = dao.getCities()
            places = dao.getPlaces()

            if (cities.none { it.cityId == selectedCityId }) {
                selectedCityId = cities.firstOrNull()?.cityId ?: 1
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("SpotlyDatabase", "Ошибка чтения базы", e)
            error = e.message ?: e.javaClass.simpleName
        } finally {
            isLoading = false
        }
    }

    val selectedPlace = places.firstOrNull {
        it.place.placeId == selectedPlaceId
    }

    BackHandler(enabled = selectedPlaceId != null) {
        selectedPlaceId = null
    }

    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Spotly",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        CircularProgressIndicator()
                        Text("Загружаем места…")
                    }
                }

                error != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Не удалось открыть базу",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        item {
                            Text(
                                text = error.orEmpty(),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        item {
                            Button(onClick = { reloadKey++ }) {
                                Text("Повторить")
                            }
                        }
                    }
                }

                selectedPlace != null -> {
                    PlaceDetails(
                        item = selectedPlace,
                        onBack = { selectedPlaceId = null }
                    )
                }

                else -> {
                    val query = search.trim()

                    val filteredPlaces = places.filter { item ->
                        item.place.cityId == selectedCityId &&
                                (
                                        query.isBlank() ||
                                                item.place.name.contains(
                                                    query,
                                                    ignoreCase = true
                                                ) ||
                                                item.place.address.contains(
                                                    query,
                                                    ignoreCase = true
                                                ) ||
                                                item.categoryName.contains(
                                                    query,
                                                    ignoreCase = true
                                                )
                                        )
                    }.sortedBy { it.place.name.lowercase() }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                text = "Spotly",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "Открой город по-своему",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                cities.forEach { city ->
                                    FilterChip(
                                        selected = selectedCityId == city.cityId,
                                        onClick = {
                                            selectedCityId = city.cityId
                                            search = ""
                                        },
                                        label = { Text(city.name) }
                                    )
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = search,
                                onValueChange = { search = it },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                label = { Text("Поиск мест") },
                                placeholder = {
                                    Text("Название, адрес или категория")
                                }
                            )
                        }

                        item {
                            Text(
                                text = "Найдено мест: ${filteredPlaces.size}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        if (filteredPlaces.isEmpty()) {
                            item {
                                Text(
                                    "Ничего не найдено. Попробуй другой запрос."
                                )
                            }
                        }

                        items(
                            items = filteredPlaces,
                            key = { it.place.placeId }
                        ) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPlaceId = item.place.placeId
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = item.categoryName,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Text(
                                        text = item.place.name,
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                    Text(
                                        text = item.place.address,
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    if (item.place.isFreeEntry == true) {
                                        Text(
                                            text = "Бесплатный вход / внешний осмотр",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }

                                    Text(
                                        text = "Подробнее →",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaceDetails(
    item: PlaceWithCategory,
    onBack: () -> Unit
) {
    val place = item.place

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            TextButton(onClick = onBack) {
                Text("← Назад к местам")
            }
        }

        item {
            Text(
                text = item.categoryName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = place.name,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            DetailBlock("О месте", place.description)
        }

        item {
            DetailBlock("Адрес", place.address)
        }

        item {
            DetailBlock("Когда посетить", place.bestVisitTime)
        }

        item {
            DetailBlock("Для фотографии", place.bestPhotoTime)
        }

        item {
            DetailBlock("Советы", place.photoTips)
        }

        item {
            DetailBlock("Стоимость", place.priceNote)
        }

        place.visitDurationMinutes?.let { minutes ->
            item {
                DetailBlock(
                    "Продолжительность",
                    "Ориентировочно $minutes мин."
                )
            }
        }

        place.averageCheckRub?.let { amount ->
            item {
                DetailBlock(
                    "Средний чек",
                    "$amount ₽"
                )
            }
        }

        item {
            Text(
                text = "Условия посещения могут меняться. " +
                        "Перед поездкой уточни их у организатора.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun DetailBlock(
    title: String,
    value: String?
) {
    if (!value.isNullOrBlank()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}