package objects3D;

import static org.lwjgl.opengl.GL11.*;
import GraphicsObjects.Utils;

public class ParticleAccelerator {
    private Cylinder cylinder;
    private Sphere sphere;
    
    private static final float[] METAL_DARK = {0.15f, 0.15f, 0.2f, 1.0f};
    private static final float[] METAL_LIGHT = {0.5f, 0.5f, 0.55f, 1.0f};
    private static final float[] ENERGY_BEAM = {0.0f, 0.4f, 0.5f, 0.4f};
    private static final float[] WARNING_RED = {0.6f, 0.1f, 0.0f, 1.0f};
    
    public ParticleAccelerator() {
        cylinder = new Cylinder();
        sphere = new Sphere();
    }
    
    public void drawAccelerator(float time) {
        glPushMatrix();
        {
            // 基座结构
            glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(METAL_DARK));
            for(int i = 0; i < 4; i++) {
                glPushMatrix();
                {
                    glRotatef(90 * i, 0.0f, 0.0f, 1.0f);
                    glTranslatef(1.0f, 0.0f, 0.0f);
                    glRotatef(30, 0.0f, 1.0f, 0.0f);
                    cylinder.drawCylinder(0.1f, 0.5f, 16);
                }
                glPopMatrix();
            }
            
            // 主环结构
            glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(METAL_LIGHT));
            glPushMatrix();
            {
                glTranslatef(0.0f, 0.0f, 0.5f);
                
                // 外环框架
                for(int i = 0; i < 36; i++) {
                    glPushMatrix();
                    {
                        glRotatef(10 * i, 0.0f, 0.0f, 1.0f);
                        // 环节
                        glPushMatrix();
                        {
                            glTranslatef(1.2f, 0.0f, 0.0f);
                            glRotatef(90, 0.0f, 1.0f, 0.0f);
                            cylinder.drawCylinder(0.15f, 0.2f, 16);
                            
                            // 冷却装置
                            glPushMatrix();
                            {
                                glTranslatef(0.0f, 0.0f, 0.1f);
                                for(int j = 0; j < 3; j++) {
                                    glPushMatrix();
                                    {
                                        glRotatef(120 * j, 1.0f, 0.0f, 0.0f);
                                        glTranslatef(0.0f, 0.2f, 0.0f);
                                        cylinder.drawCylinder(0.03f, 0.15f, 8);
                                    }
                                    glPopMatrix();
                                }
                            }
                            glPopMatrix();
                        }
                        glPopMatrix();
                        
                        // 能量注入点
                        if(i % 9 == 0) {
                            glPushMatrix();
                            {
                                glTranslatef(1.35f, 0.0f, 0.0f);
                                glRotatef(90, 0.0f, 1.0f, 0.0f);
                                
                                // 注入器
                                cylinder.drawCylinder(0.08f, 0.4f, 16);
                                
                                // 警示灯
                                float warning = (float)Math.sin(time * 0.7f + i) * 0.3f + 0.7f;
                                glMaterial(GL_FRONT, GL_EMISSION, Utils.ConvertForGL(new float[]{
                                    WARNING_RED[0] * warning,
                                    WARNING_RED[1] * warning,
                                    WARNING_RED[2] * warning,
                                    WARNING_RED[3]
                                }));
                                
                                glPushMatrix();
                                {
                                    glTranslatef(0.0f, 0.2f, 0.2f);
                                    sphere.drawSphere(0.05f, 16, 16);
                                }
                                glPopMatrix();
                            }
                            glPopMatrix();
                        }
                    }
                    glPopMatrix();
                }
                
                // 粒子束效果
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
                float beamIntensity = (float)Math.sin(time * 0.9f) * 0.2f + 0.8f;
                glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(new float[]{
                    ENERGY_BEAM[0],
                    ENERGY_BEAM[1],
                    ENERGY_BEAM[2],
                    ENERGY_BEAM[3] * beamIntensity
                }));
                
                // 环形光束
                glPushMatrix();
                {
                    glRotatef(time * 360 % 360, 0.0f, 0.0f, 1.0f);
                    for(int i = 0; i < 360; i += 5) {
                        glPushMatrix();
                        {
                            glRotatef(i, 0.0f, 0.0f, 1.0f);
                            glTranslatef(1.2f, 0.0f, 0.0f);
                            sphere.drawSphere(0.05f * beamIntensity, 8, 8);
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
} 