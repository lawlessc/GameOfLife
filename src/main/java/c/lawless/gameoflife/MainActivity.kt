package c.lawless.gameoflife

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.app.FragmentManager
import android.content.Context
import android.content.res.Resources
import android.opengl.GLSurfaceView
import android.support.v4.app.FragmentActivity
import android.support.v4.view.MotionEventCompat

import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.widget.Button
import android.widget.ImageButton

import com.threed.jpct.Camera
import com.threed.jpct.Config
import com.threed.jpct.FrameBuffer
import com.threed.jpct.Logger
import com.threed.jpct.RGBColor
import com.threed.jpct.SimpleVector
import com.threed.jpct.Texture
import com.threed.jpct.World
import com.threed.jpct.util.AAConfigChooser
import com.threed.jpct.util.MemoryHelper

import java.lang.reflect.Field

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * @author Christopher Lawless
 */
class MainActivity : AppCompatActivity (), OnScaleGestureListener /*,Observer */ {

    private var master: MainActivity? = null
    private var mGLView: GLSurfaceView? = null
    private var renderer: MyRenderer? = null
    private var fb: FrameBuffer? = null

    internal var MS_PER_UPDATE: Long = 16

    var previous: Long = 0
    var current: Long = 0
    var lag: Long = 0




    private var mScaleDetector: ScaleGestureDetector? = null
    private var tapdetection: GestureDetector? = null

    //private Texture font = null;
    private var texturesLoaded: Boolean? = false
    var isActionPaused: Boolean? = false

     enum class allGameObjects {
        INSTANCE;
        var processHandler: PostProcessHandler? = null
    }
    //////////////////////////////////This needs to be seperated from the graphical stuff above somehow.


    override fun onCreate(savedInstanceState: Bundle?) {

        Logger.log("onCreate")
        //Logger.setLogLevel(Logger.LL_DEBUG);
        //Logger.setLogLevel(Logger.);

        Logger.setLogLevel(Logger.DEBUG)

        //Context baseContext= this.getBaseContext();
        //Resources res = getResources();

        if (master != null) {
            copy(master!!)
        }

        super.onCreate(savedInstanceState)



        if (master == null) {
            //This Sets Renderer , I may want to seperate these classes!
            setContentView(R.layout.activity_main) //or whatever the layout you want to use
            mGLView = findViewById<View>(R.id.glsurfaceview1) as GLSurfaceView


            //mGLView = new GLSurfaceView(getApplication());
            // Enable the OpenGL ES2.0 context
            mGLView!!.setEGLContextClientVersion(2)
            //
            //mGLView.setEGLConfigChooser(new AAConfigChooser(mGLView));


            renderer = MyRenderer()
            mGLView!!.setRenderer(renderer)

            Texture.defaultToMipmapping(false)
            Texture.defaultTo4bpp(false)
            Texture.defaultToKeepPixels(true)


            Config.unloadImmediately = false
            Config.reuseTextureBuffers=true
            Config.maxLights=1;
            // Config.


            Config.maxPolysVisible = 50
            Config.farPlane = 10f
            Config.nearPlane = 0f
           // Config.glDebugLevel=1
            //Config.maxTextureLayers = 3

            //Config
           // Config.glDebugLevel
            //  Config.
        }



        if ((!texturesLoaded!!)!!) {
            val baseContext = this.baseContext
            texturesLoaded = true
        }


        val button: Button = findViewById(R.id.colourpicker)
        button.setOnClickListener {
            // Do something in response to button click
            allGameObjects.INSTANCE.processHandler!!.changeColours();
        }

        val pause_button: Button = findViewById(R.id.pause)
        pause_button.setOnClickListener {
            // Do something in response to button click
            isActionPaused = !isActionPaused!!
        }



        val big_pen_button: Button = findViewById(R.id.pen)
        big_pen_button.setOnClickListener {
            // Do something in response to button click
            allGameObjects.INSTANCE.processHandler!!.splatRadius =  fb!!.getWidth() /16.0f
        }

        val shrink_pen_button: Button = findViewById(R.id.pen2)
        shrink_pen_button.setOnClickListener {
            // Do something in response to button click
            allGameObjects.INSTANCE.processHandler!!.splatRadius =  1f

        }

        val random_fill: Button = findViewById(R.id.ranom_fill)
        random_fill.setOnClickListener {

            allGameObjects.INSTANCE.processHandler!!.random_fill =  true

        }

        val clear_grid: ImageButton = findViewById(R.id.clear)
        clear_grid.setOnClickListener {
            // Do something in response to button click
            allGameObjects.INSTANCE.processHandler!!.clear_grid =  true

        }

        val increase_size: Button = findViewById(R.id.increasesize)
        increase_size.setOnClickListener {
            // Do something in response to button click


         //   Thread().run {
                allGameObjects.INSTANCE.processHandler!!.increaseSize()
         //   }

        }
        val deccrease_size: Button = findViewById(R.id.decreasesize)
        deccrease_size.setOnClickListener {
            // Do something in response to button click

          //  Thread().run {
                allGameObjects.INSTANCE.processHandler!!.decreaseSize()
          //  }

          //  allGameObjects.INSTANCE.processHandler!!.decreaseSize()

        }

        val rules_dialogue_test: Button = findViewById(R.id.openrules)
        rules_dialogue_test.setOnClickListener {
            // Do something in response to button click

            //  Thread().run {
            //allGameObjects.INSTANCE.processHandler!!.decreaseSize()

           // FragmentManager fragMan = getFragmentManager()
            val dialog = RulesDialogue()

            dialog.show(fragmentManager, "missiles")


            //  }

            //  allGameObjects.INSTANCE.processHandler!!.decreaseSize()

        }




        mScaleDetector = ScaleGestureDetector(this, ScaleListener())
        tapdetection = GestureDetector(this, TapListener())
        // master = this;
    }


