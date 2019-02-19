package c.lawless.gameoflife.StorageStuff
import org.json.JSONObject
import android.content.Context
import io.objectbox.Box
import io.objectbox.query.Query


import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query



abstract  class GetSaveData (context : Context){
      private lateinit var saveBox: Box<GOFSave>
      private lateinit var saveQuery: Query<GOFSave>

    var context = context
    var saveDataArray : ArrayList<GOFSave> = ArrayList()

    fun getData(response: String) {

        saveBox = ObjectBox.boxStore.boxFor()
//
//        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
//        notesQuery = notesBox.query {
//            order(Note_.text)

        dataReady(saveDataArray)

         }




   abstract fun  dataReady(dataArray : ArrayList<GOFSave>)

}
