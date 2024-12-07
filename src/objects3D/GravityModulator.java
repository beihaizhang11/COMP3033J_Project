package objects3D;

import static org.lwjgl.opengl.GL11.*;
import GraphicsObjects.Utils;

public class GravityModulator {
    private Cylinder cylinder;
    private Sphere sphere;
    
    private static final float[] DEVICE_METAL = {0.4f, 0.4f, 0.5f, 1.0f};
    private static final float[] GRAVITY_FIELD = {0.2f, 0.0f, 0.4f, 0.2f};
    private static final float[] INDICATOR_LIGHT = {0.6f, 0.3f, 0.0f, 1.0f};
    
    public GravityModulator() {
        cylinder = new Cylinder();
        sphere = new Sphere();
    }
    
    public void drawModulator(float time) {
        glPushMatrix();
        {
            glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(DEVICE_METAL));
            
            // 基座
            cylinder.drawCylinder(0.8f, 0.2f, 32);
            
            // 主体结构
            glPushMatrix();
            {
                glTranslatef(0.0f, 0.0f, 0.2f);
                
                // 外环
                for(int i = 0; i < 3; i++) {
                    glPushMatrix();
                    {
                        glRotatef(60 * i, 0.0f, 0.0f, 1.0f);
                        // 环形支架
                        glPushMatrix();
                        {
                            glRotatef(90, 1.0f, 0.0f, 0.0f);
                            cylinder.drawCylinder(0.7f, 0.1f, 32);
                        }
                        glPopMatrix();
                        
                        // 状态指示器
                        for(int j = 0; j < 6; j++) {
                            glPushMatrix();
                            {
                                glRotatef(60 * j, 0.0f, 0.0f, 1.0f);
                                glTranslatef(0.7f, 0.0f, 0.0f);
                                
                                float pulse = (float)(Math.sin(time * 0.8f + i + j) * 0.3f + 0.7f);
                                glMaterial(GL_FRONT, GL_EMISSION, Utils.ConvertForGL(new float[]{
                                    INDICATOR_LIGHT[0] * pulse,
                                    INDICATOR_LIGHT[1] * pulse,
                                    INDICATOR_LIGHT[2] * pulse,
                                    INDICATOR_LIGHT[3]
                                }));
                                
                                sphere.drawSphere(0.05f, 16, 16);
                            }
                            glPopMatrix();
                        }
                    }
                    glPopMatrix();
                }
                
                // 引力场发生器
                glPushMatrix();
                {
                    glTranslatef(0.0f, 0.0f, 0.5f);
                    
                    // 发生器主体
                    cylinder.drawCylinder(0.3f, 1.0f, 32);
                    
                    // 引力场效果
                    glEnable(GL_BLEND);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    
                    float fieldIntensity = (float)(Math.sin(time * 0.5f) * 0.2f + 0.8f);
                    glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(new float[]{
                        GRAVITY_FIELD[0],
                        GRAVITY_FIELD[1],
                        GRAVITY_FIELD[2],
                        GRAVITY_FIELD[3] * fieldIntensity
                    }));
                    
                    // 扭曲的引力场
                    glPushMatrix();
                    {
                        glTranslatef(0.0f, 0.0f, 0.5f);
                        for(int i = 0; i < 8; i++) {
                            float height = 0.1f * i;
                            float radius = 0.3f + (float)Math.sin(time + i) * 0.1f;
                            glPushMatrix();
                            {
                                glTranslatef(0.0f, 0.0f, height);
                                cylinder.drawCylinder(radius, 0.05f, 32);
                            }
                            glPopMatrix();
                        }
                    }
                    glPopMatrix();
                    
                    glDisable(GL_BLEND);
                }
                glPopMatrix();
            }
            glPopMatrix();
        }
        glPopMatrix();
    }
} 