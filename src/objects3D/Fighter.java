package objects3D;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import org.newdawn.slick.opengl.Texture;

public class Fighter {
    private Cylinder cylinder;
    private TexSphere sphere;
    private static final float[] METAL_DARK = {0.3f, 0.3f, 0.3f, 1.0f};
    private static final float[] ENGINE_GLOW = {0.0f, 0.6f, 1.0f, 1.0f};
    private Texture texture;
    
    public Fighter(Texture shipTexture) {
        cylinder = new Cylinder();
        sphere = new TexSphere();
        texture = shipTexture;
    }
    
    public void drawFighter(float time) {
        glPushMatrix();
        {
            // 机身
            glPushMatrix();
            {
                setMetallicMaterial(METAL_DARK, 100.0f);
                glRotatef(90, 0.0f, 1.0f, 0.0f);
                cylinder.drawCylinder(0.1f, 0.8f, 16);
                
                // 驾驶舱
                glPushMatrix();
                {
                    glTranslatef(0.0f, 0.1f, 0.2f);
                    glScalef(0.15f, 0.15f, 0.3f);
                    texture.bind();
                    glEnable(GL_TEXTURE_2D);
                    sphere.DrawTexSphere(1.0f, 16, 16, texture);
                    glDisable(GL_TEXTURE_2D);
                }
                glPopMatrix();
                
                // 机翼
                glPushMatrix();
                {
                    glTranslatef(0.0f, 0.0f, 0.3f);
                    glScalef(1.0f, 0.05f, 0.4f);
                    drawCube();
                }
                glPopMatrix();
                
                // 引擎光效
                glPushMatrix();
                {
                    glTranslatef(0.0f, 0.0f, -0.1f);
                    float glow = (float)(Math.sin(time * 5.0) * 0.3 + 0.7);
                    setGlowMaterial(ENGINE_GLOW, glow);
                    cylinder.drawCylinder(0.08f, 0.2f, 16);
                }
                glPopMatrix();
            }
            glPopMatrix();
        }
        glPopMatrix();
    }
    
    private void drawCube() {
        glBegin(GL_QUADS);
        // 前面
        glNormal3f(0.0f, 0.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        // 后面
        glNormal3f(0.0f, 0.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        // 顶面
        glNormal3f(0.0f, 1.0f, 0.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        // 底面
        glNormal3f(0.0f, -1.0f, 0.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        // 右面
        glNormal3f(1.0f, 0.0f, 0.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        // 左面
        glNormal3f(-1.0f, 0.0f, 0.0f);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glEnd();
    }
    
    private void setMetallicMaterial(float[] color, float shininess) {
        FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
        ambient.put(new float[]{color[0]*0.4f, color[1]*0.4f, color[2]*0.4f, color[3]}).flip();
        
        FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
        diffuse.put(color).flip();
        
        FloatBuffer specular = BufferUtils.createFloatBuffer(4);
        specular.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f}).flip();
        
        glMaterial(GL_FRONT, GL_AMBIENT, ambient);
        glMaterial(GL_FRONT, GL_DIFFUSE, diffuse);
        glMaterial(GL_FRONT, GL_SPECULAR, specular);
        glMaterialf(GL_FRONT, GL_SHININESS, shininess);
    }
    
    private void setGlowMaterial(float[] color, float intensity) {
        FloatBuffer emission = BufferUtils.createFloatBuffer(4);
        emission.put(new float[]{
            color[0] * intensity,
            color[1] * intensity,
            color[2] * intensity,
            color[3]
        }).flip();
        glMaterial(GL_FRONT, GL_EMISSION, emission);
    }
}