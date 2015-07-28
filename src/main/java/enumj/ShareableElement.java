/*
 * The MIT License
 *
 * Copyright 2015 Marius Filip.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package enumj;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

class ShareableElement<E> {

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

    public E getValue() {
        return this.value;
    }

    void addRef(SharingEnumerator<E> owner) {
        addSharingOwner(owner);
    }

    public void release(SharingEnumerator<E> owner) {
        removeSharingOwner(owner);
    }

    public boolean isFree() {
        return waiting.isEmpty() && sharingOwners.isEmpty();
    }

    // ---------------------------------------------------------------------- //

    protected void addSharingOwner(SharingEnumerator<E> owner) {
        sharingOwners.add(owner);
    }

    protected void removeSharingOwner(SharingEnumerator<E> owner) {
        sharingOwners.remove(owner);
    }
}
