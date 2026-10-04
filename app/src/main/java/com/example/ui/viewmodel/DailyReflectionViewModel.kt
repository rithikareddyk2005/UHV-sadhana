package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DailyUhvReflection
import com.example.data.model.SentimentCategory
import com.example.data.repository.UhvRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DailyReflectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UhvRepository(AppDatabase.getDatabase(application))

    // Filter by sentiment category (null = all)
    private val _selectedSentimentFilter = MutableStateFlow<SentimentCategory?>(null)
    val selectedSentimentFilter: StateFlow<SentimentCategory?> = _selectedSentimentFilter.asStateFlow()

    // Active user id
    private val _currentUserId = MutableStateFlow("demo_student_1")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    // Form inputs
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _reflectionText = MutableStateFlow("")
    val reflectionText: StateFlow<String> = _reflectionText.asStateFlow()

    private val _selectedTheme = MutableStateFlow("Harmony in Self")
    val selectedTheme: StateFlow<String> = _selectedTheme.asStateFlow()

    private val _selectedSentiment = MutableStateFlow(SentimentCategory.HARMONIOUS)
    val selectedSentiment: StateFlow<SentimentCategory> = _selectedSentiment.asStateFlow()

    private val _moodRating = MutableStateFlow(5)
    val moodRating: StateFlow<Int> = _moodRating.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    // All reflections from Room database
    val allReflections: StateFlow<List<DailyUhvReflection>> = _currentUserId
        .flatMapLatest { userId ->
            repository.getDailyReflectionsForUser(userId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered reflections by sentiment
    val filteredReflections: StateFlow<List<DailyUhvReflection>> = combine(
        allReflections,
        _selectedSentimentFilter
    ) { list, filter ->
        if (filter == null) list else list.filter { it.sentiment == filter }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Sentiment breakdown stats
    val sentimentCounts: StateFlow<Map<SentimentCategory, Int>> = allReflections
        .map { list ->
            SentimentCategory.entries.associateWith { cat ->
                list.count { it.sentiment == cat }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()
        }
    }

    fun setUserId(userId: String) {
        _currentUserId.value = userId
    }

    fun setTitle(value: String) {
        _title.value = value
    }

    fun setReflectionText(value: String) {
        _reflectionText.value = value
    }

    fun setSelectedTheme(theme: String) {
        _selectedTheme.value = theme
    }

    fun setSelectedSentiment(sentiment: SentimentCategory) {
        _selectedSentiment.value = sentiment
    }

    fun setMoodRating(rating: Int) {
        _moodRating.value = rating.coerceIn(1, 5)
    }

    fun setSentimentFilter(filter: SentimentCategory?) {
        _selectedSentimentFilter.value = filter
    }

    fun saveReflection(userId: String, userName: String, onSuccess: () -> Unit) {
        if (_title.value.isBlank() || _reflectionText.value.isBlank()) return

        _isSaving.value = true
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH)
            val dateFormatted = dateFormat.format(Date(now))

            val reflection = DailyUhvReflection(
                id = UUID.randomUUID().toString(),
                userId = userId,
                userName = userName,
                title = _title.value.trim(),
                reflectionText = _reflectionText.value.trim(),
                uhvTheme = _selectedTheme.value,
                sentiment = _selectedSentiment.value,
                moodRating = _moodRating.value,
                timestamp = now,
                dateFormatted = dateFormatted
            )

            repository.saveDailyReflection(reflection)
            _isSaving.value = false
            resetForm()
            onSuccess()
        }
    }

    fun deleteReflection(id: String) {
        viewModelScope.launch {
            repository.deleteDailyReflection(id)
        }
    }

    fun resetForm() {
        _title.value = ""
        _reflectionText.value = ""
        _selectedTheme.value = "Harmony in Self"
        _selectedSentiment.value = SentimentCategory.HARMONIOUS
        _moodRating.value = 5
    }
}
