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

package dev.biblyon.light.util.gltf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.biblyon.light.ogl.OglProgram;
import dev.biblyon.light.ogl.util.VertexAttribute;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
class Primitive {
    public Map<VertexAttribute, Integer> attributes;
    public Integer material;
    public Integer indices;
    public PrimitiveMode mode;

    @JsonIgnore
    public Map<VertexAttribute, Accessor> attributeAccessors;
    @JsonIgnore
    public Accessor indicesAccessor;
    @JsonIgnore
    public Material materialObj;


    public void setMode(int value) {
        mode = PrimitiveMode.values()[value];
    }

    public int getMode() {
        return mode.ordinal();
    }

    public enum PrimitiveMode {
        POINTS, LINES, LINE_LOOP, LINE_STRIP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN
    }

}
