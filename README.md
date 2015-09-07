# EnumJ

Composable enumerators with LINQ-style operators and full compatibility with Java 8 lambda expressions, streams and spliterators.

__EnumJ__ contains two main data types: _enumerators_ and _enumerables_. Enumerators are powerful iterators enhanced with massive scalability and LINQ-style operators. Enumerables are powerful iterables that have also the scalability and compositionality of enumerators.

For the JavaDoc documentation of __EnumJ__ click [here](http://mariusfilip.bitbucket.org/apidocs/index.html).

## Enumerators

Enumerators are Iterator objects with the following characteristics:

 * __High composability:__ some operations can be composed iteratively
 * __Shareability:__ shareable enumerators may participate in multiple pipelines, processed independently
 * __Fault tolerance:__ fault-tolerant enumerators allow for exceptions in the middle of the pipeline without stopping the whole computation
 * __Lazy evaluations:__ enumerators allow for lazy evaluation in both returned values and input sequences
 * __Choice composition and zipping:__ construction of enumerators from multiple sub-sequences by choosing or zipping
 * __Compatibility and fluent syntax:__ enumerators support fluent syntax like Stream classes and are compatible with most major collection and sequence constructions: Iterator, Iterable, Enumeration, Stream, Spliterator and Supplier of elements.

### High composability

The following operations can be composed iteratively many times and the resulting enumerator will not overflow the stack when enumerating:

 * as(java.lang.Class)
 * asFiltered(java.lang.Class)
 * asOptional()
 * append(java.lang.Object...)
 * concatOn(java.lang.Object...)
 * concat(java.util.Enumeration)
 * concat(java.lang.Iterable)
 * concat(java.util.Iterator)
 * concat(java.util.Spliterator)
 * concat(java.util.stream.Stream)
 * concat(java.util.function.Supplier)
 * filter(java.util.function.Predicate)
 * flatMap(java.util.function.Function)
 * limit(long)
 * limitWhile(java.util.function.Predicate)
 * map(java.util.function.Function)
 * map(java.util.function.BiFunction)
 * peek(java.util.function.Consumer)
 * prependOn(java.lang.Object...)
 * prepend(java.util.Enumeration)
 * prepend(java.lang.Iterable)
 * prepend(java.util.Iterator)
 * prepend(java.util.Spliterator)
 * prepend(java.util.stream.Stream)
 * prepend(java.util.function.Supplier)
 * repeatEach(int)
 * skip(long)
 * skipWhile(java.util.function.Predicate)
 * take(long)
 * takeWhile(java.util.function.Predicate)
 * zipAll(java.util.Iterator, java.util.Iterator...)
 * zipAny(java.util.Iterator)
 * zipBoth(java.util.Iterator)
 * zipLeft(java.util.Iterator)
 * zipRight(java.util.Iterator)

### Shareability

ShareableEnumerator can spawn instances of SharingEnumerator which in turn can share the same sequence without traversing it more than once. asShareable() converts any enumerator into a ShareableEnumerator.

### Fault tolerance

Fault-tolerant enumerators accept an error handler which is being called whenever the tryPipelineOut of enumerating throws an exception. The error handler consumes the error and the pipeline doesn't stop. asTolerant(java.util.function.Consumer) converts any enumerator into a fault-tolerant enumerator.

### Lazy evaluations

The following calls allow lazy specification of input sequences:

 * ofLazyEnumeration(java.util.function.Supplier)
 * ofLazyIterable(java.util.function.Supplier)
 * ofLazyIterator(java.util.function.Supplier)
 * ofLazySpliterator(java.util.function.Supplier)
 * ofLazyStream(java.util.function.Supplier)

### Choice composition and zipping

Enumerators can be constructed of sub-sequences by choosing elements from them:

 * choiceOf(java.util.function.IntSupplier, java.util.Iterator, java.util.Iterator, java.util.Iterator...)
 * choiceOf(java.util.function.IntSupplier, java.util.function.IntUnaryOperator, java.util.Iterator, java.util.Iterator, java.util.Iterator...)

Enumerators also have four zipping operations:

 * zipAny(java.util.Iterator)
 * zipBoth(java.util.Iterator)
 * zipLeft(java.util.Iterator)
 * zipRight(java.util.Iterator)
