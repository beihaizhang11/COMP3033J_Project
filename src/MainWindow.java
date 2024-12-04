import java.io.IOException;
import java.nio.FloatBuffer;

import objects3D.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import GraphicsObjects.Arcball;
import GraphicsObjects.Utils;

//Main windows class controls and creates the 3D virtual world , please do not change this class but edit the other classes to complete the assignment. 
// Main window is built upon the standard Helloworld LWJGL class which I have heavily modified to use as your standard openGL environment. 
// 

// Do not touch this class, I will be making a version of it for your 3rd Assignment 
public class MainWindow {

	private boolean MouseOnepressed = true;
	private boolean dragMode = false;
	private boolean BadAnimation = true;
	private boolean Earth = false;
	private float cameraAngleHorizontal = 0.0f;  // 水平旋转角度
    private float cameraAngleVertical = 30.0f;   // 垂直旋转角度(初始稍微向上)
	/** position of pointer */
	float x = 400, y = 300;
	/** angle of rotation */
	float rotation = 0;
	/** time at last frame */
	long lastFrame;
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;

	long myDelta = 0; // to use for animation
	float Alpha = 0; // to use for animation
	long StartTime; // beginAnimiation

	Arcball MyArcball = new Arcball();

	boolean DRAWGRID = false;
	boolean waitForKeyrelease = true;
	/** Mouse movement */
	int LastMouseX = -1;
	int LastMouseY = -1;

	float pullX = 0.0f; // arc ball X cord.
	float pullY = 0.0f; // arc ball Y cord.

	int OrthoNumber = 1200; // using this for screen size, making a window of 1200 x 800 so aspect ratio 3:2
							// // do not change this for assignment 3 but you can change everything for your
							// project

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

	// static GLfloat light_position[] = {0.0, 100.0, 100.0, 0.0};

	// support method to aid in converting a java float array into a Floatbuffer
	// which is faster for the opengl layer to process

	public void start() {

		StartTime = getTime();
		try {
			Display.setDisplayMode(new DisplayMode(1200, 800));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL(); // init OpenGL
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer

		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			update(delta);
			renderGL();
			Display.update();
			Display.sync(120); // cap fps to 120fps
		}

		Display.destroy();
	}

	public void update(int delta) {
		// rotate quad
		// rotation += 0.01f * delta;

		int MouseX = Mouse.getX();
		int MouseY = Mouse.getY();
		int WheelPostion = Mouse.getDWheel();

		boolean MouseButonPressed = Mouse.isButtonDown(0);

		if (MouseButonPressed && !MouseOnepressed) {
			MouseOnepressed = true;
			// System.out.println("Mouse drag mode");
			MyArcball.startBall(MouseX, MouseY, 1200, 800);
			dragMode = true;

		} else if (!MouseButonPressed) {
			// System.out.println("Mouse drag mode end ");
			MouseOnepressed = false;
			dragMode = false;
		}

		if (dragMode) {
			MyArcball.updateBall(MouseX, MouseY, 1200, 800);
		}

		if (WheelPostion > 0) {
			OrthoNumber += 10;

		}

		if (WheelPostion < 0) {
			OrthoNumber -= 10;
			if (OrthoNumber < 610) {
				OrthoNumber = 610;
			}

			// System.out.println("Orth nubmer = " + OrthoNumber);

		}

		/** rest key is R */
		if (Keyboard.isKeyDown(Keyboard.KEY_R))
			MyArcball.reset();

		/* bad animation can be turn on or off using A key) */

		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			BadAnimation = !BadAnimation;
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			x += 0.35f * delta;

		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			y += 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			y -= 0.35f * delta;

		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
			rotation += 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			Earth = !Earth;
		}
		
