package operations;

import model.WavFile;

import java.util.ArrayList;
import java.util.List;

import static utils.SoundUtil.chunkArrayInt;
import static utils.SoundUtil.generateSound;

public class Autocorrelation implements Transformable {

    private int chunkSize = 44100;

    @Override
    public WavFile process(WavFile wavFile) {
        System.out.println("Starting autocorrelation");
        int numberOfFrames = (int) wavFile.getNumFrames();
        int sampleRate = (int) wavFile.getSampleRate();
        int[] bufferWav = new int[numberOfFrames];
        System.out.println("Sample rate: " + sampleRate);
        System.out.println("Number of frames: " + numberOfFrames);
        System.out.println("Chunk size: " + chunkSize);
        String name = wavFile.getName();
        try {
            wavFile.readFrames(bufferWav, numberOfFrames);
            wavFile.close();
        } catch (Exception e) {
            System.out.println("===ERROR===");
            System.out.println("Unexpected error has occurred when reading frames from sound");
            e.printStackTrace();
            System.out.println("===ERROR===");
        }

        int[][] parts = chunkArrayInt(bufferWav, chunkSize);

        List<Integer> frequencies = new ArrayList<>();

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

            System.out.println("Global maximum: " + autocorrelation[0]);
            System.out.println("Frequency: " + frequency);


            frequencies.add(frequency);
        }
        System.out.println("Autocorrelation has finished");
        WavFile generatedSound = generateSound(chunkSize, numberOfFrames, sampleRate, name, frequencies);

        return null;
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
