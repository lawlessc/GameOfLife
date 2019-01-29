package c.lawless.gameoflife.StorageStuff

import android.graphics.Bitmap
import c.lawless.gameoflife.MainActivity
import c.lawless.gameoflife.StorageStuff.ObjectBox.boxStore
import c.lawless.gameoflife.statics.GridSizes
import c.lawless.gameoflife.statics.TextureNames
import com.threed.jpct.FrameBuffer
import com.threed.jpct.TextureManager
import com.threed.jpct.World
import io.objectbox.kotlin.boxFor
import java.io.ByteArrayOutputStream
import java.nio.IntBuffer

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
    var bitmap = Bitmap.createBitmap(texture_to_save.width, texture_to_save.height, Bitmap.Config.ARGB_8888)
    var vec= fb.getPixels()
    bitmap.copyPixelsFromBuffer(IntBuffer.wrap(vec));


    //Convert the bitmap to a byte array
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    bitmap.recycle()


    //Save the byte array to a boxstore , useing current date time as a filename.
    val box = boxStore.boxFor<GOFSave>()
    box.put(GOFSave(0, java.util.Calendar.getInstance().toString(), GridSizes.size_level, byteArray))


}




fun frameLoader(fb :FrameBuffer ,world: World)
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
    var bitmap = Bitmap.createBitmap(texture_to_save.width, texture_to_save.height, Bitmap.Config.ARGB_8888)
    var vec= fb.getPixels()
    bitmap.copyPixelsFromBuffer(IntBuffer.wrap(vec));


    //Convert the bitmap to a byte array
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    bitmap.recycle()


    //Save the byte array to a boxstore , useing current date time as a filename.
    val box = boxStore.boxFor<GOFSave>()
    box.put(GOFSave(0, java.util.Calendar.getInstance().toString(), GridSizes.size_level, byteArray))


}

