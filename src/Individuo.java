import java.util.BitSet;

public class Individuo implements Comparable<Individuo>{
    private double x1, x2, fitness;
    private BitSet gene1, gene2;

    public Individuo(double x1, double x2) {
        this.x1 = x1;
        this.x2 = x2;

        this.gene1 = decimalBinario((long) ((x1+10)*10000));
        this.gene2 = decimalBinario((long) ((x2+10)*10000));

        this.fitness = CalcFitness(x1, x2);
    }

    public Individuo(BitSet gene1, BitSet gene2) {
        this.gene1 = gene1;
        this.gene2 = gene2;

        this.x1 = (binarioDecimal(gene1)/10000.0)-10.0;
        this.x2 = (binarioDecimal(gene2)/10000.0)-10.0;

        this.fitness = CalcFitness(x1, x2);

    }

    private static BitSet decimalBinario(long decimal) {
        BitSet binario = new BitSet();
        int i = 0;
        while (decimal > 0) {
            binario.set(i++, decimal % 2 == 1);
            decimal/=2;
        }
        return binario;
    }

    public static long binarioDecimal(BitSet binario) {
        long decimal = 0;
        for (int i = 0; i < binario.length(); i++) {
            decimal += binario.get(i) ? (1L << i) : 0L;
        }
        return decimal;
    }

    private static double CalcFitness(double x1, double x2) {
        double somatorio1 = 0.0, somatorio2 = 0.0;

        for (int i = 1; i <= 5; i++) {
            somatorio1 += i * Math.cos((i+1)*x1+i);
            somatorio2 += i * Math.cos((i+1)*x2+i);
        }
        return somatorio1 * somatorio2;
    }

    public double getFitness() {
        return fitness;
    }

    public BitSet getGene1() {
        return gene1;
    }

    public BitSet getGene2() {
        return gene2;
    }

    @Override
    public int compareTo(Individuo individuo) {
        return (int) ((this.getFitness()-individuo.getFitness())*100000);
    }
}
