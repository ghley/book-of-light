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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public final class ProgramBuilder {
    private Map<Integer, String> map = new HashMap<>();

    public ProgramBuilder vertex(String path) {
        try (var scanner = new Scanner(new File(path)).useDelimiter("\\A")) {
            map.put(GL_VERTEX_SHADER, scanner.next());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ProgramBuilder fragment(String path) {
        try (var scanner = new Scanner(new File(path)).useDelimiter("\\A")) {
            map.put(GL_FRAGMENT_SHADER, scanner.next());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public OglProgram build() {
        return new OglProgram(map);
    }
}
