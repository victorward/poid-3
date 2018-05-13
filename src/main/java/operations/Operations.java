package operations;

import model.WavFile;

import java.util.ArrayList;
import java.util.List;

public class Operations {
    private List<Transformable> operations = new ArrayList<>();
    private StringBuilder results;

    public Operations() {

    }

    public StringBuilder processSound(WavFile originSound) {
        for (Transformable transformable : operations) {
            results = transformable.process(originSound);
        }
        return results;
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
