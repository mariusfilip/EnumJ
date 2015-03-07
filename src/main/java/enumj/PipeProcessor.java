/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

/**
 *
 * @author Marius Filip
 */
abstract class PipeProcessor<T,R> {
    abstract void process(T value);
    abstract boolean hasValue();
    abstract R getValue();
    abstract boolean continueOnNoValue();
}
