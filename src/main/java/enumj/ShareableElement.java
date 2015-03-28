/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

/**
 *
 * @author Marius Filip
 */
final class ShareableElement<E> {

    private final E value;
    private Set<SharingEnumerator<E>> sharingOwners;
    private WeakHashMap<SharingEnumerator<E>, ShareableElement<E>> waiting;

    ShareableElement<E> next;

    public ShareableElement(E value,
                            WeakHashMap<SharingEnumerator<E>,
                                        ShareableElement<E>> waiting) {
        assert waiting != null;
        this.value = value;
        this.sharingOwners = new HashSet<>();
        this.waiting = waiting;
    }

    E getValue() {
        return this.value;
    }

    void addRef(SharingEnumerator<E> owner) {
        assert owner != null;
        sharingOwners.add(owner);
    }

    public void release(SharingEnumerator<E> owner) {
        assert owner != null;
        assert sharingOwners.contains(owner);
        sharingOwners.remove(owner);
    }

    public boolean isFree() {
        return waiting.isEmpty() && sharingOwners.isEmpty();
    }
}