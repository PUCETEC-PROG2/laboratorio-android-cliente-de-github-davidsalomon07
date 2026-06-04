package ec.edu.uisek.githubclient.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ec.edu.uisek.githubclient.R
import ec.edu.uisek.githubclient.databinding.ItemRepoBinding
import ec.edu.uisek.githubclient.model.GitHubRepo

/**
 * Adaptador pedagógico que utiliza ListAdapter para manejar actualizaciones eficientes
 * de la lista mediante DiffUtil (compara elementos viejos vs nuevos).
 */
class RepoAdapter(
    private val onEditClick: (GitHubRepo) -> Unit,
    private val onDeleteClick: (GitHubRepo) -> Unit
) : ListAdapter<GitHubRepo, RepoAdapter.RepoViewHolder>(RepoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = getItem(position)
        holder.bind(repo)
        
        // --- ANIMACIÓN PEDAGÓGICA ---
        // Aplicamos una animación de entrada a cada tarjeta al ser vinculada.
        holder.itemView.animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.item_animation_fall_down
        )
    }

    inner class RepoViewHolder(private val binding: ItemRepoBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(repo: GitHubRepo) {
            binding.tvRepoName.text = repo.name
            binding.tvRepoDescription.text = repo.description ?: "Sin descripción"
            binding.tvStars.text = repo.stars.toString()

            // Glide: Biblioteca recomendada para cargar imágenes de internet de forma eficiente.
            repo.owner?.let {
                Glide.with(binding.ivOwnerAvatar.context)
                    .load(it.avatarUrl)
                    .circleCrop()
                    .placeholder(android.R.drawable.progress_horizontal)
                    .into(binding.ivOwnerAvatar)
            } ?: run {
                // Imagen por defecto si no hay owner
                binding.ivOwnerAvatar.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            binding.btnEdit.setOnClickListener { onEditClick(repo) }
            binding.btnDelete.setOnClickListener { onDeleteClick(repo) }
        }
    }

    /**
     * Clase necesaria para que ListAdapter sepa qué cambió exactamente en la lista
     * y no tenga que refrescar todo el RecyclerView (mejora rendimiento).
     */
    class RepoDiffCallback : DiffUtil.ItemCallback<GitHubRepo>() {
        override fun areItemsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo) = oldItem == newItem
    }
}
