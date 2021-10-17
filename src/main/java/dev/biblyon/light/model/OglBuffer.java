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

package dev.biblyon.light.model;

import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;

public class OglBuffer extends OglObject {

    private final int target;
    private int type = GL_STATIC_DRAW;

    public OglBuffer(int target) {
        super(GL20.glGenBuffers());
        this.target = target;
    }

    public void setData(int[] data) {
        bind();
        glBufferData(target, data, type);
    }

    public void setData(float[] data) {
        bind();
        glBufferData(target, data, type);
    }

    @Override
    public void delete() {
        GL20.glDeleteBuffers(id);
    }

    @Override
    public void bind() {
        GL20.glBindBuffer(target, id);
    }

    @Override
    public void unbind() {
        GL20.glBindBuffer(target, 0);
    }
}
