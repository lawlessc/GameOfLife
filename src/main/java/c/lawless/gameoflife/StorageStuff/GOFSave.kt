package c.lawless.gameoflife.StorageStuff

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity class GOFSave(@Id var id: Long =0,
                       var saveName: String,
                       var size: Int,
                       var savedImage: ByteArray?,
                       var width: Int,
                        var height: Int
)



//  val image: Bitmap
//  get() = BitmapFactory.decodeByteArray(savedImage, 0, savedImage.size)