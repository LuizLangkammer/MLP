package main;

import fileReader.CarEvaluationReader;
import fileReader.MyInputReader;

import java.io.*;
import java.util.ArrayList;

public class MLPRunner {


    public static void main (String[] args) {

        //Parameters
        String basePath = "xor-base.txt";
        double ni = 0.3;
        int ages = 1000;
        int intermediary = 10;
        boolean normalize = false;
        boolean quadratic = false;
        boolean dislocated = false;
        boolean balance = false;
        boolean disableRandomness = true;

        classificationTrain(ni, ages, intermediary, normalize, quadratic, dislocated, balance, disableRandomness);

    }

    public static void classificationTrain(double ni, int ages, int intermediary, boolean normalizated, boolean quadratic, boolean dislocated, boolean balance, boolean disableRandomness){
        ArrayList<Sample>[] bases = null;
        ArrayList<Sample> trainBase = null;
        ArrayList<Sample> testBase = null;


        try{
            bases =  new CarEvaluationReader().getDevidedBase(balance, disableRandomness);
            trainBase = bases[0];
            testBase = bases[1];
        }catch(FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado");
        }catch(IOException e) {
            System.out.println("Falha na leitura do arquivo");
        }

        if(bases==null) {
            System.out.println("Encerrando...");
            System.exit(0);;
        }

        FileWriter trainCData = null;
        FileWriter testCData = null;
        FileWriter trainPData = null;
        FileWriter testPData = null;
        try {
            trainCData = new FileWriter("classificationTrain.dat");
            testCData = new FileWriter("classificationTest.dat");
            trainPData = new FileWriter("proximityTrain.dat");
            testPData = new FileWriter("proximityTest.dat");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter trainCWriter = new BufferedWriter(trainCData);
        BufferedWriter testCWriter = new BufferedWriter(testCData);
        BufferedWriter trainPWriter = new BufferedWriter(trainPData);
        BufferedWriter testPWriter = new BufferedWriter(testPData);

        if(normalizated){
            ArrayList<Sample> allBase = new ArrayList<>();
            allBase.addAll(trainBase);
            allBase.addAll(testBase);
            normalize(allBase);
        }

        MLP perceptron = new MLP(ni, trainBase.get(0).getInDimension(), testBase.get(0).getOutDimension(), intermediary, disableRandomness);
        double ageTrainCError = 0;
        double ageTestCError = 0;
        double ageTrainPError = 0;
        double ageTestPError = 0;
        double sampleCError = 0;
        double samplePError = 0;
        double missHit;

        for(int i=0;i<ages;i++) {

            ageTrainCError=0;
            ageTestCError=0;
            ageTrainPError=0;
            ageTestPError=0;

            for(int j=0; j<trainBase.size();j++) {

                sampleCError=0;
                samplePError=0;
                missHit = 0;

                double[] output = perceptron.learn(trainBase.get(j).getInput(), trainBase.get(j).getOutput(), quadratic);

                for(int o=0;o<output.length;o++) {
                    missHit += Math.abs(getValueTrashold(output[o], 0.5) - trainBase.get(j).getOutput()[o]);
                    if(quadratic){
                        samplePError+= Math.pow(output[o]-trainBase.get(j).getOutput()[o], 2);
                    }else{
                        samplePError+= Math.abs(output[o]-trainBase.get(j).getOutput()[o]);
                    }
                }

                if(missHit>0){
                    sampleCError = 1;
                }else{
                    sampleCError = 0;
                }
                ageTrainCError+=sampleCError;
                ageTrainPError+=samplePError;
            }
            for(int j=0; j<testBase.size();j++) {

                sampleCError=0;
                samplePError=0;
                missHit = 0;

                double[] output = perceptron.exec(testBase.get(j).getInput(), testBase.get(j).getOutput());

                for(int o=0;o<output.length;o++) {
                    missHit += Math.abs(getValueTrashold(output[o], 0.5) - trainBase.get(j).getOutput()[o]);
                    if(quadratic){
                        samplePError+= Math.pow(output[o]-testBase.get(j).getOutput()[o], 2);
                    }else{
                        samplePError+= Math.abs(output[o]-testBase.get(j).getOutput()[o]);
                    }
                }

                if(missHit>0){
                    sampleCError = 1;
                }else{
                    sampleCError = 0;
                }

                ageTestCError+=sampleCError;
                ageTestPError+=samplePError;
            }
            try {
                trainCWriter.write(i + "\t" + ageTrainCError / trainBase.size());
                trainCWriter.newLine();
                testCWriter.write(i + "\t" + ageTestCError / testBase.size());
                testCWriter.newLine();
                trainPWriter.write(i + "\t" + ageTrainPError / trainBase.size());
                trainPWriter.newLine();
                testPWriter.write(i + "\t" + ageTestPError / testBase.size());
                testPWriter.newLine();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println(i+"  -   "+ageTrainPError);
            System.out.println(i+"  -   "+ageTestPError);
        }

        try {
            testCWriter.flush();
            trainCWriter.flush();
            testPWriter.flush();
            trainPWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public static void defaultTrain ( String basePath, double ni, int ages, int intermediary){

        ArrayList<Sample> base = null;

        try{
            base =  new CarEvaluationReader().getBase();
        }catch(FileNotFoundException e) {
            System.out.println("Arquivo nao encontrado");
        }catch(IOException e) {
            System.out.println("Falha na leitura do arquivo");
        }

        if(base==null) {
            System.out.println("Encerrando...");
            System.exit(0);;
        }

        MLP perceptron = new MLP(ni, base.get(0).getInDimension(), base.get(0).getOutDimension(), intermediary, false);
        double ageError = 0;
        double sampleError = 0;

        for(int i=0;i<ages;i++) {
            ageError=0;
            for(int j=0; j<base.size();j++) {
                sampleError=0;
                double[] output = perceptron.learn(base.get(j).getInput(), base.get(j).getOutput(), true);
                for(int o=0;o<output.length;o++) {
                    sampleError+= Math.abs(output[o]-base.get(j).getOutput()[o]);
                }
                ageError+=sampleError;
            }
            System.out.println("Época "+i+":    "+ageError);
        }

        int soma=0;
        for(int j=0; j<base.size();j++) {

            double[] output = perceptron.exec(base.get(j).getInput(), base.get(j).getOutput());
            for(int i=0;i<base.get(j).getInput().length;i++) {
                System.out.print(base.get(j).getInput()[i]+"    ");
            }
            System.out.print("| ");
            for(int o=0;o<base.get(j).getOutput().length;o++) {
                System.out.print(base.get(j).getOutput()[o]+" ");
            }
            System.out.print("| ");
            boolean hit=true;
            for(int o=0;o<output.length;o++) {
                System.out.print(output[o]+" ");
                if(Math.round(output[o])!=base.get(j).getOutput()[o]) {
                    hit=false;
                }

            }
            if(hit) {
                soma++;
            }
            System.out.println("   HIT- " + hit);
            System.out.println();
        }

        System.out.println("Porcentagem de acerto: "+((double)soma/base.size())*100+"%");
        System.out.println("Erro da ultima época:    "+ageError);

    }


    public static void normalize (ArrayList<Sample> allBase){
        for(int j=0; j<allBase.get(0).getInDimension(); j++){
            double max = allBase.get(0).getInput()[j];
            double min = allBase.get(0).getInput()[j];
            for(int i=1; i<allBase.size(); i++){
                if(max < allBase.get(i).getInput()[j]){
                    max = allBase.get(i).getInput()[j];
                }
                if(min > allBase.get(i).getInput()[j]){
                    min = allBase.get(i).getInput()[j];
                }
            }
            for(int i=1; i<allBase.size(); i++) {
                allBase.get(i).getInput()[j] = allBase.get(i).getInput()[j] - min / (max-min);
            }
        }
    }


    public static int getValueTrashold (double value, double trashold){

        if(value>trashold){
            return 1;
        }else{
            return 0;
        }

    }




}
