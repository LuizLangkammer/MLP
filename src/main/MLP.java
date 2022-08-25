package main;

import java.util.Random;

public class MLP {

    private double ni;
    private int numIn,numOut,numH;
    private int qntOut;
    private double[][] wh;
    private double[][] wo;

    public MLP(double ni, int numIn, int numOut, int numH){

        this.ni = ni;
        this.numIn = numIn;
        this.numOut = numOut;
        this.numH = numH;

        Random random = new Random();

        wh = new double[numIn+1][numH];
        wo = new double[numH+1][numOut];

        for(int i=0;i<numIn;i++) {
            for(int j=0;j<numH;j++) {
                wh[i][j] = (random.nextDouble(0.6)-0.3);
            }
        }
        for(int i=0;i<numH;i++) {
            for(int j=0;j<numOut;j++) {
                wo[i][j] = (random.nextDouble(0.6)-0.3);
            }
        }

    }



}
