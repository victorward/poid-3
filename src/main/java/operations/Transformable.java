package operations;

import model.WavFile;

public interface Transformable {
    WavFile process(WavFile wavFile);

    void setChunkSize(int chunkSize);
}
