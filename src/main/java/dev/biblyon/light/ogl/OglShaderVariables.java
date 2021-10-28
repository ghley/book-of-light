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

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class OglShaderVariables {
    public Map<String, Float> scalar = new HashMap<>();
    public Map<String, Vector2f> vec2 = new HashMap<>();
    public Map<String, Vector3f> vec3 = new HashMap<>();
    public Map<String, Matrix3f> mat3 = new HashMap<>();
    public Map<String, Matrix4f> mat4 = new HashMap<>();
    public Map<String, float[]> fv = new HashMap<>();
    public Map<String, Vector3f[]> vec3v = new HashMap<>();

    public Map<String, int[]> tex2d = new HashMap<>();

    private float[] mat3Arr = new float[9];
    private float[] mat4Arr = new float[16];

    public void apply(OglProgram program){
        program.bind();
        for (var entry : scalar.entrySet()) {
            glUniform1f(program.getLocation(entry.getKey()), entry.getValue());
        }
        for (var entry : vec2.entrySet()) {
            var vec = entry.getValue();
            glUniform2f(program.getLocation(entry.getKey()), vec.x, vec.y);
        }
        for (var entry : vec3.entrySet()) {
            var vec = entry.getValue();
            glUniform3f(program.getLocation(entry.getKey()), vec.x, vec.y, vec.z);
        }
        for (var entry : mat3.entrySet()) {
            var mat = entry.getValue();
            mat.get(mat3Arr);
            glUniformMatrix3fv(program.getLocation(entry.getKey()), false, mat3Arr);
        }
        for (var entry : mat4.entrySet()) {
            var mat = entry.getValue();
            mat.get(mat4Arr);
            glUniformMatrix4fv(program.getLocation(entry.getKey()), false, mat4Arr);
        }
        for (var entry : tex2d.entrySet()) {
            glActiveTexture(GL_TEXTURE0 + entry.getValue()[0]);
            glUniform1i(program.getLocation(entry.getKey()), entry.getValue()[0]);
            glBindTexture(GL_TEXTURE_2D, entry.getValue()[1]);
        }
        for (var entry : fv.entrySet()) {
            glUniform1fv(program.getLocation(entry.getKey()), entry.getValue());
        }
        for (var entry : vec3v.entrySet()) {
            float[] arr = new float[entry.getValue().length*3];
            for (int q = 0; q < entry.getValue().length; q++) {
                for (int i = 0; i < 3; i++) {
                    arr[q * 3 + i] = entry.getValue()[q].get(i);
                }
            }
            glUniform3fv(program.getLocation(entry.getKey()), arr);
        }
    }
}
