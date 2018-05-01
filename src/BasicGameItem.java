import math.Matrix4f;
import math.Vector3f;

public class BasicGameItem {

    private final Mesh mesh;

    private final Vector3f position;

    private Vector3f scale;

    private final Vector3f rotation;

    private Matrix4f worldMatrix;

    private static BasicMeshShaderProgram basicMeshShaderProgram;

    public BasicGameItem(Mesh mesh) throws Exception {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);
        rotation = new Vector3f(0, 0, 0);
        worldMatrix = new Matrix4f();

        basicMeshShaderProgram = BasicMeshShaderProgram.getInstance();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        updateWorldMatrix();
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
        updateWorldMatrix();
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
        updateWorldMatrix();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void draw(Matrix4f projectionMatrix) {
        if(basicMeshShaderProgram != null) {
            basicMeshShaderProgram.bind();
            basicMeshShaderProgram.setUniform("projectionMatrix", projectionMatrix);
            basicMeshShaderProgram.setUniform("worldMatrix", worldMatrix);

            mesh.draw(projectionMatrix);

            basicMeshShaderProgram.unbind();
        }

    }

    private void updateWorldMatrix() {
        worldMatrix.setIdentity();
        Matrix4f rotMat = Matrix4f.translate(-position.x, -position.y, -position.z)
                .multiply(Matrix4f.rotate(rotation.x,1.0f, 0.0f, 0.0f ))
                .multiply(Matrix4f.translate(position.x, position.y, position.z));
        worldMatrix =  Matrix4f.translate(position.x, position.y, position.z)
                .multiply(Matrix4f.rotate(rotation.x,1.0f, 0.0f, 0.0f ))
                .multiply(Matrix4f.rotate(rotation.y,0.0f, 1.0f, 0.0f ))
                .multiply(Matrix4f.rotate(rotation.z,0.0f, 0.0f, 1.0f ))
                .multiply(Matrix4f.scale(scale.x, scale.y, scale.z));
    }
}