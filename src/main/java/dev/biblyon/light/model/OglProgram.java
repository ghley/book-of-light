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

import dev.biblyon.principle.utils.PrincipleUtils;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.opengl.GL20.*;

public class OglProgram extends OglObject {
    private Map<String, Integer> locations = new HashMap<>();

    public OglProgram(Map<Integer, String> shaderSrcMap) {
        super(glCreateProgram());
        Set<Integer> shaderIds = new HashSet<>();
        for (int type : shaderSrcMap.keySet()) {
            int shaderId = glCreateShader(type);
            glShaderSource(shaderId, shaderSrcMap.get(type));
            glCompileShader(shaderId);
            if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
                throw new RuntimeException(String.format("Shader (%d) failed: %s", type, glGetShaderInfoLog(shaderId)));
            }
            shaderIds.add(shaderId);
        }

        shaderIds.forEach(sId->glAttachShader(id, sId));
        glLinkProgram(id);

        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException(String.format("Linking failed: %s", glGetProgramInfoLog(id)));
        }

        shaderIds.forEach(GL20::glDeleteShader); // can be deleted after linking
    }

    @Override
    public void delete() {
        glDeleteProgram(id);
    }

    @Override
    public void bind() {
        glUseProgram(id);
    }

    @Override
    public void unbind() {
        glUseProgram(0);
    }

    public int getLocation(String name) {
        if (!locations.containsKey(name)) {
            locations.put(name, glGetUniformLocation(id, name));
        }
        int location = locations.get(name);
        if (location == -1) {
            PrincipleUtils.getLogger().info("Couldn't find location: "+name);
        }
        return location;
    }
}
