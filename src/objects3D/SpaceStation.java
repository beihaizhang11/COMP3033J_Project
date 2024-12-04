package objects3D;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

public class SpaceStation {
    private Cylinder cylinder;
    private TexSphere sphere;
    
    // 定义材质颜色
    private static final float[] METAL_GRAY = {0.7f, 0.7f, 0.7f, 1.0f};
    private static final float[] METAL_DARK = {0.3f, 0.3f, 0.3f, 1.0f};
    private static final float[] METAL_BLUE = {0.4f, 0.5f, 0.8f, 1.0f};
    private static final float[] SOLAR_BLUE = {0.2f, 0.4f, 1.0f, 0.8f};
    private static final float[] GLOW_RED = {1.0f, 0.2f, 0.1f, 1.0f};
    private static final float[] METAL_GOLD = {0.8f, 0.7f, 0.2f, 1.0f};
    
    public SpaceStation() {
        cylinder = new Cylinder();
        sphere = new TexSphere();
    }
    
    public void drawSpaceStation(float time) {
        glPushAttrib(GL_ENABLE_BIT | GL_CURRENT_BIT | GL_LIGHTING_BIT);
        
        // 启用混合
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        // 中央核心筒
        glPushMatrix();
        {
            // 设置金属材质
            setMetallicMaterial(METAL_GRAY, 80.0f);
            cylinder.drawCylinder(0.5f, 2.0f, 32);
            
            // 添加发光环
            setGlowMaterial(GLOW_RED, (float)(Math.sin(time) * 0.3 + 0.7));
            for(int i = 0; i < 3; i++) {
                glPushMatrix();
                glTranslatef(0.0f, 0.0f, 0.6f * i + 0.3f);
                cylinder.drawCylinder(0.52f, 0.05f, 32);
                glPopMatrix();
            }
            
            // 两端的球形连接处
            glPushMatrix();
            {
                setMetallicMaterial(METAL_BLUE, 100.0f);
                Sphere.drawSphere(0.6f, 32, 32);
                glTranslatef(0.0f, 0.0f, 2.0f);
                Sphere.drawSphere(0.6f, 32, 32);
                
                // 添加装饰环
                setMetallicMaterial(METAL_GOLD, 120.0f);
                drawRing(0.7f, 0.1f, 32);
            }
            glPopMatrix();
            
            // 太阳能板阵列
            for (int i = 0; i < 4; i++) {
                glPushMatrix();
                {
                    glRotatef(90 * i + time * 0.5f, 0.0f, 0.0f, 1.0f);
                    glTranslatef(1.0f, 0.0f, 1.0f);
                    
                    // 支撑臂
                    setMetallicMaterial(METAL_DARK, 50.0f);
                    glRotatef(90, 0.0f, 1.0f, 0.0f);
                    cylinder.drawCylinder(0.1f, 1.0f, 16);
                    
                    // 太阳能板
                    glPushMatrix();
                    {
                        setTransparentMaterial(SOLAR_BLUE);
                        glTranslatef(0.0f, 0.0f, 1.0f);
                        glScalef(1.5f, 0.05f, 0.8f);
                        drawCube();
                        
                        // 能量脉冲效果
                        float pulse = (float)(Math.sin(time * 3 + i * Math.PI/2) * 0.5 + 0.5);
                        setGlowMaterial(new float[]{0.4f, 0.6f, 1.0f, pulse}, pulse);
                        glTranslatef(0.0f, 0.1f, 0.0f);
                        drawCube();
                    }
                    glPopMatrix();
                }
                glPopMatrix();
            }
            
            // 通信天线
            glPushMatrix();
            {
                setMetallicMaterial(METAL_DARK, 50.0f);
                glTranslatef(0.0f, 0.0f, 2.2f);
                glRotatef(time * 20, 0.0f, 0.0f, 1.0f);
                cylinder.drawCylinder(0.05f, 0.8f, 8);
                
                glTranslatef(0.0f, 0.0f, 0.8f);
                glRotatef(45, 1.0f, 0.0f, 0.0f);
                cylinder.drawCylinder(0.02f, 0.4f, 8);
                
                // 添加天线尖端发光效果
                setGlowMaterial(GLOW_RED, (float)(Math.sin(time * 2) * 0.5 + 0.5));
                Sphere.drawSphere(0.03f, 8, 8);
            }
            glPopMatrix();
        }
        glPopMatrix();
        
        glDisable(GL_BLEND);
        glPopAttrib();
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
    
    private void setTransparentMaterial(float[] color) {
        FloatBuffer material = BufferUtils.createFloatBuffer(4);
        material.put(color).flip();
        glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, material);
    }
    
    private void drawRing(float radius, float thickness, int segments) {
        glBegin(GL_QUAD_STRIP);
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (i * 2.0f * Math.PI / segments);
            float x = (float) Math.cos(angle);
            float y = (float) Math.sin(angle);
            glNormal3f(x, y, 0.0f);
            glVertex3f(x * (radius - thickness), y * (radius - thickness), 0.0f);
            glVertex3f(x * radius, y * radius, 0.0f);
        }
        glEnd();
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
}