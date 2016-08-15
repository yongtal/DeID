/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

/**
 *
 * @author QM
 */
public class OrientationState {

    public final int Z_AXIS = 0;
    public final int Y_AXIS = 1;
    public final int X_AXIS = 2;
    public final int ORIGINAL_FRONT_FACE = 0;
    public final int ORIGINAL_TOP_FACE = 1;
    public final int ORIGINAL_LEFT_FACE = 2;
    public final int ORIGINAL_BOTTOM_FACE = 3;
    public final int ORIGINAL_RIGHT_FACE = 4;
    public final int ORIGINAL_BACK_FACE = 5;
    public int currentAxis; //0 for z, 1 for y, 2 for x.
    public boolean isForward; // if this orietation is in positive direction
    public int leftFace;
    public int rightFace;
    public int topFace;
    public int bottomFace;
    public int frontFace;
    public int backFace;
    public int widthAxis;
    public int heightAxis;
    public boolean widthPostive = true;
    public boolean heightPostive = true;

    public OrientationState(int cA, boolean iF, int fF, int backF, int tF, int lF, int bF, int rF) {
        this.currentAxis = cA;
        this.isForward = iF;
        this.leftFace = lF;
        this.rightFace = rF;
        this.topFace = tF;
        this.bottomFace = bF;
        this.frontFace = fF;
        this.backFace = backF;

        switch (fF) {
            case ORIGINAL_FRONT_FACE:
                switch (tF) {
                    case ORIGINAL_TOP_FACE:
                        this.heightAxis = Y_AXIS;
                        this.heightPostive = true;
                        this.widthAxis = X_AXIS;
                        this.widthPostive = true;

                        break;
                    case ORIGINAL_RIGHT_FACE:
                        this.widthAxis = Y_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = X_AXIS;
                        this.heightPostive = false;

                        break;
                    case ORIGINAL_LEFT_FACE:
                        this.widthAxis = Y_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = X_AXIS;
                        this.heightPostive = true;
                        break;
                    case ORIGINAL_BOTTOM_FACE:
                        this.widthAxis = X_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = Y_AXIS;
                        this.heightPostive = false;
                }
                break;
            case ORIGINAL_TOP_FACE:
                switch (tF) {
                    case ORIGINAL_BACK_FACE:
                        this.widthAxis = X_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = Z_AXIS;
                        this.heightPostive = false;
                        break;
                    case ORIGINAL_RIGHT_FACE:
                        this.widthAxis = Z_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = X_AXIS;
                        this.heightPostive = false;
                        break;
                    case ORIGINAL_FRONT_FACE:
                        this.widthAxis = X_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = Z_AXIS;
                        this.heightPostive = true;
                        break;
                    case ORIGINAL_LEFT_FACE:
                        this.widthAxis = Z_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = X_AXIS;
                        this.heightPostive = true;
                        break;
                }
                break;
            case ORIGINAL_LEFT_FACE:
                switch (tF) {
                    case ORIGINAL_TOP_FACE:
                        this.widthAxis = Z_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = Y_AXIS;
                        this.heightPostive = true;
                        break;
                    case ORIGINAL_FRONT_FACE:
                        this.widthAxis = Y_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = Z_AXIS;
                        this.heightPostive = true;
                        break;
                    case ORIGINAL_BOTTOM_FACE:
                        this.widthAxis = Z_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = Y_AXIS;
                        this.heightPostive = false;
                        break;
                    case ORIGINAL_BACK_FACE:
                        this.widthAxis = Y_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = Z_AXIS;
                        this.heightPostive = false;
                        break;
                }
                break;
            case ORIGINAL_BOTTOM_FACE:
                switch (tF) {
                    case ORIGINAL_FRONT_FACE:
                        this.widthAxis = X_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = Z_AXIS;
                        this.widthPostive = true;
                        break;
                    case ORIGINAL_RIGHT_FACE:
                        this.widthAxis = Z_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = X_AXIS;
                        this.heightPostive = false;
                        break;
                    case ORIGINAL_BACK_FACE:
                        this.widthAxis = X_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = Z_AXIS;
                        this.heightPostive = false;
                        break;
                    case ORIGINAL_LEFT_FACE:
                        this.widthAxis = Z_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = X_AXIS;
                        this.heightPostive = true;
                        break;
                }
                break;
            case ORIGINAL_RIGHT_FACE:
                switch (tF) {
                    case ORIGINAL_TOP_FACE:
                        this.widthAxis = Z_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = Y_AXIS;
                        this.heightPostive = true;
                        break;
                    case ORIGINAL_BACK_FACE:
                        this.widthAxis = Y_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = Z_AXIS;
                        this.heightPostive = false;
                        break;
                    case ORIGINAL_BOTTOM_FACE:
                        this.widthAxis = Z_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = Y_AXIS;
                        this.heightPostive = false;
                        break;
                    case ORIGINAL_FRONT_FACE:
                        this.widthAxis = Y_AXIS;
                        this.widthPostive = false;
                        this.leftFace = Z_AXIS;
                        this.heightPostive = true;
                        break;
                }
                break;
            case ORIGINAL_BACK_FACE: {
                switch (tF) {
                    case ORIGINAL_TOP_FACE:
                        this.widthAxis = X_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = Y_AXIS;
                        this.heightPostive = true;
                        break;
                    case ORIGINAL_LEFT_FACE:
                        this.widthAxis = Y_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = X_AXIS;
                        this.heightPostive = true;
                        break;
                    case ORIGINAL_BOTTOM_FACE:
                        this.widthAxis = X_AXIS;
                        this.widthPostive = true;
                        this.heightAxis = Y_AXIS;
                        this.heightPostive = false;
                        break;
                    case ORIGINAL_RIGHT_FACE:
                        this.widthAxis = Y_AXIS;
                        this.widthPostive = false;
                        this.heightAxis = X_AXIS;
                        this.heightPostive = false;
                        break;
                }
            }
            break;
        }
    }

