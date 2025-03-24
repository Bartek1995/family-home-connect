package com.familyhomeconnect.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.familyhomeconnect.model.House
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {
    // W prawdziwej aplikacji pobierzesz dane z Firestore lub innego źródła.
    private val _houseInfo = MutableLiveData<House>()
    val houseInfo: LiveData<House> get() = _houseInfo

    init {
        // Przykładowe dane – w rzeczywistości wywołaj zapytanie do bazy.
        _houseInfo.value = House(
            id = "1",
            address = "123 Main Street",
            ownerName = "Jan Kowalski"
        )
    }

}
