package ec.edu.uisek.githubclient.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos pedagógico que representa un repositorio de GitHub.
 * Las clases 'data class' en Kotlin son ideales para almacenar datos, ya que el compilador
 * genera automáticamente métodos como equals(), hashCode() y toString().
 */
data class GitHubRepo(
    /**
     * @SerializedName es una anotación de Gson que vincula el nombre de la variable en Kotlin
     * con la clave exacta que viene en el JSON de la API.
     */
    @SerializedName("id")
    val id: Long = 0,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("html_url")
    val htmlUrl: String? = null,

    @SerializedName("owner")
    val owner: Owner? = null,

    @SerializedName("stargazers_count")
    val stars: Int = 0
)

/**
 * Clase interna para representar al dueño del repositorio (necesaria para el avatar).
 */
data class Owner(
    @SerializedName("login")
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String
)
