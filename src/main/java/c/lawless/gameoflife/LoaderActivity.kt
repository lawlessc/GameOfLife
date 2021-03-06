package c.lawless.gameoflife

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import c.lawless.gameoflife.StorageStuff.GOFAdapter
import c.lawless.gameoflife.StorageStuff.GOFSave
import c.lawless.gameoflife.StorageStuff.GetSaveData
import c.lawless.gameoflife.StorageStuff.deleteAllFiles

class LoaderActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)

        class GSaveData (context: Context): GetSaveData(context)
        {
            override fun dataReady(dataArray: ArrayList<GOFSave>) {
                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                viewManager = LinearLayoutManager(context) as RecyclerView.LayoutManager
                viewAdapter = GOFAdapter(dataArray, context)
                recyclerView = findViewById<RecyclerView>(R.id.save_recyclerview).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)
                    // use a linear layout manager
                    layoutManager = viewManager
                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter
                }
            }
        }

        GSaveData(this)//Loads saves from memory

        val deleteAll: Button = findViewById(R.id.delete_all)
        deleteAll.setOnClickListener{
            deleteAllFiles()
            GSaveData(this)//Call this again to refresh menu
        }

    }
}
