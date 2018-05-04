public class BasicMeshShaderProgram extends ShaderProgram {
    private static BasicMeshShaderProgram instance = null;

    public static BasicMeshShaderProgram getInstance() throws Exception {
        if(instance == null) {
            instance = new BasicMeshShaderProgram();
        }
        return instance;
    }

    protected BasicMeshShaderProgram() throws Exception {
        super();

        System.out.println("Should only print once!");
        createVertexShader(Utils.readFile("./res/shaders/vertex.vs"));
        createFragmentShader(Utils.readFile("./res/shaders/fragment.fs"));
        link();

        createUniform("viewProjectionMatrix");
        createUniform("worldMatrix");
        createUniform("texture_sampler");
    }


}