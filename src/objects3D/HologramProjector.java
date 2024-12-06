package objects3D;

import static org.lwjgl.opengl.GL11.*;
import GraphicsObjects.Utils;

public class HologramProjector {
    private Cylinder cylinder;
    private Sphere sphere;
    
    private static final float[] BASE_METAL = {0.3f, 0.3f, 0.35f, 1.0f};
    private static final float[] HOLO_BLUE = {0.2f, 0.6f, 1.0f, 0.4f};
    private static final float[] LENS_GLOW = {0.0f, 0.8f, 1.0f, 1.0f};
    
    public HologramProjector() {
        cylinder = new Cylinder();
        sphere = new Sphere();
    }
    
    public void drawProjector(float time) {
        glPushMatrix();
        {
            glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(BASE_METAL));
            
            // 底座
            cylinder.drawCylinder(0.6f, 0.1f, 32);
            
            // 支柱结构
            for(int i = 0; i < 3; i++) {
                glPushMatrix();
                {
                    glRotatef(120 * i, 0.0f, 0.0f, 1.0f);
                    glTranslatef(0.4f, 0.0f, 0.0f);
                    
                    // 主支柱
                    glRotatef(15, 0.0f, 1.0f, 0.0f);
                    cylinder.drawCylinder(0.08f, 0.8f, 16);
                    
                    // 顶部连接件
                    glTranslatef(0.0f, 0.0f, 0.8f);
                    sphere.drawSphere(0.1f, 16, 16);
                    
                    // 投影镜头
                    glPushMatrix();
                    {
                        glRotatef(-30, 0.0f, 1.0f, 0.0f);
                        cylinder.drawCylinder(0.06f, 0.2f, 16);
                        
                        // 镜头发光效果
                        float lensGlow = (float)Math.sin(time * 2 + i * 2) * 0.5f + 0.5f;
                        glMaterial(GL_FRONT, GL_EMISSION, Utils.ConvertForGL(new float[]{
                            LENS_GLOW[0] * lensGlow,
                            LENS_GLOW[1] * lensGlow,
                            LENS_GLOW[2] * lensGlow,
                            LENS_GLOW[3]
                        }));
                        
                        glPushMatrix();
                        {
                            glTranslatef(0.0f, 0.0f, 0.2f);
                            sphere.drawSphere(0.06f, 16, 16);
                        }
                        glPopMatrix();
                    }
                    glPopMatrix();
                }
                glPopMatrix();
            }
            
            // 全息投影效果
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            
            float holoIntensity = (float)Math.sin(time) * 0.3f + 0.7f;
            glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(new float[]{
                HOLO_BLUE[0],
                HOLO_BLUE[1],
                HOLO_BLUE[2],
                HOLO_BLUE[3] * holoIntensity
            }));
            
            glPushMatrix();
            {
                glTranslatef(0.0f, 0.0f, 0.6f);
                
                // 多层全息效果
                for(int i = 0; i < 8; i++) {
                    float height = i * 0.1f;
                    float scale = 1.0f - (height / 0.8f);
                    float rotation = time * 100 + i * 45;
                    
                    glPushMatrix();
                    {
                        glTranslatef(0.0f, 0.0f, height);
                        glRotatef(rotation, 0.0f, 0.0f, 1.0f);
                        glScalef(scale, scale, 1.0f);
                        
                        // 绘制全息图层
                        glBegin(GL_TRIANGLE_FAN);
                        glNormal3f(0.0f, 0.0f, 1.0f);
                        glVertex3f(0.0f, 0.0f, 0.0f);
                        for(int j = 0; j <= 32; j++) {
                            float angle = (float)(j * 2.0f * Math.PI / 32);
                            float x = (float)Math.cos(angle) * 0.4f;
                            float y = (float)Math.sin(angle) * 0.4f;
                            glVertex3f(x, y, 0.0f);
                        }
                        glEnd();
                    }
                    glPopMatrix();
                }
            }
            glPopMatrix();
            
            glDisable(GL_BLEND);
        }
        glPopMatrix();
    }
} 