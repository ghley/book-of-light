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

import dev.biblyon.light.ogl.util.VertexAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class OglVertexArray extends OglObject {
    private List<OglBuffer> referencedBuffers = new ArrayList<>();
    private OglElementArrayBuffer elementArrayBuffer;

    private int size = 0;

    public OglVertexArray() {
        super(glGenVertexArrays());
    }

    public void bindBuffer(OglBuffer buffer, int[] sizes, VertexAttribute[] vertexAttributes, int type, int typeSize) {
        referencedBuffers.add(buffer);
        bind();
        buffer.bind();
        int stride = Arrays.stream(sizes).sum() * typeSize; // in bytes
        int offset = 0; // in bytes
        for (int q = 0; q < sizes.length; q++) {
            glEnableVertexAttribArray(vertexAttributes[q].ordinal());
            glVertexAttribPointer(vertexAttributes[q].ordinal(), sizes[q], type, false,
                    stride, offset);
            offset += sizes[q] * typeSize;
        }
    }

    public void bindElementBuffer(OglElementArrayBuffer elementBuffer) {
        this.elementArrayBuffer = elementBuffer;
        bind();
        elementBuffer.bind();
    }


    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void delete() {
        glDeleteVertexArrays(id);
    }

    @Override
    public void bind() {
        glBindVertexArray(id);
    }

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    public List<OglBuffer> getReferencedBuffers() {
        return referencedBuffers;
    }
}
