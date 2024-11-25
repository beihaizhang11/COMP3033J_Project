package objects3D;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class SaturnRing {
    
    public SaturnRing() {
    }
    
    public void DrawRing(float innerRadius, float outerRadius, int segments, Texture ringTexture) {
        float x, y, z;
        float s, t; // 纹理坐标
        
        float angleStep = (float) ((2.0f * Math.PI) / segments);
        
        glBegin(GL_QUAD_STRIP);
        for (float angle = 0; angle <= 2.0f * Math.PI; angle += angleStep) {
            // 内圈顶点
            x = (float) (Math.cos(angle) * innerRadius);
            y = (float) (Math.sin(angle) * innerRadius);
            z = 0.0f;
            
            s = angle / (float)(2.0f * Math.PI);
            t = 0.0f;
            
            glTexCoord2f(s, t);
            glNormal3f(0.0f, 0.0f, 1.0f);
            glVertex3f(x, y, z);
            
            // 外圈顶点
            x = (float) (Math.cos(angle) * outerRadius);
            y = (float) (Math.sin(angle) * outerRadius);
            z = 0.0f;
            
            t = 1.0f;
            
            glTexCoord2f(s, t);
            glNormal3f(0.0f, 0.0f, 1.0f);
            glVertex3f(x, y, z);
        }
        
        // 闭合环
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(innerRadius, 0.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(outerRadius, 0.0f, 0.0f);
        
        glEnd();
    }
} 