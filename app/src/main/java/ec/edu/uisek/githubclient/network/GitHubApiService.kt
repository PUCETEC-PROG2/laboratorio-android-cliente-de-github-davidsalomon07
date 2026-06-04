package ec.edu.uisek.githubclient.network

import ec.edu.uisek.githubclient.model.GitHubRepo
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz que define los endpoints de la API.
 * Retrofit utiliza anotaciones para convertir estas funciones en peticiones HTTP reales.
 */
interface GitHubApiService {

    /**
     * @GET: Indica que es una petición de lectura.
     * Repositorios de un usuario (para el ejercicio usaremos una ruta mock o pública).
     */
    @GET("users/{user}/repos")
    suspend fun getRepositories(
        @Path("user") user: String
    ): Response<List<GitHubRepo>>

    /**
     * @POST: Indica creación de un nuevo recurso.
     * Nota: En una API real requiere un Token de autenticación.
     */
    @POST("user/repos")
    suspend fun createRepository(
        @Body repo: GitHubRepo
    ): Response<GitHubRepo>

    /**
     * @PATCH o @PUT: Indica actualización.
     */
    @PATCH("repos/{owner}/{repo}")
    suspend fun updateRepository(
        @Path("owner") owner: String,
        @Path("repo") repoName: String,
        @Body repo: GitHubRepo
    ): Response<GitHubRepo>

    /**
     * @DELETE: Eliminar un recurso.
     */
    @DELETE("repos/{owner}/{repo}")
    suspend fun deleteRepository(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): Response<Unit>
}
