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
        int ages = 10000;
        int intermediary = 10;

        proximityTrain(basePath, ni, ages, intermediary);
        classificationTrain(basePath, ni, ages, intermediary);

    }

    public static void classificationTrain(String basePath, double ni, int ages, int intermediary){
        ArrayList<Sample>[] bases = null;
        ArrayList<Sample> trainBase = null;
        ArrayList<Sample> testBase = null;


        try{
            bases =  new CarEvaluationReader().getDevidedBase();
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
        FileWriter trainData = null;
        FileWriter testData = null;
        try {
            trainData = new FileWriter("classificationTrain.dat");
            testData = new FileWriter("classificationTest.dat");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter trainWriter = new BufferedWriter(trainData);
        BufferedWriter testWriter = new BufferedWriter(testData);

        MLP perceptron = new MLP(ni, trainBase.get(0).getInDimension(), testBase.get(0).getOutDimension(), intermediary);
        double ageTrainError = 0;
        double ageTestError = 0;
        double sampleError = 0;

        for(int i=0;i<ages;i++) {
            ageTrainError=0;
            ageTestError=0;
            for(int j=0; j<trainBase.size();j++) {
                sampleError=0;
                double[] output = perceptron.learn(trainBase.get(j).getInput(), trainBase.get(j).getOutput());
                for(int o=0;o<output.length;o++) {
                    sampleError+= Math.abs(output[o]-trainBase.get(j).getOutput()[o]);
                }
                ageTrainError+=sampleError;
            }
            for(int j=0; j<testBase.size();j++) {
                sampleError=0;
                double[] output = perceptron.learn(testBase.get(j).getInput(), testBase.get(j).getOutput());
                for(int o=0;o<output.length;o++) {
                    sampleError+= Math.abs(output[o]-testBase.get(j).getOutput()[o]);
                }
                ageTestError+=sampleError;
            }
            try {
                trainWriter.write(i + "\t" + ageTrainError / trainBase.size());
                trainWriter.newLine();
                testWriter.write(i + "\t" + ageTestError / testBase.size());
                testWriter.newLine();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println(i);
        }

        try {
            testWriter.flush();
            trainWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void proximityTrain(String basePath, double ni, int ages, int intermediary){
        ArrayList<Sample>[] bases = null;
        ArrayList<Sample> trainBase = null;
        ArrayList<Sample> testBase = null;


        try{
            bases =  new CarEvaluationReader().getDevidedBase();
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

        FileWriter trainData = null;
        FileWriter testData = null;
        try {
            trainData = new FileWriter("proximityTrain.dat");
            testData = new FileWriter("proximityTest.dat");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedWriter trainWriter = new BufferedWriter(trainData);
        BufferedWriter testWriter = new BufferedWriter(testData);


        MLP perceptron = new MLP(ni, trainBase.get(0).getInDimension(), testBase.get(0).getOutDimension(), intermediary);
        double ageTrainError = 0;
        double ageTestError = 0;
        double sampleError = 0;
        int missHit = 0;

        for(int i=0;i<ages;i++) {
            ageTrainError=0;
            ageTestError=0;

            for(int j=0; j<trainBase.size();j++) {
                sampleError=0;
                double[] output = perceptron.learn(trainBase.get(j).getInput(), trainBase.get(j).getOutput());
                missHit = 0;
                double biggest = output[0];
                for(int o=0;o<output.length;o++) {
                    //missHit += Math.abs(getValueTrashold(output[o], 0.5) - trainBase.get(j).getOutput()[o]);
                    if(output[o]>biggest){
                        biggest = output[o];
                    }
                }
                for(int o=0;o<output.length;o++) {
                    if(output[o]==biggest){
                        output[o] = 1;
                        missHit += Math.abs(output[o]-trainBase.get(j).getOutput()[o]);
                    }else{
                        output[o] = 0;
                        missHit += Math.abs(output[o]-trainBase.get(j).getOutput()[o]);
                    }
                }

                if(missHit>0){
                    sampleError = 1;
                }else{
                    sampleError = 0;
                }
                ageTrainError+=sampleError;
            }
            for(int j=0; j<testBase.size();j++) {

                double[] output = perceptron.exec(testBase.get(j).getInput(), testBase.get(j).getOutput());
                missHit = 0;
                //for(int o=0;o<output.length;o++) {
                  //  missHit += Math.abs(getValueTrashold(output[o], 0.5) - testBase.get(j).getOutput()[o]);
                //}
                double biggest = output[0];
                for(int o=0;o<output.length;o++) {
                    //missHit += Math.abs(getValueTrashold(output[o], 0.5) - trainBase.get(j).getOutput()[o]);
                    if(output[o]>biggest){
                        biggest = output[o];
                    }
                }
                for(int o=0;o<output.length;o++) {
                    if(output[o]==biggest){
                        output[o] = 1;
                        missHit += Math.abs(output[o]-trainBase.get(j).getOutput()[o]);
                    }else{
                        output[o] = 0;
                        missHit += Math.abs(output[o]-trainBase.get(j).getOutput()[o]);
                    }
                }
                if(missHit>0){
                    sampleError = 1;
                }else{
                    sampleError = 0;
                }
                ageTestError+=sampleError;
            }

            try {
                trainWriter.write(i + "\t" + ageTrainError / trainBase.size());
                trainWriter.newLine();
                testWriter.write(i + "\t" + ageTestError / testBase.size());
                testWriter.newLine();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println(i);

        }
        try {
            testWriter.flush();
            trainWriter.flush();

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

        MLP perceptron = new MLP(ni, base.get(0).getInDimension(), base.get(0).getOutDimension(), intermediary);
        double ageError = 0;
        double sampleError = 0;

        for(int i=0;i<ages;i++) {
            ageError=0;
            for(int j=0; j<base.size();j++) {
                sampleError=0;
                double[] output = perceptron.learn(base.get(j).getInput(), base.get(j).getOutput());
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




    public static int getValueTrashold (double value, double trashold){

        if(value>trashold){
            return 1;
        }else{
            return 0;
        }

    }




}
