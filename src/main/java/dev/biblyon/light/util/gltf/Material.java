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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class Material {
    public boolean doubleSided = false;
    public String name;
    public PbrMetallicRoughness pbrMetallicRoughness;
    public TextureInfo normalTexture;
    public TextureInfo emissiveTexture;
    public TextureInfo occlusionTexture;
    public float[] emissiveFactor = new float[] { 0,0,0};
    public AlphaMode alphaMode;
    public float alphaCutoff = 0.5f;

    public enum AlphaMode {
        OPAQUE, MASK, BLEND
    }
}
