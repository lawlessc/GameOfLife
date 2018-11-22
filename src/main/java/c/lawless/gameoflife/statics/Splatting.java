package c.lawless.gameoflife.statics;

import com.threed.jpct.*;

public class Splatting {

    TextureManager tm = TextureManager.getInstance();

    Boolean textureCompression= true;
    Boolean textureFiltering= false;
    Boolean textureMipMap= false;
    Boolean setGLFiltering=false;


    public NPOTTexture splat_tex;
    public Object3D  draw_obj_one = null;


    public GLSLShader shader;

    public World displayWorld;

    int GridWidth;
    int GridHeight;

    public Splatting(FrameBuffer fb, World world, GLSLShader shader, IRenderHook hook)
    {
        this.shader= shader;
        this.displayWorld= world;

        splat_tex = new NPOTTexture(GridWidth , GridHeight, RGBColor.BLACK);
        splat_tex.setFiltering(textureFiltering);
        splat_tex.setMipmap(textureMipMap);
        splat_tex.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("splatt", splat_tex);



        draw_obj_one = Primitives.getPlane(4,10);
        draw_obj_one.setOrigin(new SimpleVector(0.01, 0, 0));
        draw_obj_one.setRenderHook(hook);
        draw_obj_one.setShader(shader);
        draw_obj_one.setCulling(false);
        draw_obj_one.compile();
        displayWorld.addObject(draw_obj_one);
        draw_obj_one.setVisibility(false);

    }




    public void update(FrameBuffer fb)
    {

        fb.setRenderTarget(splat_tex);
        draw_obj_one.setVisibility(true);
        fb.clear();
        displayWorld.renderScene(fb);
        displayWorld.draw(fb);
        fb.display();
        draw_obj_one.setVisibility(false);
        fb.removeRenderTarget();
    }





}
