import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.awt.Color;
import java.awt.image.BufferedImage;
public class Graphics {
    public final static int black = Color.BLACK.getRGB();
    private int numLines = 0;
    private int numCor = 0;
    private double[][] originalVertex;
    private double[][] vertex;
    private int[][] lines;
    private int[][] twoDVertex;
    public static void main(String[] args) {
        Graphics graphics = new Graphics();
        //double[][] test1 = {{1,1,1,1},{1,1,1,1},{1,1,1,1},{1,1,1,1}};
        //double[][] test2 = {{1,2,3,4}, {1,2,3,4}, {1,2,3,4}, {1,2,3,4}};
        //double[] test3 = {1,2,3,4};
        //graphics.matrixOneByFour(test3, test1);
        //graphics.matrixFourByFour(test2, test1);
        graphics.InputLines("test1.txt", 8, 12);
        graphics.worldConversion(6, 8, 7.5, 60, 15);
        for(int i = 0; i < graphics.vertex.length; i++) {
            System.out.print(graphics.vertex[i][0] + " ");
            System.out.print(graphics.vertex[i][1] + " ");
            System.out.println(graphics.vertex[i][2] + " ");
        } 
    }
    /**
     * takes the data from a file and puts in the originalVertex.
     * @param dataLines - name of the file.
     * @param numCor - number of coordinates.
     * @param numLines - number of lines.
     * @return
     */
    public int InputLines(String dataLines, int numCor, int numLines) {
        this.numLines = numLines;
        lines = new int[numLines][2];
        this.numCor = numCor;
        vertex = new double[numCor][3];
        originalVertex = new double[numCor][3];
        File file = new File(dataLines);
        try {
            Scanner scanner = new Scanner(file);
            int k = 0;
            int j = 0;
            int i = 0;
            int next;
            while(i < numCor) {
                next = scanner.nextInt();
                vertex[k][j] = next;
                originalVertex[k][j] = next;
                //System.out.print(vertex[k][j] + " ");
                if(j == 2) {
                    k++;
                    j = 0;
                    i++;
                    //System.out.println();
                } else {
                j++;
                } // if
            }
            i = 0;
            j = 0;
            k = 0;
            while(i < numLines) {
                lines[k][j] = (scanner.nextInt());
                //System.out.print(lines[k][j] + " ");
                if(j == 1) {
                    k++;
                    j = 0;
                    i++;
                    //System.out.println();
                } else {
                j++;
                } // if
            }
            scanner.close();
    
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        return 1;
    }
    /**
     * output the coordinates and the lines onto a file.
     * @param dataLines -name of the file that will hold the contents
     * @param numCors - number of coordinates.
     * @param numLines - number of lines.
     */
    public void outputLines(String dataLines, int numCors, int numLines) {
        try {
            String content = "";
            for(int i = 0; i < numCor; i++) {
                content = content + vertex[i][0] + " ";
                content = content + vertex[i][1] + " ";
                content = content + vertex[i][2] + "\n";
            } // for
            for(int i = 0; i < numLines; i++) {
                content = content + lines[i][0] + " ";
                content = content + lines[i][1] + "\n";
            } // for
            Path fileName = Path.of(dataLines);
            Files.writeString(fileName, content);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     *  converts the coordinates to the eye coordinate system.
     * @param x - the x of the viewpoint coordinate.
     * @param y - the y of the viewpoint coordinate.
     * @param z - the z of the viewpoint coordinate.
     * @param d - the distance that the screen is expected to be viewed from.
     * @param s - the size of the screen
     */
    public void worldConversion(double x, double y, double z, double d, double s) {
        double[][] t1 = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{-x,-y,-z,1}};
        double[][] t2 = {{1,0,0,0},{0,0,-1,0},{0,1,0,0},{0,0,0,1}};
        double m1 = y/Math.sqrt((Math.pow(x, 2)+Math.pow(y, 2)));
        double m2 = x/Math.sqrt((Math.pow(x, 2)+Math.pow(y, 2)));
        double[][] t3 = {{-m1,0,m2,0},{0,1,0,0},{-m2,0,-m1,0},{0,0,0,1}};
        double m3 = z/Math.sqrt((Math.pow(z, 2) + Math.pow(Math.sqrt(Math.pow(x,2) + Math.pow(y, 2)), 2)));
        double m4 = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        m4 = m4/(Math.sqrt(Math.pow(z,2) + Math.pow(Math.sqrt(Math.pow(x,2) + Math.pow(y,2)),2)));
        double[][] t4 = {{1,0,0,0},{0,m4,m3,0},{0,-m3,m4,0},{0,0,0,1}};
        double[][] t5 = {{1,0,0,0},{0,1,0,0},{0,0,-1,0},{0,0,0,1}};
        double[][] newMatrix = matrixFourByFour(t1, t2);
        /*for(int i = 0; i < t4.length; i++) {
            for(int j = 0; j < t4[i].length; j++) {
                System.out.print(t4[i][j] + " ");
            }
            System.out.println();
        } */
        newMatrix = matrixFourByFour(newMatrix, t3);
        newMatrix = matrixFourByFour(newMatrix, t4);
        newMatrix = matrixFourByFour(newMatrix, t5);
        double[][] n = {{(d/s),0,0,0},{0,(d/s),0,0},{0,0,1,0},{0,0,0,1}};
        newMatrix = matrixFourByFour(newMatrix, n);
        for(int i = 0; i < numCor; i++) {
            double[] tempMatrix = {originalVertex[i][0],originalVertex[i][1],originalVertex[i][2],1};
            vertex[i] = matrixOneByFour(tempMatrix, newMatrix);
        }// for
    } // worldConversion
    /**
     * converts from the eye coordinate system to the 2d plane.
     * @param Vsx - width of viewport.
     * @param Vcx - the x of the center of the viewport.
     * @param Vsy - height of viewport.
     * @param Vcy - the y of the center of the viewport.
     */
    public void twoDConversion(double Vsx, double Vcx, double Vsy, double Vcy) {
        twoDVertex = new int[numCor][2];
        for(int i = 0;   i < numCor; i++) {
           twoDVertex[i][0] = (int)(vertex[i][0]/vertex[i][2]* Vsx + Vcx);
           twoDVertex[i][1] = (int)(vertex[i][1]/vertex[i][2]* Vsy + Vcy);
           //System.out.println(vertex[i][0] + " " + vertex[i][1]);
           System.out.print(twoDVertex[i][0] + " ");
           System.out.println(twoDVertex[i][1]);
        } // for
    } //twoDConversion
    /**
     * translates the object on the screen.
     * @param tx - the number the object will be translated by in regards to the x-axis.
     * @param ty - the number the object will be translated by in regards to the y-axis.
     * @param tz - the number the object will be translated by in regards to the z-axis.
     */
    public void translate(double tx, double ty, double tz) {
        double[][] matrix = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{tx,ty,tz,1}};
        applyTransformations(matrix);
    } // translate
    /**
     * scales the object on the screen.
     * @param Sx - the number it scales the object by in regards to the x-axis.
     * @param Sy - the number it scales the object by in regards to the y-axis.
     * @param Sz - the number it scales the object by in regards to the z-axis.
     * @param Cx - the x coordinate of the arbitrary point that the object will be scaled by.
     * @param Cy - the y coordinate of the arbitrary point that the object will be scaled by.
     * @param Cz - the z coordinate of the arbitrary point that the object will be scaled by.
     */
    public void scale(double Sx, double Sy, double Sz, double Cx, double Cy, double Cz) {
        double[][] matrix1 = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{-Cx,-Cy,-Cz,1}};
        double[][] matrix2 = {{Sx,0,0,0},{0,Sy,0,0},{0,0,Sz,0},{0,0,0,1}};
        double[][] matrix3 = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{Cx,Cy,Cz,1}};
        applyTransformations(matrixFourByFour(matrixFourByFour(matrix1, matrix2), matrix3));
    } // scale
    /**
     *  rotates the object by a certain angle around a certain axis.
     * @param angle - angle of rotation.
     * @param axis - axis that the object will be rotated around.
     */
    public void rotation(double angle, String axis) {
        if(axis.equalsIgnoreCase("z")){
            double[][] matrix = {{Math.cos(angle),Math.sin(angle),0,0},{-Math.sin(angle),
                Math.cos(angle),0,0},{0,0,1,0},{0,0,0,1}};
            applyTransformations(matrix);
        } else if(axis.equalsIgnoreCase("y")) {
            double[][] matrix = {{Math.cos(angle),0,-Math.sin(angle),0},{0,1,0,0},
            {Math.sin(angle),0,Math.cos(angle),0},{0,0,0,1}};
            applyTransformations(matrix);
        } else if(axis.equalsIgnoreCase("x")) {
            double[][] matrix = {{1,0,0,0},{0,Math.cos(angle),Math.sin(angle),0},
            {0,-Math.sin(angle),Math.cos(angle),0},{0,0,0,1}};
            applyTransformations(matrix);
        } else {
            System.out.println("invalid axis");
        }
    }
    /**
     * It multiples the coordinates by the transformation matrix to get
     * new coordinates.
     * @param matrix- matrix to multiply the tempMatrix to get new coordinates
     */
    public void applyTransformations(double[][] matrix) {
        double[] tempMatrix = new double[4];
        tempMatrix[3] = 1;
        double[] newMatrix;
        for(int i = 0; i < numCor; i++) {
            tempMatrix[0] = originalVertex[i][0];
            tempMatrix[1] = originalVertex[i][1];
            tempMatrix[2] = originalVertex[i][2];
            newMatrix = matrixOneByFour(tempMatrix, matrix);
            originalVertex[i][0] = newMatrix[0];
            originalVertex[i][1] = newMatrix[1];
            originalVertex[i][2] = newMatrix[2];
        } // for
    } //applyTransformations
    /**
     * displays the object from the twodvertex array.
     * @param canvas -the screen that the object will be displayed on.
     */
    public void display(BufferedImage canvas) {
        /*for(int i = 0; i < lines.length; i++) {
            for(int j = 0; j < lines[i].length; j++) {
                System.out.print(lines[i][j] + " ");
            }
            System.out.println();
        } */
        for(int i = 0; i < numLines; i++) {
            int firstLine = lines[i][0]-1;
            int secondLine = lines[i][1]-1;
            this.bresenhamAlg(twoDVertex[firstLine][0], twoDVertex[firstLine][1], twoDVertex[secondLine][0],
             twoDVertex[secondLine][1], canvas);
        } // for
    } // display
    /**
     *  multiplies a one by four matrix by a four by four matrix.
     * @param coordinates - the one by four.
     * @param matrix - the four by four.
     * @return - a one by four matrix.
     */
    public double[] matrixOneByFour(double[] coordinates, double[][] matrix) {
        double[] newMatrix = new double[4];
        newMatrix[0] = (matrix[0][0] *coordinates[0]) + (matrix[1][0] * coordinates[1]) +(matrix[2][0] * coordinates[2])
        + (matrix[3][0] * coordinates[3]);
        newMatrix[1] = (matrix[0][1] *coordinates[0]) + (matrix[1][1] * coordinates[1]) +(matrix[2][1] * coordinates[2])
        + (matrix[3][1] * coordinates[3]);
        newMatrix[2] = (matrix[0][2] *coordinates[0]) + (matrix[1][2] * coordinates[1]) +(matrix[2][2] * coordinates[2])
        + (matrix[3][2] * coordinates[3]);
        newMatrix[3] = (matrix[0][3] *coordinates[0]) + (matrix[1][3] * coordinates[1]) +(matrix[2][3] * coordinates[2])
        + (matrix[3][3] * coordinates[3]);
        //System.out.println(newMatrix[0]);
        //System.out.println(newMatrix[1]);
        //System.out.println(newMatrix[2]);
       // System.out.println(newMatrix[3]);
        return newMatrix;
    } // matrixOneByFour
    /**
     * multiplies two four by four matrices together.
     * @param matrix1 - the first four by four matrix.
     * @param matrix2 - the second four by four matrix.
     * @return - the resulting matrix.
     */
    public double[][] matrixFourByFour(double[][] matrix1, double[][] matrix2) {
        double[][] newMatrix = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        newMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                    } // for
                } // for 
            } // for
        /*for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                System.out.print(newMatrix[i][j] + " ");
            } // for
            System.out.println();
        }  // for */
        return newMatrix;
    } // matrixFourByFour
    /**
     *  It runs the bresenham algorithm for making lines.
     * source for algorithm-http://fredericgoset.ovh/mathematiques/courbes/en/bresenham_line.html
     * @param x0 - the first x coordinate
     * @param y0 - the first y coordinate
     * @param x1 - the second x coordinate
     * @param y1 - the second y coordinate
     * @param canvas - the object that will be drawn on.
     */
    public void bresenhamAlg(int x0, int y0, int x1, int y1, BufferedImage canvas) {
        //System.out.println("x0: " + x0 + " x1: " + x1 + " y0: " + y0 + "y1: " + y1); //test
        int dx = x1 - x0;
			int dy = y1 - y0;
			int incX = (int)Math.signum(dx); // gets the increment for x
			int incY = (int)Math.signum(dy); // gets the increment for y
			dx = Math.abs(dx);
			dy = Math.abs(dy);
            int y = y0;
            int x = x0;
			if (dy == 0) // horizontal line
			{
				while( x != x1 + incX) {
                    try {
					canvas.setRGB(x, y, black);
                    } catch(Exception e) {

                    }
                    x += incX;
                } // while
			}
			else if (dx == 0) // vertical line
			{
				while ( y != (y1 + incY)) {
                    try {
					canvas.setRGB(x, y, black);
                    } catch(Exception e) {
                        
                    }
                    y += incY;
                } // while
			}
            // this is the case of when delta x is more then delta y
			else if (dx >= dy)
			{
				int slope = 2 * dy; //since dy is smaller or equal slope is created using dy.
				int err = -dx; // err takes into account dx becuase it is bigger.
				int errInc = -2 * dx;

				while ( x != x1 + incX)
				{
                    try {
                        canvas.setRGB(x, y, black);
                        } catch(Exception e) {
                            
                        }
                    //System.out.println("x: " + x + " y: " + y); //text case
					err += slope; // will add slope to err to check that the line has close to the same slope.
                    // if the error is off it will correct.
					if (err >= 0) 
					{
						y += incY;
						err += errInc;
					}
                    x += incX;
				}
			}
			else
			{
				// when delta y is more then delta x
                // basically you just swithc dy with dx and increment by y so all points are set.
                // you also switch y and x.
				int slope = 2 * dx;
				int err = -dy;
				int errInc = -2 * dy;
				while ( y != y1 + incY)
				{
                    try {
                        canvas.setRGB(x, y, black);
                        } catch(Exception e) {
                            
                        }
                    //System.out.println("x: " + x + " y: " + y); //test case
					err += slope;

					if (err >= 0)
					{
						x += incX;
						err += errInc;
					}
                    y += incY;
				}
			}
    } //bresenhamAlg
}
