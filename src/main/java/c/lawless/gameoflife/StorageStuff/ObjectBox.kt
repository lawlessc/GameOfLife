package c.lawless.gameoflife.StorageStuff



import android.content.Context
import android.os.Environment
import io.objectbox.BoxStore
import java.io.File

/**
 * Singleton to keep BoxStore reference.
 */
object ObjectBox {

    lateinit var boxStore: BoxStore
        private set

    fun build(context: Context) {
        // This is the minimal setup required on Android

        if (!::boxStore.isInitialized) {
            boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
        }


        // Example how you could use a custom dir in "external storage"
        // (Android 6+ note: give the app storage permission in app info settings)
//        val directory = File(Environment.getExternalStorageDirectory(), "objectbox-notes");
//        boxStore = MyObjectBox.builder().androidContext(context.applicationContext)
//                .directory(directory)
//                .build()
    }

    fun getbox(context: Context) :BoxStore
    {

        if (!::boxStore.isInitialized) {
            boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
           return boxStore
        }
     return boxStore
    }


}