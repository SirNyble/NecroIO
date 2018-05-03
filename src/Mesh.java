import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import de.matthiasmann.twl.utils.PNGDecoder;
import math.Matrix4f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

    private int vaoId;

    private int vboId;
    private int textureVboId;
    private int textureId;
    private int idxVboId;

    private int vertexCount;

    public Mesh(float[] positions, float[] textureCoords, int[] indices) {
        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;

            verticesBuffer = memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();

            InputStream in = new FileInputStream("./res/textures/minecraft.png");
            PNGDecoder decoder = new PNGDecoder(in);

            ByteBuffer buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            // Create a new OpenGL texture
            textureId = glGenTextures();
// Bind the texture
            glBindTexture(GL_TEXTURE_2D, textureId);

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            int width = decoder.getWidth();
            int height = decoder.getHeight();
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(),
                    decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glGenerateMipmap(GL_TEXTURE_2D);
            ///////

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            // Colour VBO
            textureVboId = glGenBuffers();
            FloatBuffer colourBuffer = memAllocFloat(textureCoords.length);
            colourBuffer.put(textureCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
            memFree(colourBuffer);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindVertexArray(0);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (verticesBuffer  != null) {
                memFree(verticesBuffer);
                memFree(indicesBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void draw(Matrix4f projectionMatrix) {

        // Activate first texture unit
        glActiveTexture(GL_TEXTURE0);
// Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);


        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the vertices
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
