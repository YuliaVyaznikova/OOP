package ru.nsu.vyaznikova.engine.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Шина событий игры, отвечающая за:
 * - Реализацию паттерна Observer для событийной системы
 * - Регистрацию слушателей событий
 * - Рассылку событий всем заинтересованным слушателям
 * - Обеспечение слабой связанности между компонентами игры
 */
public class EventBus {
    private static EventBus instance;
    private final Map<GameEvent.EventType, List<EventListener>> listeners;

    private EventBus() {
        listeners = new HashMap<>();
        for (GameEvent.EventType type : GameEvent.EventType.values()) {
            listeners.put(type, new ArrayList<>());
        }
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public void subscribe(GameEvent.EventType eventType, EventListener listener) {
        listeners.get(eventType).add(listener);
    }

    public void unsubscribe(GameEvent.EventType eventType, EventListener listener) {
        listeners.get(eventType).remove(listener);
    }

    public void publish(GameEvent event) {
        List<EventListener> eventListeners = listeners.get(event.getType());
        for (EventListener listener : eventListeners) {
            listener.onEvent(event);
        }
    }
}
