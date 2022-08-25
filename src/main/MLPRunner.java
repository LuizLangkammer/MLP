package main;

import fileReader.MyInputReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MLPRunner {


    public static void main (String[] args) {

        //Parameters
        String basePath = "robo-base.txt";
        double ni = 0.001;
        int ages = 100000;
        int intermediary = 3;

        ArrayList<Sample> base = null;
        try{
            base = new MyInputReader().getBase(basePath);
        }catch(FileNotFoundException e) {
            System.out.println("Arquivo n�o encontrado");
        }catch(IOException e) {
            System.out.println("Falha na leitura do arquivo");
        }

        if(base==null) {
            System.out.println("Encerrando...");
            System.exit(0);;
        }

        MLP perceptron = new MLP(ni,base.get(0).getInDimension(),base.get(0).getOutDimension(), intermediary);

    }


}
