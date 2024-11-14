package objects3D;

import static org.lwjgl.opengl.GL11.*;
import GraphicsObjects.Point4f;
import GraphicsObjects.Utils;
import GraphicsObjects.Vector4f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Human {

	// basic colours
	static float black[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	static float white[] = { 1.0f, 1.0f, 1.0f, 1.0f };

	static float grey[] = { 0.5f, 0.5f, 0.5f, 1.0f };
	static float spot[] = { 0.1f, 0.1f, 0.1f, 0.5f };

	// primary colours
	static float red[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	static float green[] = { 0.0f, 1.0f, 0.0f, 1.0f };
	static float blue[] = { 0.0f, 0.0f, 1.0f, 1.0f };

	// secondary colours
	static float yellow[] = { 1.0f, 1.0f, 0.0f, 1.0f };
	static float magenta[] = { 1.0f, 0.0f, 1.0f, 1.0f };
	static float cyan[] = { 0.0f, 1.0f, 1.0f, 1.0f };

	// other colours
	static float orange[] = { 1.0f, 0.5f, 0.0f, 1.0f, 1.0f };
	static float brown[] = { 0.5f, 0.25f, 0.0f, 1.0f, 1.0f };
	static float dkgreen[] = { 0.0f, 0.5f, 0.0f, 1.0f, 1.0f };
	static float pink[] = { 1.0f, 0.6f, 0.6f, 1.0f, 1.0f };

	/// Add texture variable
	private Texture headTexture;
	private Texture bodyTexture;
	private Texture chestTexture;

	// Modify the constructor
	public Human(Texture headTex, Texture bodyTex, Texture chestTex) {
		this.headTexture = headTex;
		this.bodyTexture = bodyTex;
		this.chestTexture = chestTex;
	}

	/// Retain the original no-argument constructor
	public Human() {

	}

	// Implement using notes in Animation lecture
	public void drawHuman(float delta, boolean GoodAnimation) {
		float theta = (float) (delta * 2 * Math.PI);
		float LimbRotation;
		float RightLegRotation;
		float LeftKneeRotation;   // Control the left and right knees separately
		float RightKneeRotation;  // Control the left and right knees separately
		
		if (GoodAnimation) {
			RightLegRotation = (float) Math.cos(theta) * 45;
			LimbRotation = (float) Math.cos(theta + Math.PI) * 45;

			// Bend the knee only when the leg swings backward (when the angle is positive)
			LeftKneeRotation = (float) Math.max(0, -Math.cos(theta + Math.PI)) * -60;
			RightKneeRotation = (float) Math.max(0, -Math.cos(theta)) * -60;
		} else {
			LimbRotation = 0;
			RightLegRotation = 0;
			LeftKneeRotation = 0;
			RightKneeRotation = 0;
		}

		Sphere sphere = new Sphere();
		Cylinder cylinder = new Cylinder();
		TexSphere texSphere = new TexSphere();

		glPushMatrix();

		{
			glTranslatef(0.0f, 0.5f, 0.0f);

			// Lower torso (using texture)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			bodyTexture.bind();
			glEnable(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			texSphere.DrawTexSphere(0.5f, 32, 32, bodyTexture);
			glDisable(GL_TEXTURE_2D);

			// chest
			glPushMatrix();
			{
				glTranslatef(0.0f, 0.5f, 0.0f);

				// Upper torso (using texture)
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
				Color.white.bind();
				chestTexture.bind();
				glEnable(GL_TEXTURE_2D);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				texSphere.DrawTexSphere(0.5f, 32, 32, chestTexture);
				glDisable(GL_TEXTURE_2D);

				// neck
				glColor3f(orange[0], orange[1], orange[2]);
				glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
				glPushMatrix();
				{
					glTranslatef(0.0f, 0.0f, 0.0f);
					glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					cylinder.drawCylinder(0.15f, 0.7f, 32);

					// head
					glPushMatrix();
					{
						glTranslatef(0.0f, 0.0f, 1.0f);
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
						Color.white.bind();
						headTexture.bind();
						glEnable(GL_TEXTURE_2D);
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
						texSphere.DrawTexSphere(0.5f, 32, 32, headTexture);
						glDisable(GL_TEXTURE_2D);
					}
					glPopMatrix();
				}
				glPopMatrix();
				
				// neck
				glColor3f(orange[0], orange[1], orange[2]);
				glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
				glPushMatrix();
				{
					glTranslatef(0.0f, 0.0f, 0.0f);
					glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
					// glRotatef(45.0f,0.0f,1.0f,0.0f);
					cylinder.drawCylinder(0.15f, 0.7f, 32);

					// head
					glColor3f(red[0], red[1], red[2]);
					glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(red));
					glPushMatrix();
					{
						glTranslatef(0.0f, 0.0f, 1.0f);
						sphere.drawSphere(0.5f, 32, 32);
						glPopMatrix();
					}
					glPopMatrix();

					// left shoulder
					glColor3f(blue[0], blue[1], blue[2]);
					glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
					glPushMatrix();
					{
						glTranslatef(0.5f, 0.4f, 0.0f);
						sphere.drawSphere(0.15f, 32, 32);

						// left arm
						glColor3f(orange[0], orange[1], orange[2]);
						glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
						glPushMatrix();
						{
							glTranslatef(0.0f, 0.0f, 0.0f);
							glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

							glRotatef(LimbRotation, 1.0f, 0.0f, 0.0f);
							// glRotatef(27.5f,0.0f,1.0f,0.0f);
							cylinder.drawCylinder(0.15f, 0.7f, 32);

							// left elbow
							glColor3f(blue[0], blue[1], blue[2]);
							glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
							glPushMatrix();
							{
								glTranslatef(0.0f, 0.0f, 0.75f);
								sphere.drawSphere(0.12f, 32, 32);

								// left forearm
								glColor3f(orange[0], orange[1], orange[2]);
								glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
								glPushMatrix();
								{
									glTranslatef(0.0f, 0.0f, 0.0f);
									glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
									// glRotatef(90.0f,0.0f,1.0f,0.0f);
									cylinder.drawCylinder(0.1f, 0.7f, 32);

									// left hand
									glColor3f(blue[0], blue[1], blue[2]);
									glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
									glPushMatrix();
									{
										glTranslatef(0.0f, 0.0f, 0.75f);
										sphere.drawSphere(0.15f, 32, 32);

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
					// to chest

					// right shoulder
					glColor3f(blue[0], blue[1], blue[2]);
					glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
					glPushMatrix();
					{
						glTranslatef(-0.5f, 0.4f, 0.0f);
						sphere.drawSphere(0.15f, 32, 32);

						// right arm
						glColor3f(orange[0], orange[1], orange[2]);
						glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
						glPushMatrix();
						{
							glTranslatef(0.0f, 0.0f, 0.0f);
							glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
							glRotatef(-LimbRotation, 1.0f, 0.0f, 0.0f);
							cylinder.drawCylinder(0.15f, 0.7f, 32);

							// right elbow
							glColor3f(blue[0], blue[1], blue[2]);
							glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
							glPushMatrix();
							{
								glTranslatef(0.0f, 0.0f, 0.75f);
								sphere.drawSphere(0.12f, 32, 32);

								// right forearm
								glColor3f(orange[0], orange[1], orange[2]);
								glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
								glPushMatrix();
								{
									glTranslatef(0.0f, 0.0f, 0.0f);
									glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
									cylinder.drawCylinder(0.1f, 0.7f, 32);

									// right hand
									glColor3f(blue[0], blue[1], blue[2]);
									glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
									glPushMatrix();
									{
										glTranslatef(0.0f, 0.0f, 0.75f);
										sphere.drawSphere(0.15f, 32, 32);

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

					// right hip
					glColor3f(blue[0], blue[1], blue[2]);
					glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
					glPushMatrix();
					{
						glTranslatef(0.5f, -0.75f, 0.0f);
						sphere.drawSphere(0.20f, 32, 32);

						// right high leg
						glColor3f(orange[0], orange[1], orange[2]);
						glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
						glPushMatrix();
						{
							glTranslatef(0.0f, 0.0f, 0.0f);
							glRotatef(RightLegRotation + 90, 1.0f, 0.0f, 0.0f);
							cylinder.drawCylinder(0.15f, 0.7f, 32);

							// right knee
							glColor3f(blue[0], blue[1], blue[2]);
							glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
							glPushMatrix();
							{
								glTranslatef(0.0f, 0.0f, 0.75f);
								sphere.drawSphere(0.15f, 32, 32);

								// right low leg
								glColor3f(orange[0], orange[1], orange[2]);
								glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
								glPushMatrix();
								{
									glTranslatef(0.0f, 0.0f, 0.0f);
									glRotatef(RightKneeRotation, 1.0f, 0.0f, 0.0f);  // 使用右膝盖角度
									cylinder.drawCylinder(0.15f, 0.7f, 32);

									// right foot
									glColor3f(blue[0], blue[1], blue[2]);
									glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
									glPushMatrix();
									{
										glTranslatef(0.0f, 0.0f, 0.75f);
										sphere.drawSphere(0.18f, 32, 32);

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

				// left hip
				glColor3f(blue[0], blue[1], blue[2]);
				glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
				glPushMatrix();
				{
					glTranslatef(-0.5f, -0.2f, 0.0f);

					sphere.drawSphere(0.20f, 32, 32);

					// left high leg
					glColor3f(orange[0], orange[1], orange[2]);
					glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
					glPushMatrix();
					{
						glTranslatef(0.0f, 0.0f, 0.0f);
						glRotatef(LimbRotation + 90, 1.0f, 0.0f, 0.0f);
						cylinder.drawCylinder(0.15f, 0.7f, 32);

						// left knee
						glColor3f(blue[0], blue[1], blue[2]);
						glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
						glPushMatrix();
						{
							glTranslatef(0.0f, 0.0f, 0.75f);
							glRotatef(LeftKneeRotation, 1.0f, 0.0f, 0.0f);  // 使用左膝盖角度
							sphere.drawSphere(0.15f, 32, 32);

							// left low leg
							glColor3f(orange[0], orange[1], orange[2]);
							glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(orange));
							glPushMatrix();
							{
								glTranslatef(0.0f, 0.0f, 0.0f);
								cylinder.drawCylinder(0.15f, 0.7f, 32);

								// left foot
								glColor3f(blue[0], blue[1], blue[2]);
								glMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, Utils.ConvertForGL(blue));
								glPushMatrix();
								{
									glTranslatef(0.0f, 0.0f, 0.75f);
									sphere.drawSphere(0.18f, 32, 32);

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

}

/*
 *
 *
 * }
 *
 */

