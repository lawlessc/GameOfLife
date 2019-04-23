package c.lawless.gameoflife

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import c.lawless.gameoflife.StorageStuff.*
import io.objectbox.kotlin.boxFor


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("c.lawless.gameoflife", appContext.packageName)
    }


    @Test
    fun create_objectBox() {
        val appContext = InstrumentationRegistry.getTargetContext()
        ObjectBox.build(appContext)
    }

    @Test
    fun conversionTest() {

        var vec= intArrayOf(1, 0, 1, 0, 1,1,1,1,1,1,1,0)
        var byteArray=   convertIntegersToBytes(vec)
        val tex = convertBytestoIntegers(byteArray)
        assertEquals(compareArrays(vec,tex), true)
    }


    @Test
    fun SaveLoadTest() {
        val appContext = InstrumentationRegistry.getTargetContext()
        ObjectBox.build(appContext)

        deleteAllFiles()//Prepares for test.

        var vec= intArrayOf(1, 0, 1, 0, 1,1,1,1,1,1,1,0,1,1,1,1)
        var byteArray=   convertIntegersToBytes(vec)


        //Saving
        var now = LocalDateTime.now()
        var time_now = DateTimeFormatter.ISO_INSTANT.format(now.toInstant(ZoneOffset.UTC))
        var box = ObjectBox.boxStore.boxFor<GOFSave>()
        box.put(GOFSave(0, time_now, byteArray, 4,4))


        //Reloading
        var savefile = box.all[0]
        var byteArrayL = savefile.savedImage
        val tex = convertBytestoIntegers(byteArrayL)


        assertEquals(compareArrays(vec,tex), true)
    }




}