void setup() {
  size(1000,800,P2D);
  frameRate(60);
  shader = loadShader("Shader.frag");
  shader(shader);
  rect(0,0,width,height);
}

PShader shader;
