package kz.kuz.recyclerviewitemtouchhelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainFragment : Fragment() {
    private lateinit var mMainRecyclerView: RecyclerView
    private lateinit var mAdapter: MainAdapter

    //    int positionToUpdate;
    private inner class Item {
        var mTitle: String? = null
        var mPart1: String? = null
        var mPart2: String? = null
    }

    private val items: MutableList<Item> = ArrayList()

    // методы фрагмента должны быть открытыми
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.toolbar_title)
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mMainRecyclerView = view.findViewById(R.id.main_recycler_view)
        mMainRecyclerView.layoutManager = LinearLayoutManager(activity)
        for (i in 0..19) {
            val item: Item = Item()
            item.mTitle = "Title #" + (i + 1)
            item.mPart1 = "Part1 #" + (i + 1)
            item.mPart2 = "Part2 #" + (i + 1)
            items.add(item)
        }
        mAdapter = MainAdapter(items)
        mMainRecyclerView.adapter = mAdapter
        val callback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = items[viewHolder.adapterPosition]
                // получаем смахнутый экземпляр
                items.remove(item) // удаляем экземпляр из класса
//                mAdapter.notifyDataSetChanged(); // обновляем адаптер
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                // обновляем конкретную позицию
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(mMainRecyclerView)
        return view
    }


    private inner class MainAdapter(private val mItems: List<Item>) :
            RecyclerView.Adapter<MainAdapter.MainHolder>() {

        private inner class MainHolder(inflater: LayoutInflater, parent: ViewGroup?) :
                RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent,
                        false)), View.OnClickListener {
            val mTitleTextView: TextView = itemView.findViewById(R.id.item_title)
            val mPart1TextView: TextView = itemView.findViewById(R.id.item_part1)
            val mPart2TextView: TextView = itemView.findViewById(R.id.item_part2)

            override fun onClick(view: View) {
            // слушателя лучше реализовать здесь, а не в onBindViewHolder, там он работает
            // неточно с notifyItemMoved
//            Toast.makeText(activity, mItems[adapterPosition].mTitle + " clicked!",
//                    Toast.LENGTH_SHORT).show();
//            mMainRecyclerView.adapter?.notifyItemMoved(adapterPosition, 0);
//            mMainRecyclerView.scrollToPosition(0);
            }

            init {
                itemView.setOnClickListener(this) // реализуется слушатель на нажатие
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return MainHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
//            positionToUpdate = position;
            val item = mItems[position]
            holder.mTitleTextView.text = item.mTitle
            holder.mPart1TextView.text = item.mPart1
            holder.mPart2TextView.text = item.mPart2
        }

        override fun getItemCount(): Int {
            return mItems.size
        }
    }

    override fun onResume() {
        super.onResume()
        mAdapter.notifyDataSetChanged() // обновление в случае изменений в списке
        //        mAdapter.notifyItemChanged(positionToUpdate); // для обновления одной позиции
    }
}