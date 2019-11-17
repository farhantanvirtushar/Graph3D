package com.farhantanvir.graph3d;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.ContentValues.TAG;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];
    private final float[] vMVMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    private float[] rotationMatrix = new float[16];

    private Graph graph;
    Axis axis;


    public volatile float xAngle;
    public volatile float yAngle;

    final float lightPos[] = {-10.0f,-10.0f,10.0f};

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable( GLES20.GL_DEPTH_TEST );
        GLES20.glDepthFunc( GLES20.GL_LEQUAL );
        GLES20.glDepthMask( true );

//render
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        EquationEvaluation equationEvaluation = MainActivity.data.equationEvaluation;

        int x_len = 400;
        int y_len = 400;
        float unit = 20.0f;
        float z[][] = new float[x_len][y_len];
        for(int i=0;i<y_len;i++)
        {
            for(int j=0;j<x_len;j++)
            {
                float y = (float) (((((float)i)-(y_len/2)))/unit);
                float x = (float) ((((float)j)-(x_len/2))/unit);
                z[i][j]= (float)(equationEvaluation.evaluate(x,y));
                //z[i][j]=((float)Math.sqrt(100-((x*x)+(y*y))));
            }
        }
        //float c1[] = { 1.0f, 0, 0, 1.0f};
        float vertex[] = new float[(x_len-1)*(y_len-1)*18];
        float normal[] = new float[(x_len-1)*(y_len-1)*18];
        //float light[] = new float[(x_len-1)*(y_len-1)*18];
        //float c1[] =  new float[(x_len-1)*(y_len-1)*24];
        int len_vertex = vertex.length;
        int k=0;
        int l=0;
        for(int i=0;i<(x_len-1);i++)
        {
            for(int j=0;j<(y_len-1);j++)
            {
                float y1 = (float) (((((float)i)-y_len/2))/unit);
                float x1 = (float) ((((float)j)-(x_len/2))/unit);

                float y2 = (float) (((((float)i+1)-y_len/2))/unit);
                float x2 = x1;

                float y3 = y2;
                float x3 = (float) ((((float)j+1)-(x_len/2))/unit);

                float y4 = y1;
                float x4 = x3;

                vertex[k]=x1;
                k++;
                vertex[k]=y1;
                k++;
                vertex[k]=z[i][j];
                k++;
                vertex[k]=x4;
                k++;
                vertex[k]=y4;
                k++;
                vertex[k]=z[i][j+1];
                k++;
                vertex[k]=x2;
                k++;
                vertex[k]=y2;
                k++;
                vertex[k]=z[i+1][j];
                k++;


                //normal for first tirangle

                float n[];
                n = normalVector(x1,y1,z[i][j],x4,y4,z[i][j+1],x2,y2,z[i+1][j]);
                for(int m=0;m<3;m++)
                {
                    normal[l]=n[0];
                    l++;
                    normal[l]=n[1];
                    l++;
                    normal[l]=n[2];
                    l++;
                }
                //second triangle

                vertex[k]=x2;
                k++;
                vertex[k]=y2;
                k++;
                vertex[k]=z[i+1][j];
                k++;
                vertex[k]=x4;
                k++;
                vertex[k]=y4;
                k++;
                vertex[k]=z[i][j+1];
                k++;
                vertex[k]=x3;
                k++;
                vertex[k]=y3;
                k++;
                vertex[k]=z[i+1][j+1];
                k++;

                n = normalVector(x2,y2,z[i+1][j],x4,y4,z[i][j+1],x3,y3,z[i+1][j+1]);
                for(int m=0;m<3;m++)
                {
                    normal[l]=n[0];
                    l++;
                    normal[l]=n[1];
                    l++;
                    normal[l]=n[2];
                    l++;
                }
            }
        }


        float c1[] =  new float[(x_len-1)*(y_len-1)*24];
        int col_len = c1.length;
        k=0;
        l=0;
        float v = 1.0f;
        while (k<col_len)
        {
            //v = dot(normal[l],normal[l+1],normal[l+2],light[l],light[l+1],light[l+2]);
            float r = (float)Math.sqrt((vertex[l]*vertex[l])+(vertex[l+1]*vertex[l+1]));
            l=l+3;
            r = r/(15.0f);
            c1[k]=(1.0f-r)*v;
            k++;
            c1[k]=r*v;
            k++;
            c1[k]=0;
            k++;
            c1[k]=1.0f;
            k++;


        }
        graph = new Graph(vertex,c1,lightPos,normal);

        float axisVertex[] = new float[120];
        float axisColor[] = new float[160];
        k=0;
        float x = -12.0f;
        while (!(x>12))
        {
            axisVertex[k]=x;
            axisVertex[k+1]=-12.0f;
            axisVertex[k+2]=0.0f;
            axisVertex[k+3]=x;
            axisVertex[k+4]=12.0f;
            axisVertex[k+5]=0.0f;
            k=k+6;
            x=x+4.0f;
        }
        float y = -12.0f;
        while (!(y>12))
        {
            axisVertex[k]=-12.0f;
            axisVertex[k+1]=y;
            axisVertex[k+2]=0.0f;
            axisVertex[k+3]=12.0f;
            axisVertex[k+4]=y;
            axisVertex[k+5]=0.0f;
            k=k+6;
            y=y+4.0f;
        }

        // z axis
        axisVertex[k]=0.0f;
        axisVertex[k+1]=0.0f;
        axisVertex[k+2]=-10.0f;
        axisVertex[k+3]=0.0f;
        axisVertex[k+4]=0.0f;
        axisVertex[k+5]=10.0f;

        k+=6;
        // axis label

        //x
        axisVertex[k]=12.2f; axisVertex[k+1]=0.5f; axisVertex[k+2]=0.0f; axisVertex[k+3]=13.2f; axisVertex[k+4]=-0.5f; axisVertex[k+5]=0.0f;k=k+6;
        axisVertex[k]=13.2f; axisVertex[k+1]=0.5f; axisVertex[k+2]=0.0f; axisVertex[k+3]=12.2f; axisVertex[k+4]=-0.5f; axisVertex[k+5]=0.0f;k=k+6;
        //y
        axisVertex[k]=-0.5f; axisVertex[k+1]=13.2f; axisVertex[k+2]=0.0f; axisVertex[k+3]=0.0f; axisVertex[k+4]=12.7f; axisVertex[k+5]=0.0f;k=k+6;
        axisVertex[k]=0.5f; axisVertex[k+1]=13.2f; axisVertex[k+2]=0.0f; axisVertex[k+3]=0.0f; axisVertex[k+4]=12.7f; axisVertex[k+5]=0.0f;k=k+6;
        axisVertex[k]=0.0f; axisVertex[k+1]=12.7f; axisVertex[k+2]=0.0f; axisVertex[k+3]=0.0f; axisVertex[k+4]=12.2f; axisVertex[k+5]=0.0f;k=k+6;


        for(int i=0;i<120;i++)
        {
            axisColor[i]=0.0f;
            if((i%4)==0)
            {
                if(((i/4)==6)||((i/4)==7))
                axisColor[i]=1.0f;
            }
            if((i%4)==2)
            {
                if(((i/4)==20)||((i/4)==21))
                    axisColor[i]=1.0f;
            }
            if((i%4)==1)
            {
                if(((i/4)==28)||((i/4)==29))
                    axisColor[i]=1.0f;
            }

        }
        k = 120;
        //x color
        axisColor[k]=0.0f; axisColor[k+1]=0.0f; axisColor[k+2]=1.0f; axisColor[k+3]=1.0f; axisColor[k+4]=0.0f; axisColor[k+5]=0.0f; axisColor[k+6]=1.0f; axisColor[k+7]=1.0f;k=k+8;
        axisColor[k]=0.0f; axisColor[k+1]=0.0f; axisColor[k+2]=1.0f; axisColor[k+3]=1.0f; axisColor[k+4]=0.0f; axisColor[k+5]=0.0f; axisColor[k+6]=1.0f; axisColor[k+7]=1.0f;k=k+8;
        //y color
        axisColor[k]=1.0f; axisColor[k+1]=0.0f; axisColor[k+2]=0.0f; axisColor[k+3]=1.0f; axisColor[k+4]=1.0f; axisColor[k+5]=0.0f; axisColor[k+6]=0.0f; axisColor[k+7]=1.0f;k=k+8;
        axisColor[k]=1.0f; axisColor[k+1]=0.0f; axisColor[k+2]=0.0f; axisColor[k+3]=1.0f; axisColor[k+4]=1.0f; axisColor[k+5]=0.0f; axisColor[k+6]=0.0f; axisColor[k+7]=1.0f;k=k+8;
        axisColor[k]=1.0f; axisColor[k+1]=0.0f; axisColor[k+2]=0.0f; axisColor[k+3]=1.0f; axisColor[k+4]=1.0f; axisColor[k+5]=0.0f; axisColor[k+6]=0.0f; axisColor[k+7]=1.0f;k=k+8;

        axis = new Axis(axisVertex,axisColor);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1f, 100);
    }

    @Override
    public void onDrawFrame(GL10 gl) {


        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable( GLES20.GL_DEPTH_TEST );
        GLES20.glDepthFunc( GLES20.GL_LEQUAL );
        GLES20.glDepthMask( true );

//render
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, -10.0f, -10.0f, 20, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        Matrix.setIdentityM(vMVMatrix,0);
        Matrix.multiplyMM(vMVMatrix,0,vMVMatrix,0,viewMatrix,0);




        // Create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.090f * ((int) time);
        Matrix.setIdentityM(rotationMatrix,0);
        Matrix.rotateM(rotationMatrix, 0, yAngle, 1, 0, 0);
        Matrix.rotateM(rotationMatrix, 0, xAngle, 0, 1, 0);


        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        // Draw axis

        axis.draw(scratch);

        // Draw graph
        graph.draw(scratch,vMVMatrix);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    public float getXAngle() {
        return xAngle;
    }

    public void setXAngle(float angle) {
        xAngle = angle;
    }
    public float getYAngle() {
        return yAngle;
    }

    public void setYAngle(float angle) {
        yAngle = angle;
    }

    public float [] normalVector(float x1,float y1,float z1,float x2,float y2,float z2,float x3,float y3,float z3){
        float n[] = new float[3];
            float a = x3-x2;
            float b = y3-y2;
            float c = z3-z2;

            float d = x1-x2;
            float e = y1-y2;
            float f = z1-z2;

            n[0]= (b*f) - (e*c);
            n[1]= (c*d) - (a*f);
            n[2]= (a*e) - (b*d);
            float len = (float) Math.sqrt((n[0]*n[0])+(n[1]*n[1])+(n[2]*n[2]));

            for(int i=0;i<3;i++)
            {
                n[i] = n[i]/len;
            }
        return n;
    }

    float dot(float x1,float y1 , float z1 ,float x2,float y2 , float z2 ){
        float a = (x1*x2)+(y1*y2)+(z1*z2);
        if(a<0.1){
            a=0.1f;
        }
        return a;
    }

}
