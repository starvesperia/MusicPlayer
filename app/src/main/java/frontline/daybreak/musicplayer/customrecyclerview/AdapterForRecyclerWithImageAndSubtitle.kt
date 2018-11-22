package frontline.daybreak.musicplayer.customrecyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import frontline.daybreak.musicplayer.R

class AdapterForRecyclerWithImageAndSubtitle
    ( private val context : Context,
      private val items: ArrayList<ItemForRecyclerWithImageAndSubtitle>,
      private val clickListener : CustomItemClickListener)
    : RecyclerView.Adapter<AdapterForRecyclerWithImageAndSubtitle.ViewHolder>()
{

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textViewMain: TextView = v.findViewById(R.id.text_main)
        val textViewSub: TextView = v.findViewById(R.id.text_sub)
        val imageViewLeft: ImageView = v.findViewById(R.id.list_image_view)

        init
        {
            //v.setOnClickListener { Log.d(TAG, "Element $adapterPosition clicked.") }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_view_item_with_image_and_subtitle, viewGroup, false)
        val viewHolder = ViewHolder(v)
        v.setOnClickListener { view ->
                clickListener.onItemClick(view, viewHolder.adapterPosition)
        }

        return viewHolder
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.textViewMain.text = items[position].titleMain
        viewHolder.textViewSub.text = items[position].titleSub

        val resID = if (items[position].imageID != 0 ) items[position].imageID else R.drawable.img_cover_bg
        Glide.with(context)
            .load(resID)
            .apply(RequestOptions().override(viewHolder.imageViewLeft.width, viewHolder.imageViewLeft.height).fitCenter())
            .into(viewHolder.imageViewLeft)
    }

    override fun getItemCount() = items.size

    companion object {
        private val TAG = "Adapter"
    }
}
