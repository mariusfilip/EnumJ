/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Marius Filip
 */
class BufferedPipeProcessor<E> extends PipeProcessor<E,List<E>> {
    protected LinkedList<E> buffer;
    protected int minSize;
    protected int maxSize;

    public BufferedPipeProcessor(int minSize, int maxSize) {
        Utils.ensureNonNegative(maxSize-minSize,
                                Messages.NegativeEnumeratorSize);
        buffer = new LinkedList<>();
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    public void process(E value) {
        buffer.add(value);
        if (buffer.size() > maxSize) {
            buffer.remove();
        }
    }
    @Override
    public boolean hasValue() {
        return minSize <= buffer.size() && buffer.size() <= maxSize;
    }
    @Override
    public List<E> getValue() {
        return Collections.unmodifiableList(buffer);
    }
    @Override
    public boolean mayContinue() {
        return true;
    }
}
