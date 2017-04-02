package com.amicablesoft.ghusersbrowser.android.ui.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amicablesoft.ghusersbrowser.android.R
import com.amicablesoft.ghusersbrowser.android.model.UserShort
import com.amicablesoft.ghusersbrowser.android.ui.utils.ImageLoader
import com.amicablesoft.ghusersbrowser.android.ui.utils.bindView

class UsersAdapter(ctx: Context): RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    var onItemClickListener: ((position:Int) -> Unit)? = null
    private val layoutInflater = LayoutInflater.from(ctx)!!
    private val imageLoader = ImageLoader(ctx.applicationContext)
    private val users = ArrayList<UserShort>()

    fun setUsers(users: List<UserShort>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = users.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_user_search, parent, false)
        val holder = UserViewHolder(itemView, onItemClickListener)
        return holder
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        imageLoader.loadCircleImageAsync(user.avatarUrl, holder.avatarImage)
        holder.loginLabel.text = user.login
    }

    class UserViewHolder(itemView: View, val onItemClickListener: ((position:Int) -> Unit)?): RecyclerView.ViewHolder(itemView) {
        val avatarImage: ImageView by bindView(R.id.item_user_search__image__avatar)
        val loginLabel: TextView by bindView(R.id.item_user_search__label__login)

        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }
}