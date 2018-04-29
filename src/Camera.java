import math.Matrix4f;

public class Camera {

    private float FOV = 60.0f;
    private float zNear = 0.01f;
    private float zFar = 1000.0f;
    private float aspectRatio = 1.0f;
    private Matrix4f projectionMatrix;

    public Camera(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        projectionMatrix = new Matrix4f();
        updateProjectionMatrix();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void updateProjectionMatrix() {
        projectionMatrix = projectionMatrix.perspective(FOV, aspectRatio, zNear, zFar);
        boolean rhys = true;
    }
}
