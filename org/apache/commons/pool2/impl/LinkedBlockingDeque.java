/*      */ package org.apache.commons.pool2.impl;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.time.Duration;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.Collection;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class LinkedBlockingDeque<E>
/*      */   extends AbstractQueue<E>
/*      */   implements Deque<E>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -387911632671998426L;
/*      */   private transient Node<E> first;
/*      */   private transient Node<E> last;
/*      */   private transient int count;
/*      */   private final int capacity;
/*      */   private final InterruptibleReentrantLock lock;
/*      */   private final Condition notEmpty;
/*      */   private final Condition notFull;
/*      */   
/*      */   private abstract class AbstractItr
/*      */     implements Iterator<E>
/*      */   {
/*      */     LinkedBlockingDeque.Node<E> next;
/*      */     E nextItem;
/*      */     private LinkedBlockingDeque.Node<E> lastRet;
/*      */     
/*      */     AbstractItr() {
/*  125 */       LinkedBlockingDeque.this.lock.lock();
/*      */       try {
/*  127 */         this.next = firstNode();
/*  128 */         this.nextItem = (this.next == null) ? null : this.next.item;
/*      */       } finally {
/*  130 */         LinkedBlockingDeque.this.lock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void advance() {
/*  138 */       LinkedBlockingDeque.this.lock.lock();
/*      */       
/*      */       try {
/*  141 */         this.next = succ(this.next);
/*  142 */         this.nextItem = (this.next == null) ? null : this.next.item;
/*      */       } finally {
/*  144 */         LinkedBlockingDeque.this.lock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract LinkedBlockingDeque.Node<E> firstNode();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  157 */       return (this.next != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E next() {
/*  162 */       if (this.next == null) {
/*  163 */         throw new NoSuchElementException();
/*      */       }
/*  165 */       this.lastRet = this.next;
/*  166 */       E x = this.nextItem;
/*  167 */       advance();
/*  168 */       return x;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract LinkedBlockingDeque.Node<E> nextNode(LinkedBlockingDeque.Node<E> param1Node);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void remove() {
/*  183 */       LinkedBlockingDeque.Node<E> n = this.lastRet;
/*  184 */       if (n == null) {
/*  185 */         throw new IllegalStateException();
/*      */       }
/*  187 */       this.lastRet = null;
/*  188 */       LinkedBlockingDeque.this.lock.lock();
/*      */       try {
/*  190 */         if (n.item != null) {
/*  191 */           LinkedBlockingDeque.this.unlink(n);
/*      */         }
/*      */       } finally {
/*  194 */         LinkedBlockingDeque.this.lock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private LinkedBlockingDeque.Node<E> succ(LinkedBlockingDeque.Node<E> n) {
/*      */       while (true) {
/*  209 */         LinkedBlockingDeque.Node<E> s = nextNode(n);
/*  210 */         if (s == null) {
/*  211 */           return null;
/*      */         }
/*  213 */         if (s.item != null) {
/*  214 */           return s;
/*      */         }
/*  216 */         if (s == n) {
/*  217 */           return firstNode();
/*      */         }
/*  219 */         n = s;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class DescendingItr extends AbstractItr { private DescendingItr() {}
/*      */     
/*      */     LinkedBlockingDeque.Node<E> firstNode() {
/*  227 */       return LinkedBlockingDeque.this.last;
/*      */     } LinkedBlockingDeque.Node<E> nextNode(LinkedBlockingDeque.Node<E> n) {
/*  229 */       return n.prev;
/*      */     } }
/*      */   
/*      */   private class Itr extends AbstractItr { private Itr() {}
/*      */     
/*      */     LinkedBlockingDeque.Node<E> firstNode() {
/*  235 */       return LinkedBlockingDeque.this.first;
/*      */     } LinkedBlockingDeque.Node<E> nextNode(LinkedBlockingDeque.Node<E> n) {
/*  237 */       return n.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Node<E>
/*      */   {
/*      */     E item;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Node<E> prev;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Node<E> next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Node(E x, Node<E> p, Node<E> n) {
/*  275 */       this.item = x;
/*  276 */       this.prev = p;
/*  277 */       this.next = n;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkedBlockingDeque() {
/*  317 */     this(2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkedBlockingDeque(boolean fairness) {
/*  327 */     this(2147483647, fairness);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkedBlockingDeque(Collection<? extends E> c) {
/*  344 */     this(2147483647);
/*  345 */     this.lock.lock();
/*      */     try {
/*  347 */       for (E e : c) {
/*  348 */         Objects.requireNonNull(e);
/*  349 */         if (!linkLast(e)) {
/*  350 */           throw new IllegalStateException("Deque full");
/*      */         }
/*      */       } 
/*      */     } finally {
/*  354 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkedBlockingDeque(int capacity) {
/*  365 */     this(capacity, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkedBlockingDeque(int capacity, boolean fairness) {
/*  378 */     if (capacity <= 0) {
/*  379 */       throw new IllegalArgumentException();
/*      */     }
/*  381 */     this.capacity = capacity;
/*  382 */     this.lock = new InterruptibleReentrantLock(fairness);
/*  383 */     this.notEmpty = this.lock.newCondition();
/*  384 */     this.notFull = this.lock.newCondition();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean add(E e) {
/*  392 */     addLast(e);
/*  393 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFirst(E e) {
/*  401 */     if (!offerFirst(e)) {
/*  402 */       throw new IllegalStateException("Deque full");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLast(E e) {
/*  413 */     if (!offerLast(e)) {
/*  414 */       throw new IllegalStateException("Deque full");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  424 */     this.lock.lock();
/*      */     try {
/*  426 */       for (Node<E> f = this.first; f != null; ) {
/*  427 */         f.item = null;
/*  428 */         Node<E> n = f.next;
/*  429 */         f.prev = null;
/*  430 */         f.next = null;
/*  431 */         f = n;
/*      */       } 
/*  433 */       this.first = this.last = null;
/*  434 */       this.count = 0;
/*  435 */       this.notFull.signalAll();
/*      */     } finally {
/*  437 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(Object o) {
/*  451 */     if (o == null) {
/*  452 */       return false;
/*      */     }
/*  454 */     this.lock.lock();
/*      */     try {
/*  456 */       for (Node<E> p = this.first; p != null; p = p.next) {
/*  457 */         if (o.equals(p.item)) {
/*  458 */           return true;
/*      */         }
/*      */       } 
/*  461 */       return false;
/*      */     } finally {
/*  463 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<E> descendingIterator() {
/*  472 */     return new DescendingItr();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int drainTo(Collection<? super E> c) {
/*  491 */     return drainTo(c, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int drainTo(Collection<? super E> c, int maxElements) {
/*  511 */     Objects.requireNonNull(c, "c");
/*  512 */     if (c == this) {
/*  513 */       throw new IllegalArgumentException();
/*      */     }
/*  515 */     this.lock.lock();
/*      */     try {
/*  517 */       int n = Math.min(maxElements, this.count); int i;
/*  518 */       for (i = 0; i < n; i++) {
/*  519 */         c.add(this.first.item);
/*  520 */         unlinkFirst();
/*      */       } 
/*  522 */       i = n; return i;
/*      */     } finally {
/*  524 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E element() {
/*  540 */     return getFirst();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E getFirst() {
/*  548 */     E x = peekFirst();
/*  549 */     if (x == null) {
/*  550 */       throw new NoSuchElementException();
/*      */     }
/*  552 */     return x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E getLast() {
/*  560 */     E x = peekLast();
/*  561 */     if (x == null) {
/*  562 */       throw new NoSuchElementException();
/*      */     }
/*  564 */     return x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTakeQueueLength() {
/*  574 */     this.lock.lock();
/*      */     try {
/*  576 */       return this.lock.getWaitQueueLength(this.notEmpty);
/*      */     } finally {
/*  578 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasTakeWaiters() {
/*  589 */     this.lock.lock();
/*      */     try {
/*  591 */       return this.lock.hasWaiters(this.notEmpty);
/*      */     } finally {
/*  593 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void interuptTakeWaiters() {
/*  602 */     this.lock.lock();
/*      */     try {
/*  604 */       this.lock.interruptWaiters(this.notEmpty);
/*      */     } finally {
/*  606 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<E> iterator() {
/*  624 */     return new Itr();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean linkFirst(E e) {
/*  636 */     if (this.count >= this.capacity) {
/*  637 */       return false;
/*      */     }
/*  639 */     Node<E> f = this.first;
/*  640 */     Node<E> x = new Node<>(e, null, f);
/*  641 */     this.first = x;
/*  642 */     if (this.last == null) {
/*  643 */       this.last = x;
/*      */     } else {
/*  645 */       f.prev = x;
/*      */     } 
/*  647 */     this.count++;
/*  648 */     this.notEmpty.signal();
/*  649 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean linkLast(E e) {
/*  661 */     if (this.count >= this.capacity) {
/*  662 */       return false;
/*      */     }
/*  664 */     Node<E> l = this.last;
/*  665 */     Node<E> x = new Node<>(e, l, null);
/*  666 */     this.last = x;
/*  667 */     if (this.first == null) {
/*  668 */       this.first = x;
/*      */     } else {
/*  670 */       l.next = x;
/*      */     } 
/*  672 */     this.count++;
/*  673 */     this.notEmpty.signal();
/*  674 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean offer(E e) {
/*  682 */     return offerLast(e);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean offer(E e, Duration timeout) throws InterruptedException {
/*  701 */     return offerLast(e, timeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  721 */     return offerLast(e, timeout, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean offerFirst(E e) {
/*  729 */     Objects.requireNonNull(e, "e");
/*  730 */     this.lock.lock();
/*      */     try {
/*  732 */       return linkFirst(e);
/*      */     } finally {
/*  734 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean offerFirst(E e, Duration timeout) throws InterruptedException {
/*  752 */     Objects.requireNonNull(e, "e");
/*  753 */     long nanos = timeout.toNanos();
/*  754 */     this.lock.lockInterruptibly();
/*      */     try {
/*  756 */       while (!linkFirst(e)) {
/*  757 */         if (nanos <= 0L) {
/*  758 */           return false;
/*      */         }
/*  760 */         nanos = this.notFull.awaitNanos(nanos);
/*      */       } 
/*  762 */       return true;
/*      */     } finally {
/*  764 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  783 */     return offerFirst(e, PoolImplUtils.toDuration(timeout, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean offerLast(E e) {
/*  791 */     Objects.requireNonNull(e, "e");
/*  792 */     this.lock.lock();
/*      */     try {
/*  794 */       return linkLast(e);
/*      */     } finally {
/*  796 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean offerLast(E e, Duration timeout) throws InterruptedException {
/*  814 */     Objects.requireNonNull(e, "e");
/*  815 */     long nanos = timeout.toNanos();
/*  816 */     this.lock.lockInterruptibly();
/*      */     try {
/*  818 */       while (!linkLast(e)) {
/*  819 */         if (nanos <= 0L) {
/*  820 */           return false;
/*      */         }
/*  822 */         nanos = this.notFull.awaitNanos(nanos);
/*      */       } 
/*  824 */       return true;
/*      */     } finally {
/*  826 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  845 */     return offerLast(e, PoolImplUtils.toDuration(timeout, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public E peek() {
/*  850 */     return peekFirst();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E peekFirst() {
/*  857 */     this.lock.lock();
/*      */     try {
/*  859 */       return (this.first == null) ? null : this.first.item;
/*      */     } finally {
/*  861 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public E peekLast() {
/*  867 */     this.lock.lock();
/*      */     try {
/*  869 */       return (this.last == null) ? null : this.last.item;
/*      */     } finally {
/*  871 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public E poll() {
/*  877 */     return pollFirst();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   E poll(Duration timeout) throws InterruptedException {
/*  892 */     return pollFirst(timeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/*  908 */     return pollFirst(timeout, unit);
/*      */   }
/*      */ 
/*      */   
/*      */   public E pollFirst() {
/*  913 */     this.lock.lock();
/*      */     try {
/*  915 */       return unlinkFirst();
/*      */     } finally {
/*  917 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   E pollFirst(Duration timeout) throws InterruptedException {
/*  931 */     long nanos = timeout.toNanos();
/*  932 */     this.lock.lockInterruptibly();
/*      */     try {
/*      */       E x;
/*  935 */       while ((x = unlinkFirst()) == null) {
/*  936 */         if (nanos <= 0L) {
/*  937 */           return null;
/*      */         }
/*  939 */         nanos = this.notEmpty.awaitNanos(nanos);
/*      */       } 
/*  941 */       return x;
/*      */     } finally {
/*  943 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
/*  958 */     return pollFirst(PoolImplUtils.toDuration(timeout, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public E pollLast() {
/*  963 */     this.lock.lock();
/*      */     try {
/*  965 */       return unlinkLast();
/*      */     } finally {
/*  967 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E pollLast(Duration timeout) throws InterruptedException {
/*  982 */     long nanos = timeout.toNanos();
/*  983 */     this.lock.lockInterruptibly();
/*      */     try {
/*      */       E x;
/*  986 */       while ((x = unlinkLast()) == null) {
/*  987 */         if (nanos <= 0L) {
/*  988 */           return null;
/*      */         }
/*  990 */         nanos = this.notEmpty.awaitNanos(nanos);
/*      */       } 
/*  992 */       return x;
/*      */     } finally {
/*  994 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E pollLast(long timeout, TimeUnit unit) throws InterruptedException {
/* 1010 */     return pollLast(PoolImplUtils.toDuration(timeout, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E pop() {
/* 1018 */     return removeFirst();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void push(E e) {
/* 1026 */     addFirst(e);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void put(E e) throws InterruptedException {
/* 1044 */     putLast(e);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putFirst(E e) throws InterruptedException {
/* 1058 */     Objects.requireNonNull(e, "e");
/* 1059 */     this.lock.lock();
/*      */     try {
/* 1061 */       while (!linkFirst(e)) {
/* 1062 */         this.notFull.await();
/*      */       }
/*      */     } finally {
/* 1065 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putLast(E e) throws InterruptedException {
/* 1080 */     Objects.requireNonNull(e, "e");
/* 1081 */     this.lock.lock();
/*      */     try {
/* 1083 */       while (!linkLast(e)) {
/* 1084 */         this.notFull.await();
/*      */       }
/*      */     } finally {
/* 1087 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1100 */     s.defaultReadObject();
/* 1101 */     this.count = 0;
/* 1102 */     this.first = null;
/* 1103 */     this.last = null;
/*      */ 
/*      */     
/*      */     while (true) {
/* 1107 */       E item = (E)s.readObject();
/* 1108 */       if (item == null) {
/*      */         break;
/*      */       }
/* 1111 */       add(item);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int remainingCapacity() {
/* 1131 */     this.lock.lock();
/*      */     try {
/* 1133 */       return this.capacity - this.count;
/*      */     } finally {
/* 1135 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E remove() {
/* 1155 */     return removeFirst();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object o) {
/* 1176 */     return removeFirstOccurrence(o);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E removeFirst() {
/* 1184 */     E x = pollFirst();
/* 1185 */     if (x == null) {
/* 1186 */       throw new NoSuchElementException();
/*      */     }
/* 1188 */     return x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeFirstOccurrence(Object o) {
/* 1234 */     if (o == null) {
/* 1235 */       return false;
/*      */     }
/* 1237 */     this.lock.lock();
/*      */     try {
/* 1239 */       for (Node<E> p = this.first; p != null; p = p.next) {
/* 1240 */         if (o.equals(p.item)) {
/* 1241 */           unlink(p);
/* 1242 */           return true;
/*      */         } 
/*      */       } 
/* 1245 */       return false;
/*      */     } finally {
/* 1247 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E removeLast() {
/* 1256 */     E x = pollLast();
/* 1257 */     if (x == null) {
/* 1258 */       throw new NoSuchElementException();
/*      */     }
/* 1260 */     return x;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeLastOccurrence(Object o) {
/* 1265 */     if (o == null) {
/* 1266 */       return false;
/*      */     }
/* 1268 */     this.lock.lock();
/*      */     try {
/* 1270 */       for (Node<E> p = this.last; p != null; p = p.prev) {
/* 1271 */         if (o.equals(p.item)) {
/* 1272 */           unlink(p);
/* 1273 */           return true;
/*      */         } 
/*      */       } 
/* 1276 */       return false;
/*      */     } finally {
/* 1278 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/* 1289 */     this.lock.lock();
/*      */     try {
/* 1291 */       return this.count;
/*      */     } finally {
/* 1293 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E take() throws InterruptedException {
/* 1309 */     return takeFirst();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E takeFirst() throws InterruptedException {
/* 1320 */     this.lock.lock();
/*      */     try {
/*      */       E x;
/* 1323 */       while ((x = unlinkFirst()) == null) {
/* 1324 */         this.notEmpty.await();
/*      */       }
/* 1326 */       return x;
/*      */     } finally {
/* 1328 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E takeLast() throws InterruptedException {
/* 1340 */     this.lock.lock();
/*      */     try {
/*      */       E x;
/* 1343 */       while ((x = unlinkLast()) == null) {
/* 1344 */         this.notEmpty.await();
/*      */       }
/* 1346 */       return x;
/*      */     } finally {
/* 1348 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object[] toArray() {
/* 1370 */     this.lock.lock();
/*      */     try {
/* 1372 */       Object[] a = new Object[this.count];
/* 1373 */       int k = 0;
/* 1374 */       for (Node<E> p = this.first; p != null; p = p.next) {
/* 1375 */         a[k++] = p.item;
/*      */       }
/* 1377 */       return a;
/*      */     } finally {
/* 1379 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T[] toArray(T[] a) {
/* 1389 */     this.lock.lock();
/*      */     try {
/* 1391 */       if (a.length < this.count)
/*      */       {
/* 1393 */         a = (T[])Array.newInstance(a.getClass().getComponentType(), this.count);
/*      */       }
/* 1395 */       int k = 0;
/* 1396 */       for (Node<E> p = this.first; p != null; p = p.next) {
/* 1397 */         a[k++] = (T)p.item;
/*      */       }
/* 1399 */       if (a.length > k) {
/* 1400 */         a[k] = null;
/*      */       }
/* 1402 */       return a;
/*      */     } finally {
/* 1404 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1410 */     this.lock.lock();
/*      */     try {
/* 1412 */       return super.toString();
/*      */     } finally {
/* 1414 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void unlink(Node<E> x) {
/* 1425 */     Node<E> p = x.prev;
/* 1426 */     Node<E> n = x.next;
/* 1427 */     if (p == null) {
/* 1428 */       unlinkFirst();
/* 1429 */     } else if (n == null) {
/* 1430 */       unlinkLast();
/*      */     } else {
/* 1432 */       p.next = n;
/* 1433 */       n.prev = p;
/* 1434 */       x.item = null;
/*      */ 
/*      */       
/* 1437 */       this.count--;
/* 1438 */       this.notFull.signal();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private E unlinkFirst() {
/* 1451 */     Node<E> f = this.first;
/* 1452 */     if (f == null) {
/* 1453 */       return null;
/*      */     }
/* 1455 */     Node<E> n = f.next;
/* 1456 */     E item = f.item;
/* 1457 */     f.item = null;
/* 1458 */     f.next = f;
/* 1459 */     this.first = n;
/* 1460 */     if (n == null) {
/* 1461 */       this.last = null;
/*      */     } else {
/* 1463 */       n.prev = null;
/*      */     } 
/* 1465 */     this.count--;
/* 1466 */     this.notFull.signal();
/* 1467 */     return item;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private E unlinkLast() {
/* 1477 */     Node<E> l = this.last;
/* 1478 */     if (l == null) {
/* 1479 */       return null;
/*      */     }
/* 1481 */     Node<E> p = l.prev;
/* 1482 */     E item = l.item;
/* 1483 */     l.item = null;
/* 1484 */     l.prev = l;
/* 1485 */     this.last = p;
/* 1486 */     if (p == null) {
/* 1487 */       this.first = null;
/*      */     } else {
/* 1489 */       p.next = null;
/*      */     } 
/* 1491 */     this.count--;
/* 1492 */     this.notFull.signal();
/* 1493 */     return item;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1505 */     this.lock.lock();
/*      */     
/*      */     try {
/* 1508 */       s.defaultWriteObject();
/*      */       
/* 1510 */       for (Node<E> p = this.first; p != null; p = p.next) {
/* 1511 */         s.writeObject(p.item);
/*      */       }
/*      */       
/* 1514 */       s.writeObject(null);
/*      */     } finally {
/* 1516 */       this.lock.unlock();
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\LinkedBlockingDeque.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */