package springboot;

import domain.Room;
import springBoot.socket_io.events.client.TimerEndedEvent;
import springBoot.socket_io.events.client.UpdateAllEvent;
import springBoot.socket_io.observer.IObservable;
import springBoot.socket_io.observer.ObserverEvent;
import springBoot.socket_io.observer.ObserverEventTypes;

public class ObservableTest implements IObservable{

    public ObserverEvent lastEvent;

    public ObservableTest(){

    }

	@Override
	public void update(ObserverEvent event) {
        lastEvent = event;
	}
    
}
