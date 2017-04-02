package com.amicablesoft.ghusersbrowser.android.ui.repos

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amicablesoft.ghusersbrowser.android.R
import com.amicablesoft.ghusersbrowser.android.model.Repo
import com.amicablesoft.ghusersbrowser.android.ui.utils.bindView

class UserReposAdapter(ctx: Context): RecyclerView.Adapter<UserReposAdapter.RepoViewHolder>() {
    var onItemClickListener: ((position:Int) -> Unit)? = null
    private val layoutInflater = LayoutInflater.from(ctx)
    private val repos = ArrayList<Repo>()

    fun setRepos(repos: List<Repo>) {
        this.repos.clear()
        this.repos.addAll(repos)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = repos.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RepoViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_user_repo, parent, false)
        return RepoViewHolder(itemView, onItemClickListener)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = repos[position]
        holder.nameLabel.text = repo.name
        holder.descLabel.text = repo.desc
        holder.descLabel.visibility = if (repo.desc != null) View.VISIBLE else View.GONE
        holder.langLabel.text = repo.lang
        holder.seenLabel.text = repo.seen.toString()
        holder.starsLabel.text = repo.stars.toString()
        holder.forkLabel.text = repo.fork.toString()
    }


    class RepoViewHolder(itemView: View, val onItemClickListener:((position:Int) -> Unit)?): RecyclerView.ViewHolder(itemView) {
        val cardView: CardView by bindView(R.id.user_repo__card_view)
        val nameLabel: TextView by bindView(R.id.user_repo__label__name)
        val descLabel: TextView by bindView(R.id.user_repo__label__desc)
        val langLabel: TextView by bindView(R.id.user_repo__label__language)
        val seenLabel: TextView by bindView(R.id.user_repo__label__seen_count)
        val starsLabel: TextView by bindView(R.id.user_repo__label__stars_count)
        val forkLabel: TextView by bindView(R.id.user_repo__label__fork_count)

        init {
            cardView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }
}
