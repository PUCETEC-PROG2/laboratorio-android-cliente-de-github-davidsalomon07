package ec.edu.uisek.githubclient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.model.GitHubRepo
import ec.edu.uisek.githubclient.network.RetrofitClient
import kotlinx.coroutines.launch

/**
 * El ViewModel separa la lógica de negocio de la UI.
 * Sobrevive a cambios de configuración (como girar la pantalla).
 */
class RepoViewModel : ViewModel() {

    private val _repos = MutableLiveData<List<GitHubRepo>>()
    val repos: LiveData<List<GitHubRepo>> get() = _repos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    /**
     * Función para obtener repositorios.
     * Coroutines (viewModelScope.launch): Permite realizar tareas en segundo plano
     * sin bloquear el hilo principal de la interfaz (UI Thread).
     */
    fun fetchRepositories(user: String = "octocat") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.apiService.getRepositories(user)
                if (response.isSuccessful) {
                    _repos.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error al obtener datos: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de red: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Simulación Pedagógica de Create: En una API real enviaríamos un POST.
     */
    fun addRepoLocal(repo: GitHubRepo) {
        val currentList = _repos.value?.toMutableList() ?: mutableListOf()
        currentList.add(0, repo) // Lo agregamos al inicio para que se vea la animación
        _repos.value = currentList
    }

    /**
     * Simulación Pedagógica de Update.
     */
    fun updateRepoLocal(updatedRepo: GitHubRepo) {
        val currentList = _repos.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == updatedRepo.id }
        if (index != -1) {
            currentList[index] = updatedRepo
            _repos.value = currentList
        }
    }

    /**
     * Simulación Pedagógica de Delete.
     */
    fun deleteRepoLocal(repo: GitHubRepo) {
        val currentList = _repos.value?.toMutableList() ?: return
        currentList.remove(repo)
        _repos.value = currentList
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
