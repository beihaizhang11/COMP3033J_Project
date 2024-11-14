package objects3D;

import org.lwjgl.opengl.GL11;
import GraphicsObjects.Point4f;
import GraphicsObjects.Vector4f;
import java.math.*;

public class Cylinder {
	// Method to draw a Cylinder using OpenGL
	public void drawCylinder(float radius, float height, int slices) {
		// Define the top and bottom vertices of the Cylinder
		Point4f[] bottomVertices = new Point4f[slices];
		Point4f[] topVertices = new Point4f[slices];

		// Calculate the positions of the top and bottom vertices
		for (int i = 0; i < slices; i++) {
			double angle = 2 * Math.PI * i / slices;
			float x = (float) Math.cos(angle) * radius;
			float y = (float) Math.sin(angle) * radius;
			bottomVertices[i] = new Point4f(x, y, 0, 1.0f);
			topVertices[i] = new Point4f(x, y, height, 1.0f);
		}

		// Start drawing the side faces of the Cylinder
		GL11.glBegin(GL11.GL_QUAD_STRIP);
		for (int i = 0; i <= slices; i++) {
			int index = i % slices;
			Vector4f normal = bottomVertices[index].MinusPoint(new Point4f(0, 0, 0, 1.0f)).Normal();
			GL11.glNormal3f(normal.x, normal.y, normal.z);

			Vector4f topVector = new Vector4f(topVertices[index].x, topVertices[index].y, topVertices[index].z, 1.0f);
			Vector4f bottomVector = new Vector4f(bottomVertices[index].x, bottomVertices[index].y, bottomVertices[index].z, 1.0f);
			GL11.glVertex3f(topVector.x, topVector.y, topVector.z);
			GL11.glVertex3f(bottomVector.x, bottomVector.y, bottomVector.z);
		}
		GL11.glEnd(); // End drawing side faces

		// Draw the top and bottom circles of the Cylinder
		drawCircle(bottomVertices, new Point4f(0, 0, 0, 1.0f));
		drawCircle(topVertices, new Point4f(0, 0, height, 1.0f));
	}

	// Helper method to draw a circle given an array of vertices and a center point
	private void drawCircle(Point4f[] vertices, Point4f center) {
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		Vector4f centerVector = new Vector4f(center.x, center.y, center.z, 1.0f);
		GL11.glVertex3f(centerVector.x, centerVector.y, centerVector.z);
		for (Point4f vertex : vertices) {
			Vector4f tempVector = new Vector4f(vertex.x, vertex.y, vertex.z, 1.0f);
			GL11.glVertex3f(tempVector.x, tempVector.y, tempVector.z);
		}
		Vector4f firstVertex = new Vector4f(vertices[0].x, vertices[0].y, vertices[0].z, 1.0f);
		GL11.glVertex3f(firstVertex.x, firstVertex.y, firstVertex.z);
		GL11.glEnd(); // End drawing the circle
	}
}