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

package dev.biblyon.light.util;

import dev.biblyon.light.RenderView;
import dev.biblyon.light.util.gltf.GLTFReader;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.lwjgl.opengl.GL11.*;

class GLTFDatabaseTest {

    @Test
    public void test() {
        RenderView view = new RenderView();
        view.init();


        var reader = GLTFReader.read("./resources/test.gltf");
        var renderModel = reader.toRenderModel(0);

        var program = renderModel.getProgram();
        var vao = renderModel.getVao();
        var material = renderModel.getMaterial();

        material.mat4.put("model", new Matrix4f().identity());
        material.mat4.put("view", new Matrix4f().lookAt(new Vector3f(2,2,2), new Vector3f(0,0,0), new Vector3f(0, 1, 0)));
        material.mat4.put("projection", new Matrix4f().perspective((float)Math.toRadians(80), view.getRatio(), 0.0001f, 200f ));
        material.apply(program);

        while (view.isAlive()) {
            view.preRender();

            vao.bind();
            program.bind();
            material.apply(program);

            glDrawElements(GL_TRIANGLES, renderModel.getSize(), GL_UNSIGNED_SHORT, 0);

            view.postRender();
        }
    }

}