package ec.edu.uisek.githubclient

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ec.edu.uisek.githubclient.databinding.ActivityMainBinding
import ec.edu.uisek.githubclient.model.GitHubRepo
import ec.edu.uisek.githubclient.ui.adapter.RepoAdapter
import ec.edu.uisek.githubclient.ui.fragment.RepoFormDialogFragment
import ec.edu.uisek.githubclient.viewmodel.RepoViewModel

/**
 * Actividad Principal: Punto de entrada y orquestador de la UI.
 * Implementa una arquitectura MVVM básica.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    // Lazy initialization del ViewModel usando el delegado 'viewModels()'
    private val viewModel: RepoViewModel by viewModels()
    
    private lateinit var repoAdapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        // Iniciamos la carga de datos (Read del CRUD)
        viewModel.fetchRepositories()
    }

    private fun setupRecyclerView() {
        repoAdapter = RepoAdapter(
            onEditClick = { repo -> showFormDialog(repo) }, // Update del CRUD
            onDeleteClick = { repo -> showDeleteConfirmation(repo) } // Confirmación + Delete
        )
        
        binding.rvRepos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = repoAdapter
        }
    }

    /**
     * Diálogo de confirmación pedagógico para acciones destructivas.
     * Es una buena práctica de UX pedir confirmación antes de borrar datos.
     */
    private fun showDeleteConfirmation(repo: GitHubRepo) {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle("¿Eliminar Repositorio?")
            .setMessage("Esta acción no se puede deshacer. ¿Estás seguro de que quieres borrar '${repo.name}'?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteRepoLocal(repo)
                Toast.makeText(this, "Repositorio eliminado", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun setupObservers() {
        // Observamos los cambios en la lista de repositorios
        viewModel.repos.observe(this) { list ->
            repoAdapter.submitList(list)
        }

        // Observamos el estado de carga para mostrar el ProgressBar
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observamos errores
        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddRepo.setOnClickListener {
            showFormDialog() // Create del CRUD
        }
    }

    /**
     * Muestra el diálogo pedagógico para crear o editar.
     */
    private fun showFormDialog(repo: GitHubRepo? = null) {
        val dialog = RepoFormDialogFragment.newInstance(repo)
        dialog.onRepoSaved = { savedRepo ->
            if (repo == null) {
                viewModel.addRepoLocal(savedRepo) // Caso Create
                Toast.makeText(this, "Repositorio creado", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateRepoLocal(savedRepo) // Caso Update
                Toast.makeText(this, "Repositorio actualizado", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show(supportFragmentManager, "RepoForm")
    }
}
