package c.lawless.gameoflife.RenderHooks;

import c.lawless.gameoflife.PostProcessHandler;
import c.lawless.gameoflife.Processing;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.IRenderHook;
import com.threed.jpct.Object3D;
/**
 * Created by Chris on 29/05/2016.
 */
public class GOLRenderHook implements IRenderHook {


    PostProcessHandler parent;
   // Processing parent;
    GLSLShader shader;

    public GOLRenderHook(PostProcessHandler parent , GLSLShader shader)
    {
        this.parent=parent;
        this.shader =shader;
    }



    @Override
    public void beforeRendering(int i) {
        shader.setStaticUniform("paused", parent.main.isActionPaused() ? 1.0f : 0.0f);
        shader.setStaticUniform("inversesizex", parent.InverseSizex);
        shader.setStaticUniform("inversesizey", parent.InverseSizey);
    }

    @Override
    public void afterRendering(int i) {

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