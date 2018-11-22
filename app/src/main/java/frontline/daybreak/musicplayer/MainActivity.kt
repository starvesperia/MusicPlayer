package frontline.daybreak.musicplayer

import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TabHost
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    private val tabHost by lazy { tab_host }
    private val tab1View by lazy { tab1 }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabHost.setup()
        albumArtObjectInit()

        val tab1 = tabHost.newTabSpec("t1").setContent(R.id.tab1).setIndicator("TAB1") as TabHost.TabSpec
        val tab2 = tabHost.newTabSpec("t2").setContent(R.id.tab2).setIndicator("TAB2") as TabHost.TabSpec
        val tab3 = tabHost.newTabSpec("t3").setContent(R.id.tab3).setIndicator("TAB3") as TabHost.TabSpec

        tabHost.addTab(tab1)
        tabHost.addTab(tab2)
        tabHost.addTab(tab3)
    }

    private fun albumArtObjectInit()
    {
        val cursorCol = arrayOf(
            MediaStore.Audio.Albums.ALBUM_ART, // Album art
            MediaStore.Audio.Albums._ID
        )

        val albumCursor = applicationContext.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            cursorCol, null, null, null
        ) as Cursor

        if (albumCursor != null)
        {
            if (albumCursor.moveToFirst())
            {
                val albumArt = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
                val albumId = albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID)
                do
                {
                    if (!AlbumArtGetter.mapAlbumArt.containsKey(Integer.parseInt(albumCursor.getString(albumId))))
                    {
                        val aaPath = albumCursor?.getString(albumArt) // ?. is necessary
                        if(aaPath != null) {
                            AlbumArtGetter.mapAlbumArt[Integer.parseInt(albumCursor.getString(albumId))] = aaPath
                        }
                    }
                } while (albumCursor.moveToNext())
            }
        }
        albumCursor!!.close()
    }


    override fun onBackPressed()
    {
        if(tabHost.currentTabTag == "t1") {
            if(!tab1View.prevPath()) { super.onBackPressed() }
        }
        else {
            super.onBackPressed()
        }
    }
}
