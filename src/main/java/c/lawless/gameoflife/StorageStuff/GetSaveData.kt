package c.lawless.gameoflife.StorageStuff
import android.content.Context
import io.objectbox.Box
import io.objectbox.query.Query
import io.objectbox.kotlin.boxFor


abstract  class GetSaveData (context : Context){
      private lateinit var saveBox: Box<GOFSave>
      private lateinit var saveQuery: Query<GOFSave>
    var context = context
    var saveDataArray : ArrayList<GOFSave> = ArrayList()

    init{getData()}

    fun getData() {

        saveBox = ObjectBox.getbox(context).boxFor()

        val box = ObjectBox.boxStore.boxFor<GOFSave>()
        val  savefiles = box.all


            for (i in savefiles!!.size-1 downTo 0 ) {
                saveDataArray.add(savefiles[i])

            }

        dataReady(saveDataArray)
         }

   abstract fun  dataReady(dataArray : ArrayList<GOFSave>)
}
