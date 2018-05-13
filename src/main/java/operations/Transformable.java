package operations;

import model.WavFile;

import java.util.List;

public interface Transformable {
    public static String newline = System.getProperty("line.separator");

    StringBuilder process(WavFile wavFile);

    void setChunkSize(int chunkSize);

    default Integer averageFrequency(List<Integer> frequencies) {
        int sum = 0;
        for (Integer frequency : frequencies) {
            sum += frequency;
        }
        return sum / frequencies.size();
    }
}
