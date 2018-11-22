package frontline.daybreak.musicplayer.fileexplorerview

import android.content.Context
import android.os.Environment
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import java.io.File
import frontline.daybreak.musicplayer.customrecyclerview.AdapterForRecyclerWithImageAndSubtitle
import frontline.daybreak.musicplayer.customrecyclerview.CustomItemClickListener
import frontline.daybreak.musicplayer.customrecyclerview.ItemForRecyclerWithImageAndSubtitle
import android.view.View
import frontline.daybreak.musicplayer.R


class FileExplorerView : ConstraintLayout
{
    private val txtView by lazy { findViewById<TextView>(R.id.text_view) }
    private val rcView by lazy { findViewById<RecyclerView>(R.id.custom_recycler_view) }
    //private val rvAdapter by lazy { AdapterForRecyclerWithImageAndSubtitle(context, items) }
    lateinit var rvAdapter : AdapterForRecyclerWithImageAndSubtitle
    private val items by lazy { ArrayList<ItemForRecyclerWithImageAndSubtitle>() }

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val view = layoutInflater.inflate(R.layout.explorer_layout, this, false)

    private val rootPath : String = Environment.getExternalStorageDirectory().absolutePath
    private var nextPath : String = ""
    private var prevPath : String = ""
    private var currentPath : String = ""

    constructor(context: Context) : super(context)
    {
        init()
    }

    constructor(context: Context, attrs : AttributeSet) : super(context, attrs)
    {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr : Int)
            : super(context, attrs, defStyleAttr)
    {
        init()
    }

    private fun init(): Boolean
    {
        addView(view)
        items.clear()
        rvAdapter = AdapterForRecyclerWithImageAndSubtitle(context, items, FileExplorerClickEvent(this))
        /*
        rvAdapter = AdapterForRecyclerWithImageAndSubtitle(context, items, object : CustomItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d("listener", position.toString() + " : " + items[position])
                currentPath = txtView.text.toString()
                val path = items[position].titleMain
                if (path == "src/test") {
                    prevPath()
                } else {
                    nextPath(path)
                }
            }
        })
        */
        rvAdapter.hasStableIds()
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = rvAdapter
        rcView.isNestedScrollingEnabled = false
        rcView.isScrollbarFadingEnabled = false
        if(!setFileList(rootPath)) { return false }

        return true
    }

    private fun setFileList(path : String) : Boolean
    {
        val file = File(path)
        if(!file.isDirectory)
        {
            Toast.makeText(context, "Not Directory", Toast.LENGTH_SHORT).show()
            return false
        }
        val fileList = file.list { _, name ->
            File("$path/$name").isDirectory
                    || name.endsWith(".mp3")
        }
        if(fileList == null)
        {
            Toast.makeText(context, "Could not find file list", Toast.LENGTH_SHORT).show()
            return false
        }
        txtView.text = path
        currentPath = path
        items.clear()
        items.add(ItemForRecyclerWithImageAndSubtitle(R.drawable.baseline_folder_black_48, "..", "text_sub"))
        for (singleFile in fileList)
        {
            val resID = if(File("$path/$singleFile").isDirectory) R.drawable.baseline_folder_black_48 else 0
            items.add(ItemForRecyclerWithImageAndSubtitle(resID, singleFile.toString(), "text_sub"))
        }
        rvAdapter.notifyDataSetChanged()
        rcView.scrollToPosition(0)
        return true
    }

    // Can be called at MainActivity
    fun nextPath(str : String) : Boolean
    {
        prevPath = currentPath
        nextPath = "$currentPath/$str"

        return setFileList(nextPath)
    }

    fun prevPath() : Boolean
    {
        nextPath = currentPath
        prevPath = currentPath.substring(0, currentPath.lastIndexOf("/"))

        return setFileList(prevPath)
    }

    class FileExplorerClickEvent(private val explorer : FileExplorerView) : CustomItemClickListener
    {
        override fun onItemClick(view: View, position: Int) {
            Log.d("listener", position.toString() + " : " + explorer.items[position])
            explorer.currentPath = explorer.txtView.text.toString()
            val path = explorer.items[position].titleMain
            if (path == "..") {
                explorer.prevPath()
            } else {
                explorer.nextPath(path)
            }
        }
    }
}