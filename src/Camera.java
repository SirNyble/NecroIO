import math.Matrix4f;
import math.Vector3f;

public class Camera {

    private float FOV = 60.0f;
    private float zNear = 0.01f;
    private float zFar = 1000.0f;
    private float aspectRatio = 1.0f;
    private Matrix4f projectionMatrix;

    private Vector3f position;
    private Vector3f rotation;

    public Camera(float aspectRatio, Vector3f position, Vector3f rotation) {
        this.aspectRatio = aspectRatio;
        this.position = position;
        this.rotation = rotation;
        this.projectionMatrix = new Matrix4f();
        updateProjectionMatrix();
    }

    public Vector3f getPosition() { return position; }
    public Vector3f getRotation() { return rotation; }
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    public Matrix4f getViewMatrix() {
        Vector3f cameraPos = getPosition();
        Vector3f rotation = getRotation();

        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        // First do the rotation so camera rotates over its position
        viewMatrix = Matrix4f.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)
                .multiply(Matrix4f.rotate(rotation.x, 1, 0, 0))
                .multiply(Matrix4f.rotate(rotation.y, 0, 1, 0));

        return viewMatrix;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
//
//    public Matrix4f getViewMatrix() {
//        return getProjectionMatrix().multiply(getViewMatrix());
//    }

    public void updateProjectionMatrix() {
        projectionMatrix = projectionMatrix.perspective(FOV, aspectRatio, zNear, zFar);
    }
}
