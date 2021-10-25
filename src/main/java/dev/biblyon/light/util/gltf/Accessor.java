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
import org.joml.Vector3f;

import java.beans.Transient;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class Accessor {
    public int bufferView;
    public ComponentType componentType;
    public int count;
    public float[] max;
    public float[] min;
    public AccessorType type;

    @JsonIgnore
    public byte[] byteData;
    @JsonIgnore
    public short[] shortData;
    @JsonIgnore
    public int[] intData;
    @JsonIgnore
    public float[] floatData;

    public enum AccessorType {
        SCALAR(1), VEC2(2), VEC3(3), VEC4(4), MAT2(4), MAT3(9), MAT4(16);

        public int size;

        AccessorType(int size) {
            this.size = size;
        }
    }

    public void setComponentType(int type) {
        componentType = Arrays.stream(ComponentType.values()).filter(t->t.num == type).findAny().get();
    }

    public int getComponentType() {
        return componentType.num;
    }

    public enum ComponentType {
        BYTE(5120), UNSIGNED_BYTE(5121), SHORT(5122), UNSIGNED_SHORT(5123),
        UNSIGNED_INT(5125), FLOAT(5126);

        private int num;

        ComponentType(int num) {
            this.num = num;
        }
    }
}
