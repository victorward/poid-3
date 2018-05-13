package operations;

import model.WavFile;

import java.util.ArrayList;
import java.util.List;

import static utils.SoundUtil.chunkArrayInt;
import static utils.SoundUtil.generateAndSaveSound;

public class Autocorrelation implements Transformable {

    private int chunkSize = 44100;

    @Override
    public StringBuilder process(WavFile wavFile) {
        StringBuilder out = new StringBuilder();
        int numberOfFrames = (int) wavFile.getNumFrames();
        int sampleRate = (int) wavFile.getSampleRate();
        int[] bufferWav = new int[numberOfFrames];

        out.append("Signal length - ").append(numberOfFrames).append(newline);
        out.append("Sample rate - ").append(sampleRate).append(newline);
        out.append("Frames size - ").append(chunkSize).append(newline);

        String name = wavFile.getName();
        try {
            wavFile.readFrames(bufferWav, numberOfFrames);
            wavFile.close();
        } catch (Exception e) {
            System.err.println("===ERROR===");
            System.out.println("Unexpected error has occurred when reading frames from sound");
            e.printStackTrace();
            System.err.println("===ERROR===");
        }

        int[][] parts = chunkArrayInt(bufferWav, chunkSize);

        List<Integer> frequencies = new ArrayList<>();

        out.append("Frames count - ").append(parts.length).append(newline).append(newline);

        int counter = 0;
        for (int[] buffer : parts) {
            long[] autocorrelation = new long[chunkSize];

            for (int m = 1; m < autocorrelation.length; m++) {
                long sum = 0;
                for (int n = 0; n < autocorrelation.length - m; n++) {
                    sum += buffer[n] * buffer[n + m];
                }
                autocorrelation[m - 1] = sum;
            }

            long localMaxIndex = findLocalMax(autocorrelation);
            int frequency = (int) (sampleRate / localMaxIndex);

            out.append("Frame ").append(counter).append(" has frequency - ").append(frequency).append(" Hz").append(newline);
            out.append("Global maximum: ").append(autocorrelation[0]).append(newline);
            frequencies.add(frequency);
            counter++;
        }
        out.append(newline).append("Average frequency - ").append(averageFrequency(frequencies)).append(" Hz").append(newline).append(newline);
        generateAndSaveSound(chunkSize, numberOfFrames, sampleRate, name, frequencies);
        out.append("Autocorrelation has finished");
        return out;
    }

    private static long findLocalMax(long[] autocorrelation) {
        double globalMax = autocorrelation[0];

        double tempLocalMax = 0;
        int localMaxIndex = Integer.MAX_VALUE;
        double localMin = globalMax;

        boolean falling = true;
        for (int i = 1; i < autocorrelation.length; i++) {
            double cur = autocorrelation[i];
            if (falling) {
                if (cur < localMin) {
                    localMin = cur;
                } else if ((globalMax - localMin) * 0.95 > globalMax - cur) {
                    falling = false;
                    tempLocalMax = cur;
                }
            } else {
                if (cur > tempLocalMax) {
                    tempLocalMax = cur;
                    localMaxIndex = i;
                } else if (1.05 * tempLocalMax > globalMax) {
                    return localMaxIndex;
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    @Override
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
}
