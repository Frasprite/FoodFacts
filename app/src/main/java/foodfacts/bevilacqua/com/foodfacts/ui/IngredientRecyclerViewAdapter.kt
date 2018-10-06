package foodfacts.bevilacqua.com.foodfacts.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import foodfacts.bevilacqua.com.foodfacts.R
import foodfacts.bevilacqua.com.foodfacts.model.Ingredient
import kotlinx.android.synthetic.main.ingredient_view_item.view.*

class IngredientRecyclerViewAdapter :
        RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder>() {

    private var values: List<Ingredient> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredient_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = values.size

    override fun onBindViewHolder(holder: IngredientRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = values[position]
        holder.ingredientNameView.text = item.text
    }

    fun submitList(list: List<Ingredient>) {
        values = list
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientNameView: TextView = view.ingredientTextView
    }
}