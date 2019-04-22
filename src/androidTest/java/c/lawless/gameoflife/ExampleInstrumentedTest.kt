package c.lawless.gameoflife

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import c.lawless.gameoflife.StorageStuff.ObjectBox
import c.lawless.gameoflife.StorageStuff.convertBytestoIntegers
import c.lawless.gameoflife.StorageStuff.convertIntegersToBytes
import c.lawless.gameoflife.StorageStuff.compareArrays


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

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

        var vec= fb.getPixels()
        var byteArray=   convertIntegersToBytes(vec)
        val tex = convertBytestoIntegers(byteArray)
        assertEquals(compareArrays(vec,tex), true)
    }



}