package c.lawless.gameoflife;

import com.threed.jpct.GLSLShader;
import com.threed.jpct.IRenderHook;
import com.threed.jpct.Object3D;

/**
 * Created by Chris on 29/05/2016.
 */
public class GOLRenderHook implements IRenderHook {


    PostProcessHandler parent;
    GLSLShader shader;


    public GOLRenderHook(PostProcessHandler parent , GLSLShader shader)
    {
        this.parent=parent;
        this.shader =shader;
    }


    @Override
    public void beforeRendering(int i) {
        shader.setStaticUniform("inversesize", parent.InverseSize);
        shader.setStaticUniform("firstrun", parent.first_run ? -1 : 1);

    }

    @Override
    public void afterRendering(int i) {
        parent.first_run =false;
    }

    @Override
    public void setCurrentObject3D(Object3D object3D) {

    }

    @Override
    public void setCurrentShader(GLSLShader glslShader) {

    }

    @Override
    public void setTransparency(float v) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public boolean repeatRendering() {
        return false;
    }
}
