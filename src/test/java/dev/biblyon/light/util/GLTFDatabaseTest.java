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
import dev.biblyon.light.components.ModelComponents;
import dev.biblyon.light.system.RenderSystem;
import dev.biblyon.light.util.gltf.GLTFReader;
import dev.biblyon.principle.ecs.EntityManager;
import dev.biblyon.principle.ecs.components.TransformComponents;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

class GLTFDatabaseTest {

    @Test
    public void test() {
        RenderView view = new RenderView();
        view.init();
        var camera = view.getCamera();
        camera.setPosition(new Vector3f(2,2,2));
        camera.setTarget(new Vector3f(0,0,0));
        camera.update();

        var reader = GLTFReader.read("./resources/test5.gltf");
        var renderModel = reader.toRenderModel(0);

//        var program = renderModel.getProgram();
//        var vao = renderModel.getVao();
//        var material = renderModel.getMaterial();

        EntityManager entityManager = new EntityManager();
        var entity = entityManager.createEntity();

        RenderSystem system = new RenderSystem(new TransformComponents(), new ModelComponents());
        system.createNewModel(entity, renderModel);

//        Vector3f[] offsets = new Vector3f[100];
//        for (int q = 0; q < 10; q++) {
//            for (int r = 0; r < 10; r++) {
//                offsets[q*10 + r] = new Vector3f(q, r, 0);
//            }
//        }
//
//        float t = 0;

        while (view.isAlive()) {
//            var v = new Vector3f(20,10,-4);
//            material.vec3v.put("lightPos", new Vector3f[] { v });
//
//            material.apply(program);
//
            view.preRender();
//
//            vao.bind();
//            program.bind();
//            material.apply(program);

//            glDrawElements(GL_TRIANGLES, renderModel.getSize(), GL_UNSIGNED_SHORT, 0);
//            glDrawElementsInstanced(GL_TRIANGLES, renderModel.getSize(), GL_UNSIGNED_SHORT, 0, 100);

            system.renderAll();

            view.postRender();
        }
    }

}