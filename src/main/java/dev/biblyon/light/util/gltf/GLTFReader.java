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

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.biblyon.light.model.RenderModel;
import dev.biblyon.light.ogl.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class GLTFReader {
    private String path;

    private GLTF gltf = null;

    private GLTFReader(String path) {
        this.path = path;
    }

    public static GLTFReader read(String path) {
        var reader = new GLTFReader(path);
        reader.load();
        return reader;
    }

    public RenderModel toRenderModel(int meshIndex) {
        Mesh mesh = gltf.meshes[meshIndex];

        var primitive = mesh.primitives[0];
        Material material = primitive.materialObj;

        OglProgram.VertexAttribute[] attributes = primitive.attributes.keySet().stream().sorted().toArray(OglProgram.VertexAttribute[]::new);
        Accessor[] accessors = new Accessor[attributes.length];
        int[] sizes = new int[attributes.length];
        int[] offsets = new int[attributes.length];
        int i = 0;
        int fullSize = 0;
        for (var key : attributes) {
            var accessor = primitive.attributeAccessors.get(key);
            accessors[i] = accessor;
            System.out.println(Arrays.toString(accessors[i].floatData));
            sizes[i] = primitive.attributeAccessors.get(key).type.size;
            if (i != 0) {
                offsets[i] = sizes[i-1] + offsets[i-1];
            }
            i++;
            fullSize += accessor.count * accessor.type.size;
        }
        int count = accessors[0].count;
        int stride = offsets[sizes.length-1] + sizes[sizes.length-1];

        float[] arr = new float[fullSize]; // for now hardcoded

        for (i = 0; i < count; i++) {
            int offset = stride * i;
            for (int s = 0; s < sizes.length; s++) {
                var accessor = accessors[s];
                for (int r = 0; r < sizes[s]; r++) {
                    arr[offset + offsets[s] + r] = accessor.floatData[i*sizes[s]+r]; // for now hardcoded float array
                }
            }
        }

        short[] indices = primitive.indicesAccessor.shortData;

        OglElementArrayBuffer ebo = new OglElementArrayBuffer();
        ebo.setData(indices);

        OglArrayBuffer abo = new OglArrayBuffer();
        abo.setData(arr);

        OglVertexArray vao = new OglVertexArray();
        vao.bindBuffer(abo, sizes, attributes, GL_FLOAT, 4);
        vao.bindElementBuffer(ebo);
        vao.setSize(count);

        OglMaterial oglMaterial = new OglMaterial();
        int image = processImage(material.pbrMetallicRoughness.baseColorTexture.texture.image.image);
        oglMaterial.setBaseTexture(image);

        OglProgram program = new ProgramBuilder().vertex("resources/default.vs").fragment("resources/default.fs").build();
        var model = new RenderModel(vao, program, oglMaterial, indices.length);
        return model;
    }

    private int processImage(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);

        for(int h = 0; h < image.getHeight(); h++) {
            for(int w = 0; w < image.getWidth(); w++) {
                int pixel = pixels[h * image.getWidth() + w];

                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        return id;
    }

    public void load() {
        String json = "";
        try {
            json = Files.readString(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();

        try {
            gltf = mapper.readValue(json, GLTF.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (gltf == null) {
            throw new RuntimeException("File not found or failed to load.");
        }

        // reverse dependency order
        buffer();
        bufferView();
        accessor();
        image();
        texture();
        material();
        mesh();
//        animation();
//        skin();
        node();
        scene();
    }

    private void buffer() {
        for (Buffer buffer : gltf.buffers) {
            String base64 = buffer.uri.substring(buffer.uri.lastIndexOf(',') + 1);
            byte[] data = Base64.getDecoder().decode(base64);
            buffer.data = data;
        }
    }

    private void bufferView() {
        for (BufferView view : gltf.bufferViews) {
            view.data = Arrays.copyOfRange(gltf.buffers[view.buffer].data, view.byteOffset, view.byteOffset + view.byteLength);
        }
    }

    private void texture() {
        for (Texture texture : gltf.textures) {
            if (texture.source != null) {
                texture.image = gltf.images[texture.source];
            }
            if (texture.sampler != null) {
                texture.samplerObj = gltf.samplers[texture.sampler];
            }
        }
    }

    private void node() {
        for (Node node : gltf.nodes) {
            if (node.mesh != null) {
                node.meshObj = gltf.meshes[node.mesh];
            }
        }
    }

    private void scene() {
        for (Scene scene : gltf.scenes) {
            scene.nodeObjs = new Node[scene.nodes.length];
            for (int q = 0; q < scene.nodes.length; q++) {
                scene.nodeObjs[q] = gltf.nodes[scene.nodes[q]];
            }
        }
    }

    private void material() {
        for (Material material : gltf.materials) {
            if (material.emissiveTexture != null) {
                material.emissiveTexture.texture = gltf.textures[material.emissiveTexture.index];
            }
            if (material.normalTexture != null) {
                material.normalTexture.texture = gltf.textures[material.normalTexture.index];
            }
            if (material.occlusionTexture != null) {
                material.occlusionTexture.texture = gltf.textures[material.occlusionTexture.index];
            }
            if (material.pbrMetallicRoughness != null) {
                PbrMetallicRoughness pbr = material.pbrMetallicRoughness;
                if (pbr.baseColorTexture != null) {
                    pbr.baseColorTexture.texture = gltf.textures[pbr.baseColorTexture.index];
                }
                if (pbr.metallicRoughnessTexture != null) {
                    pbr.metallicRoughnessTexture.texture = gltf.textures[pbr.metallicRoughnessTexture.index];
                }
            }
        }
    }

    @SuppressWarnings("SwitchStatementWithoutDefaultBranch")
    private void accessor() {
        for (Accessor accessor : gltf.accessors) {
            ByteBuffer buffer = ByteBuffer.wrap(gltf.bufferViews[accessor.bufferView].data);
            // not sure if this is always the default, but it's clearly not BIG_ENDIAN
            buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
            switch (accessor.componentType) {
                case BYTE, UNSIGNED_BYTE -> {
                    accessor.byteData = buffer.array();
                }
                case SHORT, UNSIGNED_SHORT -> {
                    var shortBuffer = buffer.asShortBuffer();
                    accessor.shortData = new short[shortBuffer.limit()];
                    shortBuffer.get(accessor.shortData);
                }
                case UNSIGNED_INT -> {
                    var intBuffer = buffer.asIntBuffer();
                    accessor.intData = new int[intBuffer.limit()];
                    intBuffer.get(accessor.intData);
                }
                case FLOAT -> {
                    var floatBuffer = buffer.asFloatBuffer();
                    accessor.floatData = new float[floatBuffer.limit()];
                    floatBuffer.get(accessor.floatData);
                }
            }
        }
    }

    private void mesh() {
        for (Mesh mesh : gltf.meshes) {
            for (Primitive primitive : mesh.primitives) {
                primitive.attributeAccessors = new HashMap<>();
                for (var entry : primitive.attributes.entrySet()) {
                    primitive.attributeAccessors.put(entry.getKey(), gltf.accessors[entry.getValue()]);
                }
                if (primitive.material != null) {
                    primitive.materialObj = gltf.materials[primitive.material];
                }
                if (primitive.indices != null) {
                    primitive.indicesAccessor = gltf.accessors[primitive.indices];
                }
            }
        }
    }

    private void image() {
        for (Image image : gltf.images) {
            if (image.uri != null) {
                throw new RuntimeException("Not implemented");
            } else {
                BufferView view = gltf.bufferViews[image.bufferView];
                try {
                    image.image = ImageIO.read(new ByteArrayInputStream(view.data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
