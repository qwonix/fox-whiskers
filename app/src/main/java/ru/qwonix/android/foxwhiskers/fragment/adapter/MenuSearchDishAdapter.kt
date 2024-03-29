package ru.qwonix.android.foxwhiskers.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.qwonix.android.foxwhiskers.databinding.ItemMenuSearchDishBinding
import ru.qwonix.android.foxwhiskers.entity.Dish
import ru.qwonix.android.foxwhiskers.util.Utils


class MenuSearchDishAdapter(
    private val dishCountChangeListener: DishCountChangeListener
) : RecyclerView.Adapter<MenuSearchDishAdapter.ViewHolder>() {
    var data = mutableListOf<Dish>()

    fun setFoundedDishes(foundDishes: List<Dish>) {
        val foundedDishesDiffUtil = FoundedDishesDiffUtil(data, foundDishes)
        val diffResult = DiffUtil.calculateDiff(foundedDishesDiffUtil)
        diffResult.dispatchUpdatesTo(this)

        data.clear()
        data.addAll(foundDishes)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMenuSearchDishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], dishCountChangeListener)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(
        private val binding: ItemMenuSearchDishBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dish: Dish, dishCountChangeListener: DishCountChangeListener
        ) {
            binding.dish = dish
            binding.priceFormat = Utils.DECIMAL_FORMAT
            binding.dishCountChangeListener = dishCountChangeListener
        }
    }

    private class FoundedDishesDiffUtil(
        private val oldList: List<Dish>,
        private val newList: List<Dish>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return when {
                oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                    false
                }

                oldList[oldItemPosition].title != newList[newItemPosition].title -> {
                    false
                }

                oldList[oldItemPosition].count != newList[newItemPosition].count -> {
                    false
                }

                else -> {
                    true
                }
            }
        }

    }
}