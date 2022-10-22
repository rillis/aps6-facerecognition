package com.rillis.aps6.facereco.images;

import com.rillis.aps6.facereco.recognition.FaceDetection;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class Picture{
    Mat picMat;

    boolean comparable = false;
    String path = null;

    public Picture(String path, String outputPath) {
        this.picMat = Imgcodecs.imread(path);
        this.path = path;
        treatImage(outputPath);
    }

    public Picture(Mat mat) {
        this.picMat = mat;
    }

    public void write(String path){
        Imgcodecs.imwrite(path, this.picMat);
    }

    public void drawRect(Rect rect){
        Imgproc.rectangle(picMat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,0,255), 2);
    }

    public void toGrayScale(){
        Imgproc.cvtColor(this.picMat, this.picMat, Imgproc.COLOR_BGRA2GRAY);
    }

    public void drawAllRects(Rect[] rects){
        for(Rect rect : rects){
            drawRect(rect);
        }
    }

    public void resizeToDefaultSize(){
        Imgproc.resize(this.picMat, this.picMat, new Size(200, 200));
    }

    public Mat getPicMat() {
        return picMat;
    }

    public void treatImage(String outputPath){
        MatOfRect faces = FaceDetection.detectFaces(this);

        if(faces.toArray().length != 1){
            System.out.println("Too many or zero faces detected ("+faces.toArray().length+")");
        }else{
            this.picMat = new Mat(getPicMat(), faces.toArray()[0]);
            resizeToDefaultSize();
            toGrayScale();

            if(outputPath.length() > 0){
                String[] pathSplited = path.split("/");
                write(outputPath+pathSplited[pathSplited.length-1]);
            }
            comparable = true;

        }
    }

    public boolean isComparable() {
        return comparable;
    }
}
