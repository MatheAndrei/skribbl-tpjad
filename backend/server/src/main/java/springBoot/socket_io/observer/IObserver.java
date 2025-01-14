package springBoot.socket_io.observer;

public interface IObserver {
    boolean addObserver(IObservable obs);

    void notifyObservers(ObserverEvent event);
}
