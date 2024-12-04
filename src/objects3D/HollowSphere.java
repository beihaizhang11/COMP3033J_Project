package objects3D;

import static org.lwjgl.opengl.GL11.*;
import GraphicsObjects.Vector4f;
import org.newdawn.slick.opengl.Texture;

public class HollowSphere {
    public void drawHollowSphere(float radius, int slices, int stacks, Texture texture) {
        // 保存当前的GL状态
        glPushAttrib(GL_ENABLE_BIT);
        
        // 启用背面剔除，但是剔除正面而不是背面
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        
        // 禁用深度写入，但保持深度测试
        glDepthMask(false);
        
        for (int i = 0; i < stacks; i++) {
            float phi1 = (float) Math.PI * (-0.5f + (float) i / stacks);
            float phi2 = (float) Math.PI * (-0.5f + (float) (i + 1) / stacks);
            
            float cosP1 = (float) Math.cos(phi1);
            float sinP1 = (float) Math.sin(phi1);
            float cosP2 = (float) Math.cos(phi2);
            float sinP2 = (float) Math.sin(phi2);
            
            glBegin(GL_QUAD_STRIP);
            for (int j = 0; j <= slices; j++) {
                float theta = (float) (2.0f * Math.PI * j / slices);
                float cosT = (float) Math.cos(theta);
                float sinT = (float) Math.sin(theta);
                
                float s = (float) j / slices;
                float t1 = (float) i / stacks;
                float t2 = (float) (i + 1) / stacks;
                
                // 第一个顶点
                float x1 = cosT * cosP1;
                float y1 = sinT * cosP1;
                float z1 = sinP1;
                glTexCoord2f(s, t1);
                glNormal3f(x1, y1, z1);  // 法线朝外
                glVertex3f(x1 * radius, y1 * radius, z1 * radius);
                
                // 第二个顶点
                float x2 = cosT * cosP2;
                float y2 = sinT * cosP2;
                float z2 = sinP2;
                glTexCoord2f(s, t2);
                glNormal3f(x2, y2, z2);  // 法线朝外
                glVertex3f(x2 * radius, y2 * radius, z2 * radius);
            }
            glEnd();
        }
        
        // 恢复深度写入
        glDepthMask(true);
        
        // 恢复之前的GL状态
        glPopAttrib();
    }
}