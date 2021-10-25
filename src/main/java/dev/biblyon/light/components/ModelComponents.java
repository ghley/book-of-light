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

package dev.biblyon.light.components;

import dev.biblyon.light.ogl.OglMaterial;
import dev.biblyon.principle.ecs.Component;
import dev.biblyon.principle.ecs.Components;

public class ModelComponents extends Components<ModelComponents.Bean> {

    public ModelComponents() {
        super(Bean.class);
    }

    public static class Bean implements Component<Bean> {
        private int vaoId;
        private int programId;
        private OglMaterial material;

        @Override
        public void set(Bean meshComponent) {
            this.vaoId = meshComponent.vaoId;
            this.programId = meshComponent.programId;
            this.material = meshComponent.material;
        }
    }
}