		if (waitForKeyrelease) // check done to see if key is released
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_G)) {

				DRAWGRID = !DRAWGRID;
				Keyboard.next();
				if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
					waitForKeyrelease = true;
				} else {
					waitForKeyrelease = false;

				}
			}
		}

		/** to check if key is released */
		if (Keyboard.isKeyDown(Keyboard.KEY_G) == false) {
			waitForKeyrelease = true;
		} else {
			waitForKeyrelease = false;

		}

		// keep quad on the screen
		if (x < 0)
			x = 0;
		if (x > 1200)
			x = 1200;
		if (y < 0)
			y = 0;
		if (y > 800)
			y = 800;

		updateFPS(); // update FPS Counter

		LastMouseX = MouseX;
		LastMouseY = MouseY;
	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @return milliseconds passed since last frame
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void initGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		changeOrth();
		MyArcball.startBall(0, 0, 1200, 800);
		glMatrixMode(GL_MODELVIEW);
		FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
		lightPos.put(10000f).put(1000f).put(1000).put(0).flip();

		FloatBuffer lightPos2 = BufferUtils.createFloatBuffer(4);
		lightPos2.put(0f).put(1000f).put(0).put(-1000f).flip();

		FloatBuffer lightPos3 = BufferUtils.createFloatBuffer(4);
		lightPos3.put(-10000f).put(1000f).put(1000).put(0).flip();

		FloatBuffer lightPos4 = BufferUtils.createFloatBuffer(4);
		lightPos4.put(1000f).put(1000f).put(1000f).put(0).flip();

		glLight(GL_LIGHT0, GL_POSITION, lightPos); // specify the
													// position
													// of the
													// light
		// glEnable(GL_LIGHT0); // switch light #0 on // I've setup specific materials
		// so in real light it will look abit strange

		glLight(GL_LIGHT1, GL_POSITION, lightPos); // specify the
													// position
													// of the
													// light
		glEnable(GL_LIGHT1); // switch light #0 on
		glLight(GL_LIGHT1, GL_DIFFUSE, Utils.ConvertForGL(spot));

		glLight(GL_LIGHT2, GL_POSITION, lightPos3); // specify
													// the
													// position
													// of the
													// light
		glEnable(GL_LIGHT2); // switch light #0 on
		glLight(GL_LIGHT2, GL_DIFFUSE, Utils.ConvertForGL(grey));

		glLight(GL_LIGHT3, GL_POSITION, lightPos4); // specify
													// the
													// position
													// of the
													// light
		glEnable(GL_LIGHT3); // switch light #0 on
		glLight(GL_LIGHT3, GL_DIFFUSE, Utils.ConvertForGL(grey));

		// 添加太阳光源
		FloatBuffer sunLight = BufferUtils.createFloatBuffer(4);
		sunLight.put(300f).put(400f).put(0f).put(1.0f).flip();

		// 设置太阳光源属性
		glLight(GL_LIGHT4, GL_POSITION, sunLight);
		glLight(GL_LIGHT4, GL_DIFFUSE, Utils.ConvertForGL(new float[]{1.0f, 0.9f, 0.6f, 1.0f}));  // 暖黄色光
		glLight(GL_LIGHT4, GL_AMBIENT, Utils.ConvertForGL(new float[]{0.2f, 0.2f, 0.2f, 1.0f}));
		glEnable(GL_LIGHT4);

		glEnable(GL_LIGHTING); // switch lighting on
		glEnable(GL_DEPTH_TEST); // make sure depth buffer is switched
									// on
		glEnable(GL_NORMALIZE); // normalize normal vectors for safety
		glEnable(GL_COLOR_MATERIAL);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // load in texture

	}

	public void changeOrth() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		// 修改正交投影参数，扩大视野范围
		float aspect = 800.0f / 1200.0f;  // 保持宽高比
		float viewSize = OrthoNumber * 2.0f;  // 扩大视野范围
		
		glOrtho(-viewSize, viewSize,           // 左右范围
				-viewSize * aspect, viewSize * aspect,  // 上下范围（考虑宽高比）
				-100000, 100000);              // 近远平面
				
		glMatrixMode(GL_MODELVIEW);
		
		FloatBuffer CurrentMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, CurrentMatrix);
		
		MyArcball.getMatrix(CurrentMatrix);
		glLoadMatrix(CurrentMatrix);
	}

	/*
	 * You can edit this method to add in your own objects / remember to load in
	 * textures in the INIT method as they take time to load
	 * 
	 */
	public void renderGL() {
		changeOrth();

		// 设置清除颜色为黑色
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		// 清除颜色缓冲区和深度缓冲区
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glColor3f(0.5f, 0.5f, 1.0f);

		myDelta = getTime() - StartTime;
		float delta = ((float) myDelta) / 10000;

		// code to aid in animation
		float theta = (float) (delta * 2 * Math.PI * 0.2);
		float shipOrbit = theta * 0.5f;
		float thetaDeg = delta * 360;
		float posn_x = (float) Math.cos(theta); // same as your circle code in your notes
		float posn_y = (float) Math.sin(theta);

		// 添加相机控制代码 (在这里插入)
		float shipX = (float) Math.cos(shipOrbit) * 3200;
		float shipY = (float) Math.sin(shipOrbit) * 2800;

		// 计算飞船运动方向的切线(后方视角)
		float tangentX = (float) Math.sin(shipOrbit);
		float tangentY = -(float) Math.cos(shipOrbit);

		// 视角旋转控制
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			cameraAngleHorizontal -= 2.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			cameraAngleHorizontal += 2.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			cameraAngleVertical += 2.0f;
			if (cameraAngleVertical > 89.0f) cameraAngleVertical = 89.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			cameraAngleVertical -= 2.0f;
			if (cameraAngleVertical < -89.0f) cameraAngleVertical = -89.0f;
		}

		// 计算相机位置
		float radius = 5000.0f; // 增加相机距离
		float horizontalRad = (float)Math.toRadians(cameraAngleHorizontal);
		float verticalRad = (float)Math.toRadians(cameraAngleVertical);

		// 计算相机位置，使其以飞船为中心
		float camX = -300 + shipX + radius * (float)(Math.cos(verticalRad) * Math.sin(horizontalRad));
		float camY = -400 + shipY + radius * (float)(Math.cos(verticalRad) * Math.cos(horizontalRad));
		float camZ = radius * (float)Math.sin(verticalRad);

		// 设置相机位置和视点
		gluLookAt(
			camX,                   // 相机位置X
			camY,                   // 相机位置Y
			camZ,                   // 相机位置Z
			300 + shipX,           // 目标点X（飞船位置）
			400 + shipY,           // 目标点Y（飞船位置）
			0,                     // 目标点Z
			0, 0, 1               // 上方向向量
		);

		/*
		 * This code draws a grid to help you view the human models movement You may
		 * change this code to move the grid around and change its starting angle as you
		 * please
		 */
		if (DRAWGRID) {
			glPushMatrix();
			Grid MyGrid = new Grid();
			glTranslatef(600, 400, 0);
			glScalef(200f, 200f, 200f);
			MyGrid.DrawGrid();
			glPopMatrix();
		}


		// 绘制星空背景
		glPushMatrix();
		{
			HollowSphere starSphere = new HollowSphere();
			// 将背景球体中心设置在场景中心点
			glTranslatef(300, 400, 0);
			// 设置足够大的半径以包含所有行星
			glScalef(12000f, 12000f, 12000f);

			// 设置纹理参数
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			Color.white.bind();
			starsTexture.bind();
			glEnable(GL_TEXTURE_2D);

			// 绘制星空球体
			starSphere.drawHollowSphere(1.0f, 32, 32, starsTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// Add the sign rendering code here
		glPushMatrix();
		{
			TexCube sign = new TexCube();
			glTranslatef(600, 400, 0);
			glScalef(100f, 50f, 1f);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			Color.white.bind();
			signTexture.bind();
			glEnable(GL_TEXTURE_2D);

			sign.DrawTexCube(1.0f, signTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		/*
		 * This code puts the earth code in which is larger than the human so it appears
		 * to change the scene
		 */
		if (Earth) {
			// Globe in the centre of the scene
			glPushMatrix();
			TexSphere MyGlobe = new TexSphere();
			// TexCube MyGlobe = new TexCube();
			glTranslatef(500, 500, 500);
			glScalef(140f, 140f, 140f);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

			Color.white.bind();
			texture.bind();
			glEnable(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

			MyGlobe.DrawTexSphere(8f, 100, 100, texture);
			//MyGlobe.DrawTexCube();
			glPopMatrix();
		}

        // 更新太阳光源位置
        FloatBuffer sunLightPosition = BufferUtils.createFloatBuffer(4);
        sunLightPosition.put(300f).put(400f).put(0f).put(1.0f).flip();
        glLight(GL_LIGHT4, GL_POSITION, sunLightPosition);

		// 在人物中心添加一个带纹理的球体
		glPushMatrix();
		{
			TexSphere centerSphere = new TexSphere();
			// 将球体放置在人物旋转的中心点
			glTranslatef(300, 400, 0);
			// 设置适当的缩放比例,使球体大小合适
			glScalef(1320f, 1320f, 1320f);

			// 设置纹理参数
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			chestTexture.bind();  // 使用太阳纹理
			glEnable(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			// 绘制球体
			centerSphere.DrawTexSphere(1.0f, 32, 32, chestTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// 水星 (直径约为地球的0.383倍)
		glPushMatrix();
		{
			TexSphere mercury = new TexSphere();
			float mercuryOrbit = theta * 2.0f;
			float mercuryX = (float) Math.cos(mercuryOrbit) * 1600;
			float mercuryY = (float) Math.sin(mercuryOrbit) * 1600;

			glTranslatef(300 + mercuryX, 400 + mercuryY, 0);
			glScalef(50f, 50f, 50f);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			mercuryTexture.bind();
			glEnable(GL_TEXTURE_2D);
			mercury.DrawTexSphere(1.0f, 32, 32, mercuryTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// 金星 (直径约为地球的0.95倍)
		glPushMatrix();
		{
			TexSphere venus = new TexSphere();
			float venusOrbit = theta * 1.5f;
			float venusX = (float) Math.cos(venusOrbit) * 2200;
			float venusY = (float) Math.sin(venusOrbit) * 2200;

			glTranslatef(300 + venusX, 400 + venusY, 0);
			glScalef(125f, 125f, 125f);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			venusTexture.bind();
			glEnable(GL_TEXTURE_2D);
			venus.DrawTexSphere(1.0f, 32, 32, venusTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// 地球和月球
		glPushMatrix();
		{
			TexSphere earth = new TexSphere();
			float earthOrbit = theta * 1.0f;
			float earthX = (float) Math.cos(earthOrbit) * 3000;
			float earthY = (float) Math.sin(earthOrbit) * 3000;

			glTranslatef(300 + earthX, 400 + earthY, 0);

			// 地球
			glPushMatrix();
			{
				glScalef(130f, 130f, 130f);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
				Color.white.bind();
				texture.bind();
				glEnable(GL_TEXTURE_2D);
				earth.DrawTexSphere(1.0f, 32, 32, texture);
				glDisable(GL_TEXTURE_2D);
			}
			glPopMatrix();

			// 月球 (直径约为地球的0.27倍)
			TexSphere moon = new TexSphere();
			float moonOrbit = theta * 2.0f;
			float moonX = (float) Math.cos(moonOrbit) * 200;
			float moonY = (float) Math.sin(moonOrbit) * 200;

			glPushMatrix();
			{
				glTranslatef(moonX, moonY, 0);
				glScalef(35f, 35f, 35f);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
				Color.white.bind();
				moonTexture.bind();
				glEnable(GL_TEXTURE_2D);
				moon.DrawTexSphere(1.0f, 32, 32, moonTexture);
				glDisable(GL_TEXTURE_2D);
			}
			glPopMatrix();
		}
		glPopMatrix();

		// 火星 (直径约为地球的0.532倍)
		glPushMatrix();
		{
			TexSphere mars = new TexSphere();
			float marsOrbit = theta * 0.8f;
			float marsX = (float) Math.cos(marsOrbit) * 3800;
			float marsY = (float) Math.sin(marsOrbit) * 3800;

			glTranslatef(300 + marsX, 400 + marsY, 0);
			glScalef(70f, 70f, 70f);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			marsTexture.bind();
			glEnable(GL_TEXTURE_2D);
			mars.DrawTexSphere(1.0f, 32, 32, marsTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// 木星 (直径约为地球的11.2倍)
		glPushMatrix();
		{
			TexSphere jupiter = new TexSphere();
			float jupiterOrbit = theta * 0.4f;
			float jupiterX = (float) Math.cos(jupiterOrbit) * 5000;
			float jupiterY = (float) Math.sin(jupiterOrbit) * 5000;

			glTranslatef(300 + jupiterX, 400 + jupiterY, 0);
			glScalef(450f, 450f, 450f);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			headTexture.bind();  // 使用木星纹理
			glEnable(GL_TEXTURE_2D);
			jupiter.DrawTexSphere(1.0f, 32, 32, headTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// 土星
		glPushMatrix();
		{
			TexSphere saturn = new TexSphere();
			SaturnRing saturnRing = new SaturnRing();
			float saturnOrbit = theta * 0.3f;
			float saturnX = (float) Math.cos(saturnOrbit) * 6000;
			float saturnY = (float) Math.sin(saturnOrbit) * 6000;

			glTranslatef(300 + saturnX, 400 + saturnY, 0);

			// 绘制土星本体
			glPushMatrix();
			{
				glScalef(120f, 120f, 120f);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
				Color.white.bind();
				saturnTexture.bind();
				glEnable(GL_TEXTURE_2D);
				saturn.DrawTexSphere(1.0f, 32, 32, saturnTexture);
				glDisable(GL_TEXTURE_2D);
			}
			glPopMatrix();

			// 绘制土星环
			glPushMatrix();
			{
				// 稍微倾斜土星环
				glRotatef(20, 1.0f, 0.0f, 0.0f);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
				Color.white.bind();
				saturnringTexture.bind();
				glEnable(GL_TEXTURE_2D);
				saturnRing.DrawRing(150f, 250f, 64, saturnringTexture);
				glDisable(GL_TEXTURE_2D);
			}
			glPopMatrix();
		}
		glPopMatrix();

		// 天王星 (直径约为地球的4倍)
		glPushMatrix();
		{
			TexSphere uranus = new TexSphere();
			float uranusOrbit = theta * 0.2f;
			float uranusX = (float) Math.cos(uranusOrbit) * 7400;
			float uranusY = (float) Math.sin(uranusOrbit) * 7400;

			glTranslatef(300 + uranusX, 400 + uranusY, 0);
			glScalef(160f, 160f, 160f);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			uranusTexture.bind();
			glEnable(GL_TEXTURE_2D);
			uranus.DrawTexSphere(1.0f, 32, 32, uranusTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// 海王星 (直径约为地球的3.9倍)
		glPushMatrix();
		{
			TexSphere neptune = new TexSphere();
			float neptuneOrbit = theta * 0.3f;
			float neptuneX = (float) Math.cos(neptuneOrbit) * 8600;
			float neptuneY = (float) Math.sin(neptuneOrbit) * 8600;

			glTranslatef(300 + neptuneX, 400 + neptuneY, 0);
			glScalef(155f, 155f, 155f);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			bodyTexture.bind();  // 使用海王星纹理
			glEnable(GL_TEXTURE_2D);
			neptune.DrawTexSphere(1.0f, 32, 32, bodyTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// 陈茜行星 (在海王星轨道外)
		glPushMatrix();
		{
			TexSphere cx = new TexSphere();
			float cxOrbit = theta * 0.15f;  // 更慢的轨道速度
			float cxX = (float) Math.cos(cxOrbit) * 9800;  // 更大的轨道半径
			float cxY = (float) Math.sin(cxOrbit) * 9800;

			glTranslatef(300 + cxX, 400 + cxY, 0);
			glScalef(180f, 180f, 180f);  // 略大于海王星

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			Color.white.bind();
			cxTexture.bind();
			glEnable(GL_TEXTURE_2D);
			cx.DrawTexSphere(1.0f, 32, 32, cxTexture);
			glDisable(GL_TEXTURE_2D);
		}
		glPopMatrix();

		// 彗星
		glPushMatrix();
		{
			float cometTime = theta * 0.3f;
			float cometX = (float) Math.cos(cometTime) * 7000;
			float cometY = (float) Math.sin(cometTime) * 4000;
			float cometZ = (float) Math.cos(cometTime * 2) * 1000;
			
			glTranslatef(300 + cometX, 400 + cometY, cometZ);
			glRotatef(cometTime * 57.3f, 0.0f, 0.0f, 1.0f);
			
			// 彗星核心
			glPushMatrix();
			{
				glScalef(40f, 40f, 40f);
				glColor3f(0.8f, 0.8f, 1.0f);
				Sphere.drawSphere(1.0f, 16, 16);
			}
			glPopMatrix();
			
			// 彗星尾
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glBegin(GL_TRIANGLE_FAN);
			glColor4f(0.6f, 0.6f, 1.0f, 0.8f);
			glVertex3f(0, 0, 0);
			glColor4f(0.6f, 0.6f, 1.0f, 0.0f);
			for(int i = 0; i <= 32; i++) {
				float angle = (float)(i * 2.0f * Math.PI / 32);
				glVertex3f((float)Math.cos(angle) * 60,
						  (float)Math.sin(angle) * 60,
						  -400);
			}
			glEnd();
			glDisable(GL_BLEND);
		}
		glPopMatrix();

		// 绘制飞船
		glPushMatrix();
		{
			SpaceShip ship = new SpaceShip(shipTexture);
            shipOrbit = theta * 0.5f;
            shipX = (float) Math.cos(shipOrbit) * 3200;
            shipY = (float) Math.sin(shipOrbit) * 2800;

			glTranslatef(300 + shipX, 400 + shipY, 0);
			glRotatef(shipOrbit * 57.3f + 90, 0.0f, 0.0f, 1.0f);
			glRotatef(90, 1.0f, 0.0f, 0.0f);                      // 修正为X轴旋转，而不是Y轴
			glRotatef(90, 0.0f, 1.0f, 0.0f);                      // 修正为X轴旋转，而不是Y轴
			glScalef(80f, 80f, 80f);

			ship.drawSpaceShip(delta);
		}
		glPopMatrix();

		// 绘制太空基地
		glPushMatrix();
		{
			SpaceStation station = new SpaceStation();
			float stationOrbit = theta * 0.02f; // 更慢的轨道速度
			float stationX = (float) Math.cos(stationOrbit) * 3800;
			float stationY = (float) Math.sin(stationOrbit) * 3800;
			
			// 将空间站移到太阳系平面上方
			glTranslatef(300 + stationX, 400 + stationY, 800);
			glRotatef(stationOrbit * 30, 0.0f, 0.0f, 1.0f);
			glRotatef(45, 1.0f, 0.0f, 0.0f); // 倾斜45度
			glScalef(200f, 200f, 200f);
			
			station.drawSpaceStation(getTime());
		}
		glPopMatrix();

		// 绘制战斗机编队
/*		for(int i = 0; i < 5; i++) {
			glPushMatrix();
			{
				Fighter fighter = new Fighter(shipTexture);
				float fighterOrbit = theta * 0.8f + i * ((float)(2.0f * Math.PI) / 5.0f);
				float Fradius = 3500.0f;
				float fighterX = (float) Math.cos(fighterOrbit) * Fradius;
				float fighterY = (float) Math.sin(fighterOrbit) * Fradius;

				// 在太阳系平面上方绕空间站飞行
				glTranslatef(300 + fighterX, 400 + fighterY, 400);
				// 使战机朝向运动方向
				glRotatef((float)Math.toDegrees(fighterOrbit) + 90, 0.0f, 0.0f, 1.0f);
				glRotatef(90, 1.0f, 0.0f, 0.0f);
				// 让战机稍微倾斜,增加动感
				glRotatef(20, 0.0f, 1.0f, 0.0f);
				glScalef(50f, 50f, 50f);

				fighter.drawFighter(getTime() + i * 0.5f);
			}
			glPopMatrix();
		}*/

	}

	public static void main(String[] argv) {
		MainWindow hello = new MainWindow();
		
		hello.start();
	}

	Texture texture;
	Texture signTexture;  // Add sign texture variable
	Texture headTexture;      // Add head texture 
	Texture bodyTexture;      // Add body texture
	Texture chestTexture;     // Add chest texture
	Texture marsTexture;
	Texture mercuryTexture;
	Texture moonTexture;
	Texture saturnTexture;
	Texture saturnringTexture;
	Texture starsTexture;
	Texture uranusTexture;
	Texture venusTexture;
	Texture ringTexture;
	Texture shipTexture;
	Texture cxTexture;


	/*
	 * Any additional textures for your assignment should be written in here. Make a
	 * new texture variable for each one so they can be loaded in at the beginning
	 */
	public void init() throws IOException {

		texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/earthspace.png"));
		signTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/sign.png"));
		
		// 加载人物纹理
		headTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_jupiter.jpg"));
		bodyTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_neptune.jpg"));
		chestTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_sun.jpg"));
		marsTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_mars.jpg"));
		mercuryTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_mercury.jpg"));
		moonTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_moon.jpg"));
		saturnTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_saturn.jpg"));
		saturnringTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_saturn_ring_alpha.png"));
		starsTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/8k_stars_milky_way.jpg"));
		uranusTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_uranus.jpg"));
		venusTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/2k_venus_surface.jpg"));
		ringTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/2k_saturn_ring_alpha.png"));
		shipTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/enterprise.png"));
		cxTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/ucd.png"));
		
		System.out.println("Textures loaded okay ");
	}
}
