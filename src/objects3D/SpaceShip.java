package objects3D;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import GraphicsObjects.Utils;

public class SpaceShip {
    static float grey[] = { 0.8f, 0.8f, 0.8f, 1.0f };
    static float brown[] = { 0.5f, 0.25f, 0.0f, 1.0f };
    static float silver[] = { 0.9f, 0.9f, 0.9f, 1.0f };
    private Texture shipTexture;
    
    public SpaceShip(Texture shipTex) {
        this.shipTexture = shipTex;
    }
    
    public void drawSpaceShip(float delta) {
        Sphere sphere = new Sphere();
        Cylinder cylinder = new Cylinder();
        TexSphere texSphere = new TexSphere();
        
        glPushMatrix();
        {
            // 主圆盘部分(使用纹理)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
            Color.white.bind();
            shipTexture.bind();
            glEnable(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glPushMatrix();
            {
                glScalef(2.0f, 0.3f, 2.0f);
                texSphere.DrawTexSphere(1.0f, 32, 32, shipTexture);
            }
            glPopMatrix();
            glDisable(GL_TEXTURE_2D);

            // 舰桥部分
            glPushMatrix();
            {
                glTranslatef(0.0f, 0.3f, 0.0f);
                glColor3f(silver[0], silver[1], silver[2]);
                glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(silver));
                sphere.drawSphere(0.4f, 32, 32);
            }
            glPopMatrix();
            
            // 主引擎和连接部分
            glPushMatrix();
            {
                // 整体旋转180度
                glRotatef(180, 0.0f, 1.0f, 0.0f);
                
                // 连接颈部(倾斜45度的支架)
                glTranslatef(0.0f, -0.2f, 1.0f);
                glRotatef(45, 1.0f, 0.0f, 0.0f);
                glColor3f(grey[0], grey[1], grey[2]);
                glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(grey));
                cylinder.drawCylinder(0.2f, 1.0f, 32);
                
                // 主引擎
                glPushMatrix();
                {
                    glTranslatef(0.0f, -0.7f, 0.7f);
                    glRotatef(-45, 1.0f, 0.0f, 0.0f);  // 调整回水平
                    glColor3f(brown[0], brown[1], brown[2]);
                    glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(brown));
                    cylinder.drawCylinder(0.3f, 2.0f, 32);
                    
                    // 主引擎尾部
                    glPushMatrix();
                    {
                        glTranslatef(0.0f, 0.0f, 2.0f);
                        glColor3f(silver[0], silver[1], silver[2]);
                        glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(silver));
                        sphere.drawSphere(0.35f, 32, 32);
                    }
                    glPopMatrix();
                    
                    // 左副引擎支架
                    glPushMatrix();
                    {
                        glTranslatef(-0.4f, 0.0f, 1.5f);
                        glRotatef(-45, 0.0f, 1.0f, 0.0f);  // 水平面内的旋转
                        glRotatef(-45, 1.0f, 0.0f, 0.0f);   // 向上倾斜45度
                        glColor3f(grey[0], grey[1], grey[2]);
                        glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(grey));
                        cylinder.drawCylinder(0.1f, 0.8f, 32);
                        
                        // 左副引擎
                        glPushMatrix();
                        {
                            glTranslatef(0.0f, 0.0f, 0.8f);
                            glRotatef(45, 0.0f, 1.0f, 0.0f);   // 抵消水平旋转
                            glRotatef(45, 1.0f, 0.0f, 1.0f);  // 抵消垂直旋转
                            glColor3f(brown[0], brown[1], brown[2]);
                            glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(brown));
                            cylinder.drawCylinder(0.2f, 1.5f, 32);
                            
                            // 左副引擎尾部
                            glPushMatrix();
                            {
                                glTranslatef(0.0f, 0.0f, 1.5f);
                                glColor3f(silver[0], silver[1], silver[2]);
                                glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(silver));
                                sphere.drawSphere(0.25f, 32, 32);
                            }
                            glPopMatrix();
                        }
                        glPopMatrix();
                    }
                    glPopMatrix();
                    
                    // 右副引擎支架
                    glPushMatrix();
                    {
                        glTranslatef(0.4f, 0.0f, 1.5f);
                        glRotatef(45, 0.0f, 1.0f, 0.0f);   // 水平面内的旋转
                        glRotatef(-45, 1.0f, 0.0f, 0.0f);   // 向上倾斜45度
                        glColor3f(grey[0], grey[1], grey[2]);
                        glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(grey));
                        cylinder.drawCylinder(0.1f, 0.8f, 32);
                        
                        // 右副引擎
                        glPushMatrix();
                        {
                            glTranslatef(0.0f, 0.0f, 0.8f);
                            glRotatef(-45, 0.0f, 1.0f, 0.0f);  // 抵消水平旋转
                            glRotatef(45, 1.0f, 0.0f, 1.0f);  // 抵消垂直旋转
                            glColor3f(brown[0], brown[1], brown[2]);
                            glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(brown));
                            cylinder.drawCylinder(0.2f, 1.5f, 32);
                            
                            // 右副引擎尾部
                            glPushMatrix();
                            {
                                glTranslatef(0.0f, 0.0f, 1.5f);
                                glColor3f(silver[0], silver[1], silver[2]);
                                glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(silver));
                                sphere.drawSphere(0.25f, 32, 32);
                            }
                            glPopMatrix();
                        }
                        glPopMatrix();
                    }
                    glPopMatrix();
                }
                glPopMatrix();
            }
            glPopMatrix();
        }
        glPopMatrix();
    }
} 