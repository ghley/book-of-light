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

import dev.biblyon.light.ogl.OglUniformBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;

public class Camera {
    private Vector3f position = new Vector3f();
    private Vector3f target = new Vector3f();
    private float fov = (float)Math.toRadians(80f);
    private float ratio =  1.f;
    private float zNear = 0.01f;
    private float zFar = 1000f;

    private OglUniformBuffer buffer;

    public Camera() {
        buffer = new OglUniformBuffer();
        buffer.setData(new float[32]);
        buffer.bindBase(1);
    }

    public void update() {
        float[] both = new float[32];
        view().get(both, 0);
        perspective().get(both, 16);
        buffer.setData(both);
    }

    public Matrix4f perspective() {
        return new Matrix4f().perspective(fov, ratio, zNear, zFar); // TODO pre calc in future
    }

    public Matrix4f view() {
        return new Matrix4f().lookAt(position, target, new Vector3f(0,1,0));
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void setTarget(Vector3f target) {
        this.target = target;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getzNear() {
        return zNear;
    }

    public void setzNear(float zNear) {
        this.zNear = zNear;
    }

    public float getzFar() {
        return zFar;
    }

    public void setzFar(float zFar) {
        this.zFar = zFar;
    }
}
