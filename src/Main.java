import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static Individuo cruzamento(Individuo pai, Individuo mae, double taxaMutacao) {

        BitSet gene1 = new BitSet();
        BitSet gene2 = new BitSet();

        for (int i = 0; i < 18; i++) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                mutacao(pai, taxaMutacao, gene1, gene2, i);
            } else{
                mutacao(mae, taxaMutacao, gene1, gene2, i);
            }
        }

        return new Individuo(gene1, gene2);
    }

    public static void mutacao(Individuo progenitor, double taxaMutacao, BitSet gene1, BitSet gene2, int i) {
        if (ThreadLocalRandom.current().nextDouble(100) < taxaMutacao) {
            gene1.set(i, !progenitor.getGene1().get(i));
            gene2.set(i, !progenitor.getGene2().get(i));
        }
        else {
            gene1.set(i, progenitor.getGene1().get(i));
            gene2.set(i, progenitor.getGene2().get(i));
        }
    }

    public static int escolherProgenitor(int tamPopulacao){
        int prob = ThreadLocalRandom.current().nextInt(0,1+(tamPopulacao*((tamPopulacao+1)/2)));

        for (int i = tamPopulacao, j = 0, soma = 0; i >= 0; i--, j++) {
            soma += i;
            if (soma > prob)
                return j;
        }
        return 0;
    }

    public static void run (FileWriter arquivo, int tamPopulacao, int numGeracao, double taxaMutacao, int execucao) throws IOException {
        double[] media = new double[numGeracao];

        ArrayList<ArrayList<Individuo>> individuos = new ArrayList<>(numGeracao);
        ArrayList<Individuo> primeiraGeracao = new ArrayList<>(tamPopulacao);


        //primeira geração

        double soma = 0;
        for (int i = 0; i < tamPopulacao; i++) {
            double x1 = ThreadLocalRandom.current().nextInt(-100000, 100001) / 10000.0;
            double x2 = ThreadLocalRandom.current().nextInt(-100000, 100001) / 10000.0;
            primeiraGeracao.add(new Individuo(x1, x2));
            soma += primeiraGeracao.get(i).getFitness();
        }
        media[0] = soma / tamPopulacao;

        primeiraGeracao.sort(Individuo::compareTo);

        individuos.add(primeiraGeracao);

        //outras gerações

        for (int i = 1; i < numGeracao; i++) {
            ArrayList<Individuo> geracaoIndividuos = new ArrayList<>(tamPopulacao);
            geracaoIndividuos.add(individuos.get(i - 1).get(0));

            soma = 0;

            for (int j = 1; j < tamPopulacao; j++) {

                int nMae, nPai = escolherProgenitor(tamPopulacao);

                do {
                    nMae = escolherProgenitor(tamPopulacao);
                } while (nMae == nPai);

                geracaoIndividuos.add(cruzamento(individuos.get(i - 1).get(nPai), individuos.get(i - 1).get(nMae), taxaMutacao));

                soma += geracaoIndividuos.get(j).getFitness();
            }
            media[i] = soma / tamPopulacao;
            geracaoIndividuos.sort(Individuo::compareTo);
            individuos.add(geracaoIndividuos);
        }

        for (int i = 0; i < numGeracao; i++) {
            System.out.println("Geração: "+(i+1)+" Melhor: " + individuos.get(i).get(0).getFitness() + "  Média: " + media[i] + "  Pior: " + individuos.get(i).get(tamPopulacao - 1).getFitness());
            arquivo.write(execucao+" "+i+" "+individuos.get(i).get(0).getFitness() + " " + media[i] + " " + individuos.get(i).get(tamPopulacao - 1).getFitness() + "\n");
        }
    }

    public static void main(String[] args) {
        try {
            FileWriter arquivo = new FileWriter("arquivo.txt"); //arquivo para gerar o gráfico no matlab
            Scanner scn = new Scanner(System.in);

            System.out.print("Insira o tamanho da população: ");
            int tamPopulacao = scn.nextInt();

            System.out.print("Insira o número de gerações: ");
            int numGeracao = scn.nextInt();

            System.out.print("Insira a taxa de mutação % (0 - 100): ");
            double taxaMutacao = scn.nextDouble();

            System.out.print("Insira o número de vezes para executar: ");
            int exec = scn.nextInt();

            for (int execucao = 1; execucao <= exec; execucao++) {
                System.out.println("\nExecução "+execucao+"\n");
                run(arquivo,tamPopulacao,numGeracao,taxaMutacao,execucao);
            }

            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}