    public OrientationState toLeft() {
        int newAxis;
        boolean newPositiveDirection;

        switch (frontFace) {
            case ORIGINAL_FRONT_FACE:
                switch (topFace) {
                    case ORIGINAL_TOP_FACE:
                        newAxis = X_AXIS;
                        newPositiveDirection = true;
                        return new OrientationState(newAxis,
                                newPositiveDirection, ORIGINAL_LEFT_FACE,
                                ORIGINAL_RIGHT_FACE, ORIGINAL_TOP_FACE,
                                ORIGINAL_BACK_FACE, ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_RIGHT_FACE);
                    case ORIGINAL_RIGHT_FACE:
                        newAxis = Y_AXIS;
                        newPositiveDirection = true;
                        return new OrientationState(newAxis,
                                newPositiveDirection,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_FRONT_FACE);
                    case ORIGINAL_BOTTOM_FACE:
                        newAxis = X_AXIS;
                        newPositiveDirection = false;
                        return new OrientationState(newAxis,
                                newPositiveDirection,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_FRONT_FACE);
                    case ORIGINAL_LEFT_FACE:
                        newAxis = Y_AXIS;
                        newPositiveDirection = false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_FRONT_FACE);
                }
                break;
            case ORIGINAL_TOP_FACE:
                switch (topFace) {
                    case ORIGINAL_BACK_FACE:
                        newAxis = X_AXIS;
                        newPositiveDirection = true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_TOP_FACE);
                    case ORIGINAL_RIGHT_FACE:
                        newAxis = Z_AXIS;
                        newPositiveDirection = false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_TOP_FACE);
                    case ORIGINAL_FRONT_FACE:
                        newAxis = X_AXIS;
                        newPositiveDirection = false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_TOP_FACE);
                    case ORIGINAL_LEFT_FACE:
                        newAxis = Z_AXIS;
                        newPositiveDirection = true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_TOP_FACE);
                }
                break;
            case ORIGINAL_LEFT_FACE:
                switch (topFace) {
                    case ORIGINAL_TOP_FACE:
                        newAxis = Z_AXIS;
                        newPositiveDirection = false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_LEFT_FACE);
                    case ORIGINAL_FRONT_FACE:
                        newAxis = Y_AXIS;
                        newPositiveDirection = true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_LEFT_FACE);
                    case ORIGINAL_BOTTOM_FACE:
                        newAxis = Z_AXIS;
                        newPositiveDirection = true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_LEFT_FACE);
                    case ORIGINAL_BACK_FACE:
                        newAxis = Y_AXIS;
                        newPositiveDirection = false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_LEFT_FACE);
                }
                break;
            case ORIGINAL_BOTTOM_FACE:
                switch (topFace) {
                    case ORIGINAL_FRONT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_LEFT_FACE, 
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_BOTTOM_FACE);
                    case ORIGINAL_RIGHT_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BOTTOM_FACE);
                    case ORIGINAL_BACK_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_RIGHT_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_BOTTOM_FACE);
                    case ORIGINAL_LEFT_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_RIGHT_FACE, 
                                ORIGINAL_BOTTOM_FACE);
                }
                break;
            case ORIGINAL_RIGHT_FACE:
                switch(topFace)
                {
                    case ORIGINAL_TOP_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_RIGHT_FACE);
                    case ORIGINAL_BACK_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_RIGHT_FACE);
                    case ORIGINAL_BOTTOM_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_RIGHT_FACE);
                    case ORIGINAL_FRONT_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_LEFT_FACE, 
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_RIGHT_FACE);
                }
                break;
            case ORIGINAL_BACK_FACE:
                switch(topFace)
                {
                    case ORIGINAL_TOP_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_RIGHT_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_BACK_FACE);
                    case ORIGINAL_LEFT_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_RIGHT_FACE, 
                                ORIGINAL_BACK_FACE);
                    case ORIGINAL_BOTTOM_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_LEFT_FACE, 
                                ORIGINAL_RIGHT_FACE, 
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BACK_FACE);
                    case ORIGINAL_RIGHT_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_LEFT_FACE, 
                                ORIGINAL_BACK_FACE);
                }
                break;
        }
        return null;
    }

    public OrientationState toRight() {
        OrientationState leftOnce=toLeft();
        OrientationState leftTwice=leftOnce.toLeft();
        OrientationState right=leftTwice.toLeft();
        return right;
    }

    public OrientationState toTop() {
        int newAxis;
        boolean newPositiveDirection;
        switch(frontFace)
        {
            case ORIGINAL_FRONT_FACE:
                switch(topFace)
                {
                    case ORIGINAL_TOP_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_RIGHT_FACE);
                    case ORIGINAL_RIGHT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, 
                                newPositiveDirection, 
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_FRONT_FACE);
                    case ORIGINAL_BOTTOM_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_LEFT_FACE);
                    case ORIGINAL_LEFT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis,
                                newPositiveDirection, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_TOP_FACE);
                }
                break;
            case ORIGINAL_BACK_FACE:
                switch(topFace)
                {
                    case ORIGINAL_TOP_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_LEFT_FACE);
                    case ORIGINAL_LEFT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_LEFT_FACE, 
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_BOTTOM_FACE);
                    case ORIGINAL_BOTTOM_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_RIGHT_FACE);
                    case ORIGINAL_RIGHT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_LEFT_FACE, 
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_TOP_FACE);
                }
                break;
            case ORIGINAL_LEFT_FACE:
                switch(topFace)
                {
                    case ORIGINAL_TOP_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis,
                                newPositiveDirection,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_RIGHT_FACE, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_LEFT_FACE, 
                                ORIGINAL_FRONT_FACE);
                    case ORIGINAL_FRONT_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BOTTOM_FACE);
                    case ORIGINAL_BOTTOM_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BACK_FACE);
                    case ORIGINAL_BACK_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_LEFT_FACE);
                }
                break;
            case ORIGINAL_RIGHT_FACE:
                switch(topFace)
                {
                    case ORIGINAL_FRONT_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_LEFT_FACE, 
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_TOP_FACE);
                    case ORIGINAL_TOP_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_RIGHT_FACE, 
                                ORIGINAL_BACK_FACE);
                    case ORIGINAL_BACK_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BOTTOM_FACE);
                    case ORIGINAL_BOTTOM_FACE:
                        newAxis=Y_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_FRONT_FACE);
                }
                break;
            case ORIGINAL_TOP_FACE:
                switch(topFace)
                {
                    case ORIGINAL_BACK_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_RIGHT_FACE);
                    case ORIGINAL_RIGHT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_BACK_FACE, 
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_FRONT_FACE);
                    case ORIGINAL_FRONT_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_LEFT_FACE);
                    case ORIGINAL_LEFT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_FRONT_FACE,
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_BACK_FACE);
                }
                break;
            case ORIGINAL_BOTTOM_FACE:
                switch(topFace)
                {
                    case ORIGINAL_FRONT_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_RIGHT_FACE);
                    case ORIGINAL_RIGHT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_BACK_FACE);
                    case ORIGINAL_BACK_FACE:
                        newAxis=Z_AXIS;
                        newPositiveDirection=false;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_FRONT_FACE, 
                                ORIGINAL_TOP_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_BOTTOM_FACE,
                                ORIGINAL_LEFT_FACE);
                    case ORIGINAL_LEFT_FACE:
                        newAxis=X_AXIS;
                        newPositiveDirection=true;
                        return new OrientationState(newAxis, newPositiveDirection, 
                                ORIGINAL_LEFT_FACE,
                                ORIGINAL_RIGHT_FACE,
                                ORIGINAL_TOP_FACE, 
                                ORIGINAL_BACK_FACE,
                                ORIGINAL_BOTTOM_FACE, 
                                ORIGINAL_FRONT_FACE);                        
                }
                break;
        }
       
        return null;
    }

    public OrientationState toBottom() {
         OrientationState upOnce=toTop();
        OrientationState upTwice=upOnce.toTop();
        OrientationState down=upTwice.toTop();
        return down;
       
    }
}