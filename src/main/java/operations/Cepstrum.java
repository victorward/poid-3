package operations;

import model.Complex;
import model.WavFile;
import utils.FourierTransformUtil;

import java.util.*;

import static utils.SoundUtil.chunkArray;
import static utils.SoundUtil.generateAndSaveSound;

public class Cepstrum implements Transformable {
    private int chunkSize = 44100;
    private double[][] d;

    static public class DataComp implements Comparator<Integer> {
        double[] d;

        DataComp(double[] d) {
            this.d = d;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            return 0;
        }
    }

    static public class MaxDataComp extends DataComp {
        MaxDataComp(double[] d) {
            super(d);
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            return (d[o1] > d[o2]) ? 1 : -1;
        }
    }

    @Override
    public StringBuilder process(WavFile wavFile) {
        StringBuilder out = new StringBuilder();
        int numberOfFrames = (int) wavFile.getNumFrames();
        int sampleRate = (int) wavFile.getSampleRate();
        String name = wavFile.getName();
        int windowWidth = (int) wavFile.getNumFrames();
        double[][] parts = new double[1][];
        int N = makePowerOf2(windowWidth);

        out.append("Signal length - ").append(numberOfFrames).append(newline);
        out.append("Sample rate - ").append(sampleRate).append(newline);
        out.append("Frames size - ").append(chunkSize).append(newline);

        if (windowWidth == 44100) {
            double[] buffer = new double[N];
            readFramesFromWav(wavFile, N, buffer);
            parts[0] = buffer;
        } else {
            double[] buffer = new double[numberOfFrames];
            readFramesFromWav(wavFile, numberOfFrames, buffer);
            chunkSize = makePowerOf2(chunkSize);
            N = chunkSize;
            parts = chunkArray(buffer, chunkSize);
        }
        out.append("Frames size with power of 2 - ").append(chunkSize).append(newline);

        List<Integer> frequencies = new ArrayList<>();

        out.append("Frames count - ").append(parts.length).append(newline).append(newline);
        int counter = 0;
        for (double[] buffer : parts) {
            counter++;
            Complex[] complexSound = transformSignalToComplex(buffer);


            double arg = (2 * Math.PI) / ((double) N - 1.0);

            for (int i = 0; i < N; ++i)
//              hammming window
                complexSound[i] = new Complex(buffer[i] * (0.54 - 0.46 * Math.cos(arg * (double) i)), 0);

            complexSound = FourierTransformUtil.dif1d(complexSound);

//          complexSound = Arrays.copyOfRange(complexSound, 0, N / 2);
//          cepstrum rzeczywiste i zespolone
            for (int i = 0; i < complexSound.length; ++i)
                complexSound[i] = new Complex(10.0 * Math.log10(complexSound[i].abs() + 1), 0);

            complexSound = FourierTransformUtil.dif1d(complexSound);
            complexSound = Arrays.copyOfRange(complexSound, 0, N / 4);

            d = new double[2][N];

            for (int i = 0; i < complexSound.length; ++i) {
                //power cepstrum
                //d[0][i]=Math.pow(csignal[i].abs(),2);
                d[0][i] = complexSound[i].abs();
            }

            double[] dd = d[0];
            LinkedList<Integer> pperiod = new LinkedList<>();

            //RANGE
            int range = 10;

//		System.out.println("RANGE"+range);
            for (int i = range; i < dd.length - range; ++i) {
                int bigger = 0;
                //sprawdz czy jest to ,,dolina o zboczu wysokim na ,,range''
                //sprawdzamy wysokość, ale nie stromość zbocza - peaki są ostre
                for (int j = i - range; j < i + range; ++j) {
                    if (dd[j] <= dd[i] && i != j)
                        bigger++;
                }
                //sprawdz czy zbocza sa tak wysokie jak to zalozylismy
                if (bigger == (range * 2) - 1) {
                    pperiod.add(i);
                }
            }

            //odrzucanie wysokich ale peakow ale nie stromych
            //musza opadac w obu kierunkach - nisko
            for (ListIterator<Integer> iter = pperiod.listIterator(); iter.hasNext(); ) {
                int i = iter.next(), j = 0, k = 0;
                //szukamy najniższego wartosci na zboczu lewym
                while (i - j - 1 >= 0) {
                    if ((dd[i - j - 1] <= dd[i - j]))
                        ++j;
                    else
                        break;
                }
                //szukamy najnizszej wartosci na zboczu prawym
                while (((i + k + 1) < dd.length)) {
                    if ((dd[i + k + 1] <= dd[i + k]))
                        ++k;
                    else
                        break;
                }


                double maxmin = Math.max(dd[i - j], dd[i + k]);
                if (maxmin > dd[i] * 0.2) {
                    iter.remove();
//				System.out.println(i+ "+"+j+ " " + dd[i-j]+" "+dd[i]+" "+dd[i+k]);
                } else d[1][i] = dd[i];

            }
            //progowanie co do największego peaku
            int max_ind = Collections.max(pperiod, new MaxDataComp(dd));

            for (ListIterator<Integer> it = pperiod.listIterator(); it.hasNext(); ) {
                Integer num = it.next();
                if (dd[num] > dd[max_ind] * 0.4) {
                    d[1][num] = dd[num];
                } else
                    it.remove();
            }

            int max_b, max_a;
            max_b = Collections.max(pperiod, new MaxDataComp(dd));
            int a = 0, b = 0;
            while (pperiod.size() > 1) {
                for (ListIterator del = pperiod.listIterator(); del.hasNext(); )
                    if ((Integer) del.next() == max_b) {
                        del.remove();
                        break;
                    }

                max_a = Collections.max(pperiod, new MaxDataComp(dd));

                a = max_a;
                b = max_b;
                if (a > b) {
                    int tmp = a;
                    a = b;
                    b = tmp;
                }
//                System.out.println(a + " " + b);


                for (ListIterator<Integer> it = pperiod.listIterator(); it.hasNext(); ) {
                    Integer num = it.next();
                    if (num < a || num > b)
                        it.remove();
                }

                max_b = max_a;

            }
            max_ind = Math.abs(b - a);
            if (max_ind == 0 && pperiod.size() == 1)
                max_ind = pperiod.get(0);
//            else
//                System.out.println(a + " " + b);


            int frequency = (int) (sampleRate / (double) max_ind);
            out.append("Frame ").append(counter).append(" has frequency - ").append(frequency).append(" Hz at ").append(max_ind).append(newline);
            frequencies.add(frequency);

        }
        out.append(newline).append("Average frequency - ").append(averageFrequency(frequencies)).append(" Hz").append(newline).append(newline);
        generateAndSaveSound(chunkSize, numberOfFrames, sampleRate, name, frequencies);
        out.append("Cepstrum analysis has finished");
        return out;
    }

    private void readFramesFromWav(WavFile wavFile, int n, double[] buffer) {
        try {
            wavFile.readFrames(buffer, n);
            wavFile.close();
        } catch (Exception e) {
            System.out.println("Unexpected error has occurred when reading frames from sound");
            e.printStackTrace();
        }
    }

    @Override
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    private int makePowerOf2(int windowWidth) {
        int powerOfTwo = 2;

        while (windowWidth > powerOfTwo) {
            powerOfTwo *= 2;
        }

        return powerOfTwo;
    }

    private Complex[] transformSignalToComplex(double[] buffer) {
        int size = buffer.length;
        Complex[] complex = new Complex[size];

        for (int i = 0; i < size; i++) {
            complex[i] = new Complex(buffer[i], 0.0d);
        }

        return complex;
    }

}
