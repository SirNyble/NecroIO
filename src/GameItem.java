import math.Matrix4f;
import math.Vector3f;

public class GameItem {

    private final Mesh mesh;

    private final Vector3f position;

    private Vector3f scale;

    private final Vector3f rotation;

    private Matrix4f worldMatrix;

    private static BasicMeshShaderProgram basicMeshShaderProgram;

    public GameItem(Mesh mesh) throws Exception {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 0, 0);
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

            mesh.draw(projectionMatrix);

            basicMeshShaderProgram.unbind();
        }

    }

    private void updateWorldMatrix() {
        worldMatrix.setIdentity();
        worldMatrix.translate(position.x, position.y, position.z);
//        worldMatrix.translate(this.position, worldMatrix).
//            rotate((float)Math.toRadians(rotation.x), new Vector3f(1.0f, 0.0f, 0.0f) )
//            .rotate((float)Math.toRadians(rotation.y), new Vector3f(0.0f, 1.0f, 0.0f) )
//            .rotate((float)Math.toRadians(rotation.z), new Vector3f(0.0f, 0.0f, 1.0f) )
//            .scale(scale);
    }
}