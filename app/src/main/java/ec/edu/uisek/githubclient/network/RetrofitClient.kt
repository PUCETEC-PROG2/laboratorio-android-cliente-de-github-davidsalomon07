package ec.edu.uisek.githubclient.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Patrón Singleton (Object en Kotlin) para asegurar que solo exista una instancia
 * de Retrofit en toda la aplicación, optimizando el uso de recursos.
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.github.com/"

    /**
     * Propiedad 'lazy' (perezosa): El objeto se crea solo cuando se accede por primera vez.
     */
    val apiService: GitHubApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // GsonConverterFactory convierte automáticamente el JSON a objetos de Kotlin
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }
}
