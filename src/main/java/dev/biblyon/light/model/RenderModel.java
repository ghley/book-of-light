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

import dev.biblyon.light.ogl.*;
import org.joml.Matrix4f;

public class RenderModel {
//    private final Matrix4f localTransform;
    private final OglVertexArray vao;
    private final OglProgram program;
    private final OglMaterial material;
    private final int size;

    public RenderModel(OglVertexArray vao, OglProgram program, OglMaterial material, int size) {
        this.vao = vao;
        this.program = program;
        this.material = material;
        this.size = size;
    }

    public OglVertexArray getVao() {
        return vao;
    }

    public OglProgram getProgram() {
        return program;
    }

    public OglMaterial getMaterial() {
        return material;
    }

    public int getSize() {
        return size;
    }
}
