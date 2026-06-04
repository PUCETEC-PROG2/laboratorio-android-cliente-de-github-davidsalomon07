package ec.edu.uisek.githubclient.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import ec.edu.uisek.githubclient.databinding.DialogRepoFormBinding
import ec.edu.uisek.githubclient.model.GitHubRepo
import ec.edu.uisek.githubclient.model.Owner

/**
 * DialogFragment para el formulario CRUD. 
 * Los diálogos son una excelente forma de mantener el contexto del usuario.
 */
class RepoFormDialogFragment : DialogFragment() {

    private var _binding: DialogRepoFormBinding? = null
    private val binding get() = _binding!!

    // Callback para devolver el repositorio creado o editado al fragment/actividad padre
    var onRepoSaved: ((GitHubRepo) -> Unit)? = null
    
    // Variable para saber si estamos editando
    private var repoToEdit: GitHubRepo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRepoFormBinding.inflate(inflater, container, false)
        
        // Hacer el fondo transparente para que se vean los bordes redondeados del CardView
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Si recibimos un ID, significa que es una edición
        arguments?.let {
            // En un caso real buscaríamos por ID, aquí pasaremos el objeto simplificado
        }

        setupUI()
    }

    private fun setupUI() {
        repoToEdit?.let {
            binding.tvTitle.text = "Editar Repositorio"
            binding.etName.setText(it.name)
            binding.etDescription.setText(it.description)
            binding.btnSave.text = "Actualizar"
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val description = binding.etDescription.text.toString()

            if (name.isNotEmpty()) {
                val newRepo = repoToEdit?.copy(
                    name = name,
                    description = description
                ) ?: GitHubRepo(
                    id = System.currentTimeMillis(), // ID temporal para el mock
                    name = name,
                    description = description,
                    owner = Owner("User", "https://api.dicebear.com/7.x/avataaars/svg?seed=$name")
                )
                
                onRepoSaved?.invoke(newRepo)
                dismiss()
            } else {
                binding.etName.error = "El nombre es obligatorio"
            }
        }

        binding.btnCancel.setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        // Hacer el diálogo grande (90% del ancho de la pantalla)
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * Función estática para crear la instancia del diálogo
     */
    companion object {
        fun newInstance(repo: GitHubRepo? = null): RepoFormDialogFragment {
            val fragment = RepoFormDialogFragment()
            fragment.repoToEdit = repo
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
