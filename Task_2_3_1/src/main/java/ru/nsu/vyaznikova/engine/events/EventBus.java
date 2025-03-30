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
    private final Map<String, List<EventListener>> listeners;
    private final List<EventListener> globalListeners;

    private EventBus() {
        listeners = new HashMap<>();
        globalListeners = new ArrayList<>();
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public void subscribe(EventListener listener) {
        globalListeners.add(listener);
    }

    public void unsubscribe(EventListener listener) {
        globalListeners.remove(listener);
    }

    public void subscribe(String eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        if (listeners.containsKey(eventType)) {
            listeners.get(eventType).remove(listener);
        }
    }

    public void publish(Event event) {
        // Уведомляем глобальных слушателей
        for (EventListener listener : globalListeners) {
            listener.onEvent(event);
        }

        // Уведомляем слушателей конкретного типа события
        String eventType = event.getEventType();
        if (listeners.containsKey(eventType)) {
            List<EventListener> eventListeners = listeners.get(eventType);
            for (EventListener listener : eventListeners) {
                listener.onEvent(event);
            }
        }
    }
}
