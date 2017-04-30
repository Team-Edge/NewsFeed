package genericObserver;

import java.util.ArrayList;
import java.util.Collection;

/**
 * like java.util.Observable, But uses generics to avoid need for a cast.
 *
 * For any undocumented variable, parameter or method, see java.util.Observable
 * @see java.util.Observable
 */
public class Observable<T> {

    public interface Observer<U> {
        public void update(Observable<? extends U> observer, U arg);
    }

    private boolean changed = false;
    private final Collection<Observer<? super T>> observers;

    public Observable() {
    	this(new ArrayList<Observer<? super T>>());
    }

    public Observable(Collection<Observer<? super T>> observers) {
        this.observers = observers;
    }

    public void addObserver(final Observer<? super T> observer) {
        synchronized (observers) {
            if (!observers.contains(observer)) {
                observers.add(observer);
            }
        }
    }

    public void removeObserver(final Observer<? super T> observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    public void clearObservers() {
        synchronized (observers) {
            this.observers.clear();
        }
    }

    public void setChanged() {
        synchronized (observers) {
            this.changed = true;
        }
    }

    public void clearChanged() {
        synchronized (observers) {
            this.changed = false;
        }
    }

    public boolean hasChanged() {
        synchronized (observers) {
            return this.changed;
        }
    }

    public int countObservers() {
        synchronized (observers) {
            return observers.size();
        }
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(final T value) {
        ArrayList<Observer<? super T>> toNotify = null;
        synchronized(observers) {
            if (!changed) {
                return;
            }
            toNotify = new ArrayList<>(observers);
            changed = false;
        }
        for (Observer<? super T> observer : toNotify) {
            observer.update(this, value);
        }
    }
}