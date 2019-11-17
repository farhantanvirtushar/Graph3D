package com.farhantanvir.graph3d;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class Axis {

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;"+
                    "attribute vec4 aColor;"+
                    "varying vec4 vColor;" +
                    "uniform mat4 u_MVMatrix;"+
                    "uniform vec3 u_LightPos;"+
                    "attribute vec3 a_Normal;"+
                    "void main() {" +
                    "   vec3 modelViewVertex = vec3(u_MVMatrix * vPosition);              \n"+

                    "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"+

                    "   float distance = length(u_LightPos - modelViewVertex);             \n"+
                    "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"+
                    "   float diffuse = max(dot(modelViewNormal, lightVector),0.1);       \n"+

                    "   diffuse = diffuse * (1.0 / (1.0 + (0.000025 * distance * distance)));  \n"+


                    "    vColor      = aColor ;" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";


    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";



    private int mProgram;
    private int vPMatrixHandle;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float lineCoords[];

    // Set color with red, green, blue and alpha (opacity) values
    float color[];



    private int positionHandle;
    private int colorHandle;

    private int vertexCount ;
    private int vertexStride ;
    int colorCount;
    int colorStride;




    public Axis(float lineCoords[], float color[]) {

        this.lineCoords = lineCoords;
        this.color=color;


        vertexCount = lineCoords.length / COORDS_PER_VERTEX;
        vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

        colorCount=color.length/4;
        colorStride= 4 *4;





        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);



        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                lineCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(lineCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(color.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        colorBuffer = byteBuffer.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);




    }


    public void draw(float[] mvpMatrix){

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);


        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        colorHandle    = GLES20.glGetAttribLocation(mProgram, "aColor");



        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);


        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle,4,GLES20.GL_FLOAT,false,colorStride,colorBuffer);

        // get handle to fragment shader's vColor member


        // Set color for drawing the triangle
        //GLES20.glUniform4fv(colorHandle, 1, color, 0);




        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);



        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
        GLES20.glLineWidth(5.0f);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);

    }
}
