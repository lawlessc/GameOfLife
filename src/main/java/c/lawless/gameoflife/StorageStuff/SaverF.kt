package c.lawless.gameoflife.StorageStuff

import android.graphics.Bitmap
import c.lawless.gameoflife.statics.TextureNames
import com.threed.jpct.FrameBuffer
import com.threed.jpct.TextureManager
import com.threed.jpct.World
import java.io.ByteArrayOutputStream
import java.nio.IntBuffer


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


    var bitmap = Bitmap.createBitmap(texture_to_save.width, texture_to_save.height, Bitmap.Config.ARGB_8888)
    var vec= fb.getPixels()
    bitmap.copyPixelsFromBuffer(IntBuffer.wrap(vec));


    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    bitmap.recycle()

    //Get ID and name etc here then save object to box.
    saveObj = GOFSave()


}

fun store(fb :FrameBuffer, world: World) {


}