import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

/**
 * This class represents custom GLJPanel. It allows to rotate and move the camera (see keyPressed method).
 * It also loads some common scene resources.
 */
public class CustomPanel extends GLJPanel implements GLEventListener, KeyListener {
    private float rotateX;
    private float rotateY;
    private float rotateZ;
    private float scale;
    private float translateX;
    private float translateY;
    private float translateZ;
    private float rotationStep = 15;
    private float scaleStep = 1;
    private float translationStep = 1;
    // this variable stores amount of translation necessary to place scene at the center of the screen
    private float defaultXTranslation;
    private int groundTexture;
    private int waterTexture;

    public CustomPanel(GLCapabilities capabilities) {
        this(capabilities, 0);
    }

    public CustomPanel(GLCapabilities capabilities, float defaultXTranslation) {
        super(capabilities);
        this.defaultXTranslation = defaultXTranslation;
        setPreferredSize(new Dimension(500, 500));
        addGLEventListener(this);
        addKeyListener(this);

        setDefault();
    }

    private void setDefault() {
        rotateX = 15;
        rotateY = 15;
        rotateZ = 0;
        scale = 10;
        translateX = defaultXTranslation;
        translateY = 0;
        translateZ = 0;
    }

    public int getGroundTexture() {
        return groundTexture;
    }

    public int getWaterTexture() {
        return waterTexture;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        gl.glEnable(GL2.GL_TEXTURE_2D);
        try {
            File groundTextureFile = new File("ground.jpg");
            Texture ground = TextureIO.newTexture(groundTextureFile, true);
            groundTexture = ground.getTextureObject(gl);

            File waterTextureFile = new File("water.jpg");
            Texture water = TextureIO.newTexture(waterTextureFile, true);
            waterTexture = water.getTextureObject(gl);
        }
        catch (IOException e) {
            System.err.println("Texture error: " + e.getMessage());
        }
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-scale, scale, -scale, scale, -scale, scale);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glRotatef(rotateX, 1, 0, 0);
        gl.glRotatef(rotateY, 0, 1, 0);
        gl.glRotatef(rotateZ, 0, 0, 1);
        gl.glTranslatef(translateX, translateY, translateZ);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Available keys:
     * A, D, W, S, Q, E to rotate the camera
     * J, L, I, K, U, O to move the camera
     * F, C to scale the scene
     * R to restore initial view
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            rotateY += rotationStep;
        } else if (key == KeyEvent.VK_D) {
            rotateY -= rotationStep;
        } else if (key == KeyEvent.VK_S) {
            rotateX -= rotationStep;
        } else if (key == KeyEvent.VK_W) {
            rotateX += rotationStep;
        } else if (key == KeyEvent.VK_E) {
            rotateZ -= rotationStep;
        } else if (key == KeyEvent.VK_Q) {
            rotateZ += rotationStep;
        } else if (key == KeyEvent.VK_F) {
            scale -= scaleStep;
        } else if (key == KeyEvent.VK_C) {
            scale += scaleStep;
        } else if (key == KeyEvent.VK_J) {
            translateX += translationStep;
        } else if (key == KeyEvent.VK_L) {
            translateX -= translationStep;
        } else if (key == KeyEvent.VK_K) {
            translateY += translationStep;
        } else if (key == KeyEvent.VK_I) {
            translateY -= translationStep;
        } else if (key == KeyEvent.VK_U) {
            translateZ -= translationStep;
        } else if (key == KeyEvent.VK_O) {
            translateZ += translationStep;
        } else if (key == KeyEvent.VK_R) {
            setDefault();
        } else if (key == KeyEvent.VK_ESCAPE) {
            JFrame parent = (JFrame) getTopLevelAncestor();
            parent.dispose();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
