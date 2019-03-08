package c.lawless.gameoflife
import c.lawless.gameoflife.RenderHooks.GOLRenderHook
import c.lawless.gameoflife.StorageStuff.*
import c.lawless.gameoflife.statics.GridSizes
import com.threed.jpct.*

class Processing {

    internal var tm = TextureManager.getInstance()

    internal var textureCompression: Boolean = false//Setting this true will cause loading of saves to fail.
    internal var textureFiltering: Boolean = false
    internal var textureMipMap: Boolean = false
    internal var setGLFiltering: Boolean = false

    lateinit var frame_one: NPOTTexture//Textures for alternating frames.
    lateinit var frame_two: NPOTTexture

    lateinit var fameObjOne: Object3D
    lateinit var fameObjTwo: Object3D

    lateinit internal var GOF_Hook: GOLRenderHook
    //lateinit var GOF_shader: GLSLShader

    var displayWorld: World

    //public  Boolean switcher= true;
    var save = false
    var loadTest = false
    //var loatTestStageOne= false;

    var load_id: Long =0


    constructor(world: World) {
        this.displayWorld = world
        setupTextures()
    }

    constructor(world: World, loadid: Long) {
        this.displayWorld = world
        this.load_id = loadid
        this.loadTest = true
        val save = loadFile(load_id)

      //replaceTextures(save.width,save.height)
        GridSizes.GridWidth=save.width;
        GridSizes.GridHeight=save.height;
        replaceTextures()
    }


    fun setupObjects(pp: PostProcessHandler, GOF_shader: GLSLShader) {
        GOF_Hook = GOLRenderHook(pp, GOF_shader)

        fameObjOne = Primitives.getPlane(4, 10f)
        fameObjOne.origin = SimpleVector(0.01, 0.0, 0.0)
        fameObjOne.renderHook = GOF_Hook
        fameObjOne.shader = GOF_shader
        val one = TextureInfo(TextureManager.getInstance().getTextureID("frameone"))
        one.add(TextureManager.getInstance().getTextureID("splatt"), TextureInfo.MODE_ADD)
        fameObjOne.setTexture(one)
        fameObjOne.culling = false
        fameObjOne.compile()
        displayWorld.addObject(fameObjOne)
        fameObjOne.visibility = false

        fameObjTwo = Primitives.getPlane(4, 10f)
        fameObjTwo.origin = SimpleVector(0.01, 0.0, 0.0)
        fameObjTwo.renderHook = GOF_Hook
        fameObjTwo.shader = GOF_shader
        val two = TextureInfo(TextureManager.getInstance().getTextureID("frametwo"))
        two.add(TextureManager.getInstance().getTextureID("splatt"), TextureInfo.MODE_ADD)
        fameObjTwo.setTexture(two)
        fameObjTwo.culling = false
        fameObjTwo.compile()
        displayWorld.addObject(fameObjTwo)
        fameObjTwo.visibility = false
    }

    fun setupTextures()
    {
        frame_one = NPOTTexture(GridSizes.GridWidth, GridSizes.GridHeight, RGBColor.BLACK)
        frame_one.setFiltering(textureFiltering)
        frame_one.setMipmap(textureMipMap)
        frame_one.setTextureCompression(textureCompression)
        frame_one.setClamping(false);
        tm.addTexture("frameone", frame_one)

        frame_two = NPOTTexture(GridSizes.GridWidth, GridSizes.GridHeight, RGBColor.BLACK)
        frame_two.setFiltering(textureFiltering)
        frame_two.setMipmap(textureMipMap)
        frame_two.setTextureCompression(textureCompression)
        frame_two.setClamping(false);
        tm.addTexture("frametwo", frame_two)
    }

    fun replaceTextures() {

        val frame_one_ = NPOTTexture(GridSizes.GridWidth, GridSizes.GridHeight, RGBColor.BLACK)
        frame_one_.setFiltering(textureFiltering)
        frame_one_.setMipmap(textureMipMap)
        frame_one_.setTextureCompression(textureCompression)
        frame_one_.setClamping(false);
        tm.replaceTexture("frameone", frame_one_)

        val frame_two_ = NPOTTexture(GridSizes.GridWidth, GridSizes.GridHeight, RGBColor.BLACK)
        frame_two_.setFiltering(textureFiltering)
        frame_two_.setMipmap(textureMipMap)
        frame_two_.setTextureCompression(textureCompression)
        frame_two_.setClamping(false);
        tm.replaceTexture("frametwo", frame_two_)

        frame_one = frame_one_
        frame_two = frame_two_
    }

    fun replaceTextures(width: Int, height: Int) {

        val frame_one_ = NPOTTexture(width, height, RGBColor.BLACK)
        frame_one_.setFiltering(textureFiltering)
        frame_one_.setMipmap(textureMipMap)
        frame_one_.setTextureCompression(textureCompression)
        frame_one_.setClamping(false);
        tm.replaceTexture("frameone", frame_one_)

        val frame_two_ = NPOTTexture(width, height, RGBColor.BLACK)
        frame_two_.setFiltering(textureFiltering)
        frame_two_.setMipmap(textureMipMap)
        frame_two_.setTextureCompression(textureCompression)
        frame_two_.setClamping(false);
        tm.replaceTexture("frametwo", frame_two_)

        frame_one = frame_one_
        frame_two = frame_two_
    }

    fun process(FB: FrameBuffer, switcher: Boolean)
    {
        loadFrame(FB, load_id)

        if (switcher) {

            FB.setRenderTarget(frame_two)
            fameObjOne.visibility = true
            FB.clear()
            displayWorld.renderScene(FB)
            displayWorld.draw(FB)
            FB.display()
            fameObjOne.visibility = false
            saveFrame(FB)
            FB.removeRenderTarget()
        } else {
            FB.setRenderTarget(frame_one)
            fameObjTwo.visibility = true
            FB.clear()
            displayWorld.renderScene(FB)
            displayWorld.draw(FB)
            FB.display()
            fameObjTwo.visibility = false
            saveFrame(FB)
            FB.removeRenderTarget()
        }
    }

    fun saveFrame(fb: FrameBuffer) {


        if (save) {
          //  if(loatTestStageOne) {
                fb.resize(GridSizes.GridWidth, GridSizes.GridHeight)
                frameSaver(fb, GridSizes.GridWidth, GridSizes.GridHeight)
                //frameConversionTest(fb, GridSizes.GridWidth, GridSizes.GridHeight)

                save = false
           // }
         //   loatTestStageOne= true
        }

    }

    fun loadFrame(FB: FrameBuffer, id: Long) {
        if (loadTest) {
            val save = loadFile(id)
            val tex = convertBytestoIntegers(save.savedImage)
            replaceTextures(save.width, save.height)

            FB.resize(save.width, save.height)
            FB.setRenderTarget(frame_one)
            FB.clear()

            blitLoad(FB, tex, save.width, save.height)

            FB.display()

            displayWorld.renderScene(FB)
            displayWorld.draw(FB)

            FB.setRenderTarget(frame_two)
            FB.clear()

            blitLoad(FB, tex, save.width, save.height)



            FB.display()
            FB.removeRenderTarget()

            loadTest = false
        }
    }

    fun blitLoad(fb: FrameBuffer, tex: IntArray, width: Int, height: Int){
        fb.blit(
            tex,
            width,
            height,
            0,
            0,
            0,
            0,
            width,
            height,
            FrameBuffer.OPAQUE_BLITTING)//if set to true the blit overlays the previous screen.
    }
}
