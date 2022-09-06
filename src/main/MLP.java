package main;

import java.util.Random;

public class MLP {

    private double ni;
    private int numInput,numOutput,numIntermediary;

    private double[][] wh;
    private double[][] wo;

    public MLP(double ni, int numInput, int numOutput, int numIntermediary){

        this.ni = ni;
        this.numInput = numInput;
        this.numOutput = numOutput;
        this.numIntermediary = numIntermediary;

        Random random = new Random();

        wh = new double[numInput+1][numIntermediary];
        wo = new double[numIntermediary+1][numOutput];

        for(int i=0;i<numInput+1;i++) {
            for(int j=0;j<numIntermediary;j++) {
                wh[i][j] =(random.nextDouble(0.6)-0.3);
            }
        }
        for(int i=0;i<numIntermediary+1;i++) {
            for(int j=0;j<numOutput;j++) {
                wo[i][j] = (random.nextDouble(0.6)-0.3);
            }
        }
    }

    public double[] learn(double[] input, double[] desiredOutput){

        double[] entries = new double[input.length + 1];
        for(int i=0;i<input.length;i++) {
            entries[i]=input[i];
        }
        entries[input.length] = 1;

        double[] h = sum(numIntermediary, entries, wh, true);
        double[] o = sum(numOutput, h, wo, false);

        double [] deltaO = new double[numOutput];
        for(int i=0; i<numOutput; i++){
            deltaO[i] = o[i] * (1-o[i]) * (desiredOutput[i] - o[i]);
        }

        double[] deltaH = new double[numIntermediary];

        for(int i=0; i<numIntermediary; i++){
            double soma = 0;
            for(int j=0; j<numOutput; j++){
                soma += deltaO[j] * wo[i][j];
            }
            deltaH[i] = h[i] * (1-h[i]) * soma;
        }

        for(int i=0; i<numInput+1; i++){
            for(int j=0; j<numIntermediary; j++){
                wh[i][j] += (ni * deltaH[j] * entries[i]);
            }
        }

        for(int i=0; i<numIntermediary+1; i++){
            for(int j=0; j<numOutput;j++){
                wo[i][j] += (ni * deltaO[j] * h[i]);
            }
        }
        return o;
    }

    public double[] exec(double[] input, double[] desiredOutput){
        double[] entries = new double[input.length + 1];
        for(int i=0;i<input.length;i++) {
            entries[i]=input[i];
        }
        entries[input.length] = 1;

        double[] h = sum(numIntermediary, entries, wh, true);
        double[] o = sum(numOutput, h, wo, false);
        return o;
    }

    private double[] sum (int qntOut, double[] input, double[][] weight, boolean bias){

        double[] out;
        if(bias){
            out = new double[qntOut+1];
            out[qntOut] = 1;
        }else{
            out = new double[qntOut];
        }


        for(int j=0; j<qntOut;j++){
            for(int i=0; i<input.length; i++){
                out[j] += input[i] * weight[i][j];
            }
            out[j] = sig(out[j]);
        }

        return out;
    }

    private double sig(double u) {
        return 1/(1+Math.exp(-u));
    }



}
