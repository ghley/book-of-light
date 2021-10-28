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

public class OglMaterial extends OglShaderVariables {
    private float[] pbr = new float[Material.values().length];

    public OglMaterial() {
        set(Material.SPECULAR, 0.5f);
        set(Material.ROUGHNESS, 0.5f);
        set(Material.SHEEN_TINT, 0.5f);
        set(Material.CLEAR_COAT_GLOSS, 1f);
        fv.put("pbr", pbr);
    }

    public void setBaseTexture(int baseTexture) {
        tex2d.put("base", new int[]{0, baseTexture});
    }

    public void setOcclusion(int occlusion) {
        tex2d.put("occlusion", new int[]{2, occlusion});
    }

    public void setNormal(int normal) {
        tex2d.put("normal", new int[]{1, normal});
    }

    public void setEmissive(int emissive) {
        tex2d.put("emissive", new int[]{3, emissive});
    }

    public void set(Material material, float value) {
        pbr[material.ordinal()] = value;
    }

    public enum Material {
        SPECULAR,
        SPECULAR_TINT,
        ROUGHNESS,
        METALLIC,
        SHEEN,
        SHEEN_TINT,
        ANISOTROPIC,
        CLEAR_COAT,
        CLEAR_COAT_GLOSS,
        SUBSURFACE;
    }

}
