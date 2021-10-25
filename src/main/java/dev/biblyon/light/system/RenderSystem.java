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

package dev.biblyon.light.system;

import dev.biblyon.light.components.ModelComponents;
import dev.biblyon.principle.ecs.components.TransformComponents;

public class RenderSystem {
    private TransformComponents transforms;
    private ModelComponents meshes;

    public RenderSystem(TransformComponents transforms, ModelComponents meshes) {
        this.transforms = transforms;
        this.meshes = meshes;
    }

    public void renderAll() {

    }
}
