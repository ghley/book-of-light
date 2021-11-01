/*
 * Book of Light is a OpenGL based renderer using LWJGL
 * Copyright (C) 2021 Ghley
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.
 */

package dev.biblyon.light;

import dev.biblyon.light.model.Camera;
import dev.biblyon.light.model.Skylight;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryStack.stackPush;

public class RenderView {
    private RenderViewConfig config;

    private long window = 0;
    private boolean alive = true;

    private Camera camera;


    public RenderView() {
        this(RenderViewConfig.DEFAULT);
    }

    public RenderView(RenderViewConfig config) {
        this.config = config;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW failed initialisation");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(config.getWidth(), config.getHeight(), config.getFrameTitle(), 0,0);
        if (window == 0) {
            throw new RuntimeException("Window failed initialisation");
        }

        if (config.isDisableCursor()) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }

        if (glfwRawMouseMotionSupported()) {
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        }

        centerFrame();

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        camera = new Camera();
        camera.setFov((float)Math.toRadians(80));
        camera.setRatio(config.getWidth() / (float)config.getHeight());
    }

    private void centerFrame() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window,
                    (mode.width() - pWidth.get(0)) / 2,
                    (mode.height() - pHeight.get(0)) / 2
            );
        }
    }

    public void preRender() {
        if (!alive) {
            return;
        }
        glViewport(0, 0, config.getWidth(), config.getHeight()); // TODO quick fix for framebuffers interfering
        glClearColor(0.2f,0.3f,0.4f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        alive = !glfwWindowShouldClose(window);
    }

    public void postRender() {
        if (!alive) {
            return;
        }
        glfwSwapBuffers(window);
        glfwPollEvents();
        if (glfwWindowShouldClose(window)) {
            alive = false;
            destroy();
        }
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean isAlive() {
        return alive;
    }

    public Camera getCamera() {
        return camera;
    }
}
