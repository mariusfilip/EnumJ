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
final class ForkedElement<E> {
    private final E value;

    private boolean leftHas;
    private boolean rightHas;
    private ForkedElement<E> next;

    public ForkedElement(E value) {
        this.value = value;
    }
    
    public E getValue(boolean left) {
        leftHas |= left;
        rightHas |= !left;
        return value;
    }
    
    public boolean leftHas() {
        return this.leftHas;
    }
    
    public boolean rightHas() {
        return this.rightHas;
    }

    public ForkedElement<E> getNext() {
        return this.next;
    }

    public void setNext(ForkedElement<E> next) {
        assert next != null;
        assert this.next == null;
        this.next = next;
    }
}
