package ru.qwonix.android.foxwhiskers.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.qwonix.android.foxwhiskers.databinding.ItemMenuDishTypeChipBinding
import ru.qwonix.android.foxwhiskers.entity.DishType


class MenuDishTypeChipAdapter : RecyclerView.Adapter<MenuDishTypeChipAdapter.ViewHolder>() {

    var dishTypes = listOf<DishType>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private lateinit var binding: ItemMenuDishTypeChipBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemMenuDishTypeChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dishTypes[position])
    }

    override fun getItemCount(): Int = dishTypes.size

    inner class ViewHolder(
        private val binding: ItemMenuDishTypeChipBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dishType: DishType) {
            binding.dishType = dishType
            binding.isChecked = true
        }
    }
}

