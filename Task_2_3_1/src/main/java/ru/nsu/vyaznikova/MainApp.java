package ru.nsu.vyaznikova;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.vyaznikova.controller.GameController;
import ru.nsu.vyaznikova.engine.events.EventBus;
import ru.nsu.vyaznikova.engine.events.Event;
import ru.nsu.vyaznikova.engine.events.EventListener;
import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.view.GameView;
import ru.nsu.vyaznikova.engine.JavaFXGameLoop;

/**
 * Главный класс приложения.
 */
public class MainApp extends Application implements EventListener {
    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 30;
    private static final int TARGET_LENGTH = 10;
    private static final int FOOD_COUNT = 3;

    private GameModel gameModel;
    private GameView gameView;
    private Text scoreText;
    private GameController gameController;

    @Override
    public void start(Stage primaryStage) {
        // Инициализация модели
        gameModel = new GameModel(GRID_WIDTH, GRID_HEIGHT, TARGET_LENGTH, FOOD_COUNT);

        // Инициализация представления
        scoreText = new Text("Length: 1");
        gameView = new GameView(GRID_WIDTH, GRID_HEIGHT, gameModel.getGrid());

        // Создаем контейнер и добавляем в него элементы
        VBox root = new VBox(10); // 10 - отступ между элементами
        root.getChildren().addAll(gameView.getCanvas(), scoreText);

        // Инициализация контроллера
        gameController = new GameController(gameModel);

        // Создаем сцену
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(gameController::handleKeyPress);

        // Настраиваем и показываем окно
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Подписываемся на события
        subscribeToEvents();

        // Запускаем игровой цикл
        JavaFXGameLoop gameLoop = new JavaFXGameLoop(gameModel, () -> {
            gameView.render();
            updateScore();
        });
        gameLoop.start();
    }

    private void subscribeToEvents() {
        EventBus.getInstance().subscribe("SNAKE_MOVED", this);
        EventBus.getInstance().subscribe("FOOD_EATEN", this);
        EventBus.getInstance().subscribe("GAME_OVER", this);
        EventBus.getInstance().subscribe("VICTORY", this);
        EventBus.getInstance().subscribe("STATE_CHANGED", this);
    }

    @Override
    public void onEvent(Event event) {
        switch (event.getEventType()) {
            case "SNAKE_MOVED" -> gameView.render();
            case "FOOD_EATEN" -> updateScore();
            case "GAME_OVER" -> scoreText.setText("Game Over!");
            case "VICTORY" -> scoreText.setText("Victory!");
            case "STATE_CHANGED" -> updateScore();
        }
    }

    private void updateScore() {
        scoreText.setText("Length: " + gameModel.getLength());
    }

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
}