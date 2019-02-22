package c.lawless.gameoflife.StorageStuff


import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


@Entity class GOFSave(@Id var id: Long =0,
                       var saveName: String,
                       var savedImage: ByteArray?,
                       var width: Int,
                        var height: Int
)


