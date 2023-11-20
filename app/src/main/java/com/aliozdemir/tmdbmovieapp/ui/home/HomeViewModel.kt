package com.aliozdemir.tmdbmovieapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliozdemir.tmdbmovieapp.model.MovieItem
import com.aliozdemir.tmdbmovieapp.network.ApiClient
import com.aliozdemir.tmdbmovieapp.util.Constants
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val movieList: MutableLiveData<List<MovieItem?>?> = MutableLiveData()
    val isLoading = MutableLiveData(false)
    val errorMessage: MutableLiveData<String?> = MutableLiveData()

    init {
        getMovieList()
    }

    fun getMovieList() {
        isLoading.value = true

        viewModelScope.launch {
            try {
                val response = ApiClient.getClient().getMovieList(token = Constants.BEARER_TOKEN)

                if (response.isSuccessful) {
                    movieList.postValue(response.body()?.movieItems)
                } else {
                    if (response.message().isNullOrEmpty()) {
                        errorMessage.value = "An unknown error occurred"
                    } else {
                        errorMessage.value = response.message()
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }

    }

}