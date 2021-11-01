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

package dev.biblyon.light.ogl.util;

import dev.biblyon.light.ogl.OglProgram;
import dev.biblyon.light.ogl.ProgramBuilder;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL30.*;

public enum RectToCubeMap {
    ;


    public static int process(String hdrFile) {
        int dim = 512;

        int rectTexture = TextureUtil.loadHDR("resources/test.hdr");
        OglProgram program = new ProgramBuilder().program("resources/shaders/equiMap.program").build();

        int fb = glGenFramebuffers();
        int rb = glGenRenderbuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fb);
        glBindRenderbuffer(GL_RENDERBUFFER, rb);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, dim,dim); // TODO adjust this
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rb);

        int cubeMapTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubeMapTexture);
        for (int q = 0; q < 6; q++) {
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + q, 0, GL_RGB16F, dim, dim,
                    0, GL_RGB, GL_FLOAT, 0);
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(90f), 1.0f, 0.1f, 10.f);
        Vector3f zero = new Vector3f(0);
        Vector3f px = new Vector3f(1,0,0);
        Vector3f mx = new Vector3f(-1,0,0);
        Vector3f py = new Vector3f(0,1,0);
        Vector3f my = new Vector3f(0,-1,0); // up? not sure this is correct
        Vector3f pz = new Vector3f(0,0,1);
        Vector3f mz = new Vector3f(0,0,-1);
        Matrix4f[] views = new Matrix4f[] {
                new Matrix4f().lookAt(zero, px, my),
                new Matrix4f().lookAt(zero, mx, my),
                new Matrix4f().lookAt(zero, py, pz),
                new Matrix4f().lookAt(zero, my, mz),
                new Matrix4f().lookAt(zero, pz, my),
                new Matrix4f().lookAt(zero, mz, my),
        };

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, rectTexture);

        program.bind();
        program.set("projection", projection);
        program.set("rectMap", 0);

        glViewport(0, 0, dim, dim);
        glBindFramebuffer(GL_FRAMEBUFFER, fb);

        for (int q = 0; q < 6; q++) {
            program.set("view", views[q]);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_CUBE_MAP_POSITIVE_X+q, cubeMapTexture, 0);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glBindTexture(GL_TEXTURE_CUBE_MAP, cubeMapTexture);
        glGenerateMipmap(GL_TEXTURE_CUBE_MAP);

        program.delete(); // yes we can just cache this, will do later (or never) TODO
        glDeleteTextures(rectTexture);
        glDeleteFramebuffers(fb);
        glDeleteRenderbuffers(rb);
        return cubeMapTexture;
    }

}
