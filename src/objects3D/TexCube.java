package objects3D;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;

public class TexCube {
	static float red[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	static float green[] = { 0.0f, 1.0f, 0.0f, 1.0f };
	static float blue[] = { 0.0f, 0.0f, 1.0f, 1.0f };
	static float white[] = { 1.0f, 1.0f, 1.0f, 1.0f };

	public TexCube() {
	}

	public void DrawTexCube(float size, Texture myTexture) {
		float width = size * 8.4f;    // Width is 8.4 units (to fit in the sign texture)
		float height = size * 2.9f;    // Height is 2.9 units
		
		Point4f vertices[] = {
				// The four vertices on the front
			new Point4f(-width/2, -height/2, -size, 0.0f),  // 0
			new Point4f(-width/2, -height/2, size, 0.0f),   // 1
			new Point4f(-width/2, height/2, -size, 0.0f),   // 2
			new Point4f(-width/2, height/2, size, 0.0f),    // 3
				// The four vertices on the back
			new Point4f(width/2, -height/2, -size, 0.0f),   // 4
			new Point4f(width/2, -height/2, size, 0.0f),    // 5
			new Point4f(width/2, height/2, -size, 0.0f),    // 6
			new Point4f(width/2, height/2, size, 0.0f)      // 7
		};

		int faces[][] = {
				{0, 4, 7, 3},  // Front
				{1, 5, 6, 2},  // Back
				{0, 1, 5, 4},  // Bottom
				{2, 3, 7, 6},  // Top
				{0, 2, 6, 4},  // Left
				{1, 3, 7, 5}   // Right
		};

		glBegin(GL_QUADS);
		for (int face = 0; face < 6; face++) {
			Vector4f v = vertices[faces[face][1]].MinusPoint(vertices[faces[face][0]]);
			Vector4f w = vertices[faces[face][3]].MinusPoint(vertices[faces[face][0]]);
			Vector4f normal = v.cross(w).Normal();
			glNormal3f(normal.x, normal.y, normal.z);

			if (face == 0) {  // Display texture only on the front
				// Texture coordinates
				glTexCoord2f(0.0f, 1.0f); glVertex3f(vertices[faces[face][0]].x, vertices[faces[face][0]].y, vertices[faces[face][0]].z);
				glTexCoord2f(1.0f, 1.0f); glVertex3f(vertices[faces[face][1]].x, vertices[faces[face][1]].y, vertices[faces[face][1]].z);
				glTexCoord2f(1.0f, 0.0f); glVertex3f(vertices[faces[face][2]].x, vertices[faces[face][2]].y, vertices[faces[face][2]].z);
				glTexCoord2f(0.0f, 0.0f); glVertex3f(vertices[faces[face][3]].x, vertices[faces[face][3]].y, vertices[faces[face][3]].z);
			} else {  // Do not use texture on other faces, only use color
				glColor3f(0.8f, 0.8f, 0.8f);  // Set to light gray
				for (int vertex = 0; vertex < 4; vertex++) {
					Point4f v1 = vertices[faces[face][vertex]];
					glVertex3f(v1.x, v1.y, v1.z);
				}
			}
		}
		glEnd();
	}
}

/*
 * 
 * 
 * }
 * 
 */
