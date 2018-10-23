package c.lawless.gameoflife;

import com.threed.jpct.GLSLShader;
import com.threed.jpct.IRenderHook;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

/**
 * Created by Chris on 29/05/2016.
 *
 * Basically so i can tap the screen
 */
public class SplatHook implements IRenderHook {

    PostProcessHandler parent;
    GLSLShader impulse;
    SimpleVector fillcol = new SimpleVector(1,1,1);


    public SplatHook(PostProcessHandler parent , GLSLShader impulse)
    {
        this.parent=parent;
        this.impulse =impulse;
    }

    @Override
    public void beforeRendering(int i) {


        //impulse.setStaticUniform("aspectRatio", parent.AspectRatio);
        impulse.setStaticUniform("on", parent.splat_on ? 1 : 0);
        impulse.setStaticUniform("Point", parent.splatPos);
        impulse.setStaticUniform("Radius", parent.splatRadius);
        impulse.setStaticUniform("FillColor", fillcol);
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
