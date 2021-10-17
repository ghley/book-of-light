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

package dev.biblyon.light.ogl;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

public class OglElementArrayBuffer extends OglBuffer {

    public OglElementArrayBuffer() {
        super(GL_ELEMENT_ARRAY_BUFFER);
    }

    @Override
    public void setData(float[] data) {
        throw new RuntimeException("EBOs are supposed to be int only");
    }

    @Override
    public void setSubData(int offset, float[] subData) {
        throw new RuntimeException("EBOs are supposed to be int only");
    }
}
