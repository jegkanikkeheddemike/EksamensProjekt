#ifdef GL_ES
precision mediump float;
#endif

#define PROCESSING_COLOR_SHADER

void main(){
    int size = 1800;
    gl_FragColor = vec4(float(gl_FragCoord.x/size),1.0,1.0,1.0);
}