    override fun onPause() {
        isActionPaused = true
        super.onPause()
        mGLView!!.onPause()
    }

    override fun onResume() {
        //		if(allGameObjects.INSTANCE.isActionPaused)
        //		{
        //			copy(master);
        //		}
        super.onResume()
        mGLView!!.onResume()
    }

    override fun onStop() {
        Logger.log("onStop")
        super.onStop()
    }

    private fun copy(src: Any) {
        try {
            Logger.log("Copying data from master Activity!")
            val fs = src.javaClass.declaredFields
            for (f in fs) {
                f.isAccessible = true
                f.set(this, f.get(src))
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

   // private var mActivePointerId = INVALID_POINTER_ID

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        mScaleDetector!!.onTouchEvent(ev)
        tapdetection!!.onTouchEvent(ev)


      // val sizemod = allGameObjects.INSTANCE.processHandler!!.size_modifier


        val action = MotionEventCompat.getActionMasked(ev)

        when (action) {
            MotionEvent.ACTION_DOWN -> {

                                Thread().run {
                    val left = mGLView!!.left
                    val top = mGLView!!.top
                    allGameObjects.INSTANCE.processHandler!!.setSplatPos(ev.getX(), ev.getY())

                }



            }

            MotionEvent.ACTION_MOVE -> {

                Thread().run {
                    val left = mGLView!!.left
                    val top = mGLView!!.top
                    allGameObjects.INSTANCE.processHandler!!.setSplatPos(ev.getX() , ev.getY())

                }

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                Thread().run {
                    allGameObjects.INSTANCE.processHandler!!.splat_on = false
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {


                Thread().run {
                    allGameObjects.INSTANCE.processHandler!!.splat_on = false
                }

            }
        }
        return true
    }


    internal inner class MyRenderer : GLSurfaceView.Renderer {
//        private var fps = 0
//        private var lfps = 0
//        private var time = System.currentTimeMillis()

        override fun onSurfaceChanged(gl: GL10, w: Int, h: Int) {
            if (fb != null) {
                //	fb.dispose();
                fb!!.resize(w, h)
            }
            // else{
            fb = FrameBuffer(w, h)
            //	}
            if (master == null) {

                master = this@MainActivity

                val res = resources
                allGameObjects.INSTANCE.processHandler = PostProcessHandler(res, fb, master)

                current = System.currentTimeMillis()
                lag = 0
                previous = System.currentTimeMillis()

                MemoryHelper.compact()

            }

        }

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {

            //	Logger.log("onSurfaceCreated");
        }

        override fun onDrawFrame(gl: GL10) {
            allGameObjects.INSTANCE.processHandler!!.update()
        }
    }


    override fun onScale(detector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        // TODO Auto-generated method stub
        return true
    }


    override fun onScaleEnd(detector: ScaleGestureDetector) {
        // TODO Auto-generated method stub
    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            return true
        }
    }




    private inner class TapListener : OnGestureListener, GestureDetector.OnDoubleTapListener {

        override fun onDown(e: MotionEvent): Boolean {
            // TODO Auto-generated method stub
            return false
        }



        override fun onFling(
            e1: MotionEvent, e2: MotionEvent, velocityX: Float,
            velocityY: Float
        ): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent) {
            // TODO Auto-generated method stub
        }

        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent, distanceX: Float,
            distanceY: Float
        ): Boolean {
            // TODO Auto-generated method stub
            return false
        }

        override fun onShowPress(e: MotionEvent) {

            // TODO Auto-generated method stub
            //allGameObjects.INSTANCE.processHandler.setSplatPos(e.getX() , e.getY());
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {

            allGameObjects.INSTANCE.processHandler!!.splat_on=false

            // TODO Auto-generated method stub
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            val left = mGLView!!.left
            val top = mGLView!!.top

            return false
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            val left = mGLView!!.left
            val top = mGLView!!.top
            return false
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            ///DONT USE THIS EVER!
            //IT FIRES FAR TOO MANY TIMES ON EACH PRESS, WILL FUCK UP YOUR DAY.
            return false
        }

    }


}
