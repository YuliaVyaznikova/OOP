package ru.nsu.vyaznikova;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.vyaznikova.controller.GameController;
import ru.nsu.vyaznikova.engine.JavaFXGameLoop;
import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.view.GameView;

/**
 * Главный класс приложения, отвечающий за:
 * - Определение констант игры (размер поля, целевая длина змейки)
 * - Создание всех основных компонентов (модель, представление, контроллер)
 * - Настройку UI (создание окна, сцены, обработчиков событий)
 * - Запуск игрового цикла
 */
public class MainApp extends Application {
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 15;
    private static final int TARGET_LENGTH = 10;
    private static final int GAME_SPEED = 200; // миллисекунды между обновлениями
    private static final int CELL_SIZE = 30; // размер одной ячейки в пикселях

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Создаем компоненты игры
        GameModel gameModel = new GameModel(GRID_WIDTH, GRID_HEIGHT, TARGET_LENGTH);
        Text scoreText = new Text("Length: 1");
        GameView gameView = new GameView(gameModel, scoreText);
        GameController gameController = new GameController(gameModel);
        JavaFXGameLoop gameLoop = new JavaFXGameLoop(gameModel, gameView::draw);

        // Создаем layout
        VBox root = new VBox(10); // 10 - отступ между элементами
        root.getChildren().addAll(scoreText, gameView);
        
        // Настраиваем сцену
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(gameController::handleKeyEvent);
        
        // Настраиваем окно
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Запускаем игровой цикл
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}