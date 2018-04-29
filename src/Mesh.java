import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import math.Matrix4f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

    private  int vaoId;

    private  int vboId;

    private  ShaderProgram shaderProgram;

    private  int vertexCount;

    public Mesh(float[] positions) {
        FloatBuffer verticesBuffer = null;
        try {
            shaderProgram = new ShaderProgram();
            shaderProgram.createVertexShader(Utils.readFile("./res/shaders/vertex.vs"));
            shaderProgram.createFragmentShader(Utils.readFile("./res/shaders/fragment.fs"));
            shaderProgram.link();

            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            vertexCount = positions.length / 3;
            verticesBuffer.put(positions).flip();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindVertexArray(0);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (verticesBuffer  != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void draw() {
        shaderProgram.bind();

        Matrix4f mat;

        // Bind to the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, getVertexCount());

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
