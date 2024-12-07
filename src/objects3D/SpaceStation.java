package objects3D;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

public class SpaceStation {
    private Cylinder cylinder;
    private TexSphere sphere;
    
    // 更新材质颜色和效果
    private static final float[] METAL_PRIMARY = {0.3f, 0.35f, 0.4f, 1.0f};      // 主体金属色
    private static final float[] METAL_ACCENT = {0.6f, 0.65f, 0.7f, 1.0f};       // 装饰金属色
    private static final float[] ENERGY_CORE = {0.1f, 0.6f, 1.0f, 0.8f};         // 能量核心
    private static final float[] SOLAR_PANEL = {0.1f, 0.2f, 0.3f, 0.9f};         // 太阳能板
    private static final float[] WARNING_LIGHT = {0.8f, 0.2f, 0.1f, 1.0f};       // 警示灯
    private static final float[] TECH_GLOW = {0.0f, 0.8f, 0.6f, 0.7f};          // 科技光芒
    
    public SpaceStation() {
        cylinder = new Cylinder();
        sphere = new TexSphere();
    }
    
    public void drawSpaceStation(float time) {
        glPushAttrib(GL_ENABLE_BIT | GL_CURRENT_BIT | GL_LIGHTING_BIT);
        
        // 中央核心筒
        glPushMatrix();
        {
            // 设置主体金属材质
            setMetallicMaterial(METAL_PRIMARY, 50.0f);
            
            // 添加金属纹理效果
            glEnable(GL_TEXTURE_GEN_S);
            glEnable(GL_TEXTURE_GEN_T);
            glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
            glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
            
            cylinder.drawCylinder(0.5f, 2.0f, 32);
            
            glDisable(GL_TEXTURE_GEN_S);
            glDisable(GL_TEXTURE_GEN_T);
            
            // 能量核心环
            for(int i = 0; i < 3; i++) {
                glPushMatrix();
                {
                    glTranslatef(0.0f, 0.0f, 0.6f * i + 0.3f);
                    
                    // 能量脉冲效果
                    float pulse = (float)(Math.sin(time * 0.8f + i * Math.PI/3) * 0.3f + 0.7f);
                    setGlowMaterial(ENERGY_CORE, pulse);
                    
                    glEnable(GL_BLEND);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    cylinder.drawCylinder(0.52f, 0.05f, 32);
                    glDisable(GL_BLEND);
                }
                glPopMatrix();
            }
            
            // 两端的连接球
            glPushMatrix();
            {
                setMetallicMaterial(METAL_ACCENT, 80.0f);
                
                // 添加环形纹理
                for(int end = 0; end < 2; end++) {
                    glPushMatrix();
                    {
                        glTranslatef(0.0f, 0.0f, end * 2.0f);
                        
                        // 主球体
                        Sphere.drawSphere(0.6f, 32, 32);
                        
                        // 装饰环
                        float ringPulse = (float)(Math.sin(time * 0.5f + end * Math.PI) * 0.2f + 0.8f);
                        setGlowMaterial(TECH_GLOW, ringPulse);
                        
                        glRotatef(90, 1.0f, 0.0f, 0.0f);
                        for(int ring = 0; ring < 3; ring++) {
                            glPushMatrix();
                            {
                                glRotatef(ring * 60, 0.0f, 1.0f, 0.0f);
                                drawRing(0.7f, 0.05f, 32);
                            }
                            glPopMatrix();
                        }
                    }
                    glPopMatrix();
                }
            }
            glPopMatrix();
            
            // 太阳能板阵列
            for(int i = 0; i < 4; i++) {
                glPushMatrix();
                {
                    glRotatef(90 * i + time * 0.2f, 0.0f, 0.0f, 1.0f);
                    glTranslatef(1.0f, 0.0f, 1.0f);
                    
                    // 支撑臂
                    setMetallicMaterial(METAL_PRIMARY, 30.0f);
                    glRotatef(90, 0.0f, 1.0f, 0.0f);
                    cylinder.drawCylinder(0.1f, 1.0f, 16);
                    
                    // 太阳能板
                    glPushMatrix();
                    {
                        glTranslatef(0.0f, 0.0f, 1.0f);
                        
                        // 面板基础材质
                        setMetallicMaterial(SOLAR_PANEL, 10.0f);
                        
                        // 添加网格纹理效果
                        glScalef(1.5f, 0.05f, 0.8f);
                        drawSolarPanel();
                        
                        // 能量流动效果
                        glEnable(GL_BLEND);
                        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                        
                        float energyPulse = (float)(Math.sin(time * 0.7f + i * Math.PI/2) * 0.3f + 0.7f);
                        setGlowMaterial(TECH_GLOW, energyPulse * 0.5f);
                        
                        glTranslatef(0.0f, 1.0f, 0.0f);
                        drawSolarPanel();
                        
                        glDisable(GL_BLEND);
                    }
                    glPopMatrix();
                }
                glPopMatrix();
            }
            
            // 通信天线
            glPushMatrix();
            {
                setMetallicMaterial(METAL_ACCENT, 70.0f);
                glTranslatef(0.0f, 0.0f, 2.2f);
                glRotatef(time * 10, 0.0f, 0.0f, 1.0f);
                
                // 天线主杆
                cylinder.drawCylinder(0.05f, 0.8f, 8);
                
                // 天线接收器
                glTranslatef(0.0f, 0.0f, 0.8f);
                glRotatef(45, 1.0f, 0.0f, 0.0f);
                
                // 接收器发光效果
                float signalPulse = (float)(Math.sin(time * 0.9f) * 0.4f + 0.6f);
                setGlowMaterial(WARNING_LIGHT, signalPulse);
                
                cylinder.drawCylinder(0.02f, 0.4f, 8);
                
                // 天线尖端
                glTranslatef(0.0f, 0.0f, 0.4f);
                Sphere.drawSphere(0.03f, 8, 8);
            }
            glPopMatrix();
        }
        glPopMatrix();
        
        glPopAttrib();
    }
    
    // 绘制太阳能板的网格纹理
    private void drawSolarPanel() {
        glBegin(GL_QUADS);
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                float x1 = -1.0f + i * 0.25f;
                float x2 = x1 + 0.24f;
                float z1 = -1.0f + j * 0.25f;
                float z2 = z1 + 0.24f;
                
                glNormal3f(0.0f, 1.0f, 0.0f);
                glVertex3f(x1, 0.0f, z1);
                glVertex3f(x2, 0.0f, z1);
                glVertex3f(x2, 0.0f, z2);
                glVertex3f(x1, 0.0f, z2);
            }
        }
        glEnd();
    }
    
    // 其他辅助方法保持不变...
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
}