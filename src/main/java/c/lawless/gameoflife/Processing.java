package c.lawless.gameoflife;

import c.lawless.gameoflife.statics.GridSizes;
import com.threed.jpct.*;

public class Processing {

    TextureManager tm = TextureManager.getInstance();

    Boolean textureCompression= true;
    Boolean textureFiltering= false;
    Boolean textureMipMap= false;
    Boolean setGLFiltering=false;

    public NPOTTexture frame_one;//Textures for alternating frames.
    public NPOTTexture frame_two;
    public NPOTTexture splat_tex;



    public GLSLShader GOF_shader;

    public Object3D fameObjOne = null;
    public Object3D fameObjTwo = null;
    public World displayWorld;



    public Processing(FrameBuffer fb, World world, GLSLShader gof_shader, IRenderHook hook)
    {
        this.GOF_shader= gof_shader;
        this.displayWorld= world;


        fameObjOne = Primitives.getPlane(4,10);
        fameObjOne.setOrigin(new SimpleVector(0.01, 0, 0));
        fameObjOne.setRenderHook(hook);
        fameObjOne.setShader(GOF_shader);
        TextureInfo one  =  new TextureInfo(TextureManager.getInstance().getTextureID("frameone"));
        one.add(TextureManager.getInstance().getTextureID("splatt"), TextureInfo.MODE_ADD);
        fameObjOne.setTexture(one);
        fameObjOne.setCulling(false);
        fameObjOne.compile();
        // advectingObj.strip();
        displayWorld.addObject(fameObjOne);
        fameObjOne.setVisibility(false);

        fameObjTwo = Primitives.getPlane(4,10);
        fameObjTwo.setOrigin(new SimpleVector(0.01, 0, 0));
        fameObjTwo.setRenderHook(hook);
        fameObjTwo.setShader(GOF_shader);
        TextureInfo two  =  new TextureInfo(TextureManager.getInstance().getTextureID("frametwo"));
        two.add(TextureManager.getInstance().getTextureID("splatt"), TextureInfo.MODE_ADD);
        fameObjTwo.setTexture(two);
        fameObjTwo.setCulling(false);
        fameObjTwo.compile();
        displayWorld.addObject(fameObjTwo);
        fameObjTwo.setVisibility(false);

        frame_one = new NPOTTexture(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight(), RGBColor.BLACK);
        frame_one.setFiltering(textureFiltering);
        frame_one.setMipmap(textureMipMap);
        frame_one.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("frameone", frame_one);

        frame_two = new NPOTTexture(GridSizes.INSTANCE.getGridWidth(), GridSizes.INSTANCE.getGridHeight(), RGBColor.BLACK);
        frame_two.setFiltering(textureFiltering);
        frame_two.setMipmap(textureMipMap);
        frame_two.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
        tm.addTexture("frametwo", frame_two);

//
//        splat_tex = new NPOTTexture(GridWidth , GridHeight, RGBColor.BLACK);
//        splat_tex.setFiltering(textureFiltering);
//        splat_tex.setMipmap(textureMipMap);
//        splat_tex.setTextureCompression(textureCompression);// texture compression eliminates the artifacts
//        tm.addTexture("splatt", splat_tex);



    }





}
