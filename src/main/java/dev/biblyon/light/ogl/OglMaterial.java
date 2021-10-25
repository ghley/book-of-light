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

import org.joml.Vector3f;

public class OglMaterial {
    private int baseTexture = -1;
    private int occlusion = -1;
    private int normal = -1;
    private int emissive = -1;

    public int getBaseTexture() {
        return baseTexture;
    }

    public void setBaseTexture(int baseTexture) {
        this.baseTexture = baseTexture;
    }

    public int getOcclusion() {
        return occlusion;
    }

    public void setOcclusion(int occlusion) {
        this.occlusion = occlusion;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getEmissive() {
        return emissive;
    }

    public void setEmissive(int emissive) {
        this.emissive = emissive;
    }
}
