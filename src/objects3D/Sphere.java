package objects3D;

import GraphicsObjects.Vector4f;
import org.lwjgl.opengl.GL11;

public class Sphere {
	// Method to draw a Sphere using OpenGL
	public void drawSphere(float radius, int slices, int stacks) {
		// Loop through each stack to draw the Sphere
		for (int i = 0; i < stacks; i++) {
			float lat0 = (float) Math.PI * (-0.5f + (float) (i) / stacks);
			float z0 = (float) Math.sin(lat0);
			float zr0 = (float) Math.cos(lat0);

			float lat1 = (float) Math.PI * (-0.5f + (float) (i + 1) / stacks);
			float z1 = (float) Math.sin(lat1);
			float zr1 = (float) Math.cos(lat1);

			// Start drawing quads for each stack
			GL11.glBegin(GL11.GL_QUAD_STRIP);
			for (int j = 0; j <= slices; j++) {
				float lng = (float) (2 * Math.PI * (j - 1) / slices);
				float x = (float) Math.cos(lng);
				float y = (float) Math.sin(lng);

				// Set the normal and draw each vertex of the quad strip
				GL11.glNormal3f(x * zr0, y * zr0, z0);
				Vector4f tempVector0 = new Vector4f(x * zr0 * radius, y * zr0 * radius, z0 * radius, 1.0f);
				GL11.glVertex3f(tempVector0.x, tempVector0.y, tempVector0.z);

				GL11.glNormal3f(x * zr1, y * zr1, z1);
				Vector4f tempVector1 = new Vector4f(x * zr1 * radius, y * zr1 * radius, z1 * radius, 1.0f);
				GL11.glVertex3f(tempVector1.x, tempVector1.y, tempVector1.z);
			}
			GL11.glEnd(); // End drawing the quad strip
		}
	}
}
