/**
 * Игровой цикл на JavaFX, отвечающий за:
 * - Наследование от AnimationTimer для точного таймирования
 * - Регулярное обновление состояния игры через GameModel
 * - Вызов перерисовки после каждого обновления
 * - Управление частотой обновлений игры
 * - Синхронизацию модели и представления
 */
package ru.nsu.vyaznikova.engine;

import javafx.animation.AnimationTimer;
import ru.nsu.vyaznikova.model.game.GameModel;

public class JavaFXGameLoop extends AnimationTimer implements GameLoop {
    private final GameModel model;
    private final Runnable updateView;
    private final long frameTimeInMs = 100 * 1_000_000L; // 100ms в наносекундах
    private long lastUpdate = 0;

    public JavaFXGameLoop(GameModel model, Runnable updateView) {
        this.model = model;
        this.updateView = updateView;
    }

    @Override
    public void handle(long now) {
        if ((now - lastUpdate) >= frameTimeInMs) {
            model.update();
            updateView.run();
            lastUpdate = now;
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void pause() {
        stop();
    }

    @Override
    public void resume() {
        start();
    }

    @Override
    public boolean isRunning() {
        return isRunning();
    }
}
