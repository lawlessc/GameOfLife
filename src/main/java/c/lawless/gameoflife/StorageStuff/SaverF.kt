package c.lawless.gameoflife.StorageStuff

import android.graphics.Bitmap
import c.lawless.gameoflife.MainActivity
import c.lawless.gameoflife.StorageStuff.ObjectBox.boxStore
import c.lawless.gameoflife.statics.GridSizes
import c.lawless.gameoflife.statics.TextureNames
import io.objectbox.kotlin.boxFor
import java.io.ByteArrayOutputStream
import java.nio.IntBuffer
import android.graphics.BitmapFactory
import com.threed.jpct.*
import com.threed.jpct.util.BitmapHelper
import java.nio.ByteBuffer
//import com.sun.deploy.trace.Trace.flush
import java.io.DataOutputStream
import java.nio.ByteOrder
import java.nio.ByteOrder.BIG_ENDIAN
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.microedition.khronos.opengles.GL11
import java.time.format.DateTimeFormatter.ISO_INSTANT




/*Christopher Lawless 2018/2019 */


fun frameSaver(fb :FrameBuffer ,world: World)
{
    //We render to the save texture here
    fb.setRenderTarget(TextureManager.getInstance().getTexture(TextureNames.savetexture))
    world.renderScene(fb)
    world.draw(fb)
    fb.display()
    fb.removeRenderTarget()

    //retrieve rendered to texture
    var texture_to_save=  TextureManager.getInstance().getTexture(TextureNames.savetexture)

     //create a bitmap of the image
    //var bitmap = Bitmap.createBitmap(texture_to_save.width, texture_to_save.height, Bitmap.Config.ARGB_8888)
    var vec= fb.getPixels()

   var byteArray=   convertIntegersToBytes(vec);


    //We get date time for title, for now
    val now = LocalDateTime.now()
    val time_now = DateTimeFormatter.ISO_INSTANT.format(now.toInstant(ZoneOffset.UTC))

    //Save the byte array to a boxstore , useing current date time as a filename.

    val box = boxStore.boxFor<GOFSave>()
    box.put(GOFSave(0,

        time_now,
        GridSizes.size_level,
        byteArray,
        texture_to_save.width,
        texture_to_save.height))
}






fun frameTestLoader(fb :FrameBuffer ,id: Long)
{
   val box = boxStore.boxFor<GOFSave>()

   val  savefile = box.get(id)

   val vec= savefile.savedImage

   val fin = convertBytestoIntegers(vec)

   doBlit(fb,fin,savefile.width, savefile.height);
}


fun loadFile(id :Long):GOFSave
{
    val box = boxStore.boxFor<GOFSave>()
    val  savefile = box.get(id)
    return  savefile;
}

fun deleteAllFiles()
{
   boxStore.boxFor<GOFSave>().removeAll()
}



fun doBlit(fb: FrameBuffer, tex : IntArray, width: Int, height: Int) {

    fb.resize(width, height)
    fb.blit(tex ,width,height, 0, 0, 0,0,
        width, height,
        false)//if set to true the blit overlays the previous screen.
}

//https://stackoverflow.com/questions/1086054/how-to-convert-int-to-byte
fun convertIntegersToBytes(integers: IntArray?): ByteArray? {
    if (integers != null) {
        val outputBytes = ByteArray(integers.size * 4)

        var i = 0
        var k = 0
        while (i < integers.size) {
            val integerTemp = integers[i]
            var j = 0
            while (j < 4) {
                outputBytes[k] = (integerTemp shr 8 * j and 0xFF).toByte()
                j++
                k++
            }
            i++
        }
        return outputBytes
    } else {
        return null
    }
}

//Got from http://www.java2s.com/Tutorial/Java/0180__File/ConvertBytearraytoInt.htm
fun convertBytestoIntegers( bytes : ByteArray? ): IntArray
{
    val intBuf = ByteBuffer.wrap(bytes)
        .order(ByteOrder.LITTLE_ENDIAN)
        .asIntBuffer()
    val array = IntArray(intBuf.remaining())
    intBuf.get(array)

    return array
}