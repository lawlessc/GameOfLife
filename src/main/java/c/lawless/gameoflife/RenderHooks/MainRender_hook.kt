package c.lawless.gameoflife.RenderHooks
import c.lawless.gameoflife.PostProcessHandler
import com.threed.jpct.GLSLShader
import com.threed.jpct.IRenderHook
import com.threed.jpct.Object3D
import com.threed.jpct.SimpleVector
/*
 * Created by Chris on 29/05/2016.
 */
class MainRender_hook(internal var parent: PostProcessHandler, internal var shader: GLSLShader) : IRenderHook {

    internal var light_col = SimpleVector(1f, 1f, 1f)
    internal var dark_col = SimpleVector(0f, 0f, 0f)

    override fun beforeRendering(i: Int) {

        //impulse.setStaticUniform("aspectRatio", parent.AspectRatio);
        shader.setUniform("light_col", light_col)
        shader.setUniform("dark_col", dark_col)
    }

    override fun afterRendering(i: Int) {
    }

     fun setColours(light : SimpleVector, dark :SimpleVector) {
         light_col=light
         dark_col=dark
    }

    override fun setCurrentObject3D(object3D: Object3D) {
    }

    override fun setCurrentShader(glslShader: GLSLShader) {
    }

    override fun setTransparency(v: Float) {
    }

    override fun onDispose() {
    }

    override fun repeatRendering(): Boolean {
        return false
    }
}
