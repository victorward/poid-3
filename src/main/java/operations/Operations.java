package operations;

import model.WavFile;

import java.util.ArrayList;
import java.util.List;

public class Operations {
    private List<Transformable> operations = new ArrayList<>();
    private WavFile resultSound;

    public Operations() {

    }

    public WavFile processSound(WavFile originSound) {
        for (Transformable transformable : operations) {
            resultSound = transformable.process(originSound);
        }
        return resultSound;
    }

    public void clear() {
        operations.clear();
    }

    public void addOperation(Transformable transformable) {
        operations.add(transformable);
    }

    public void removeOperation(Transformable transformable) {
        operations.remove(transformable);
    }

    public List<Transformable> getOperations() {
        return operations;
    }
}
