package ru.nsu.vyaznikova;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.vyaznikova.controller.GameController;
import ru.nsu.vyaznikova.engine.JavaFXGameLoop;
import ru.nsu.vyaznikova.model.game.GameModel;
import ru.nsu.vyaznikova.view.GameView;
import ru.nsu.vyaznikova.view.input.JavaFXInputAdapter;

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
        GameModel model = new GameModel(GRID_WIDTH, GRID_HEIGHT, TARGET_LENGTH);
        GameController controller = new GameController(model);
        Text scoreText = new Text("Length: 1");
        GameView view = new GameView(model, scoreText);

        // Настраиваем сцену
        StackPane root = new StackPane();
        root.getChildren().addAll(view, scoreText);
        Scene scene = new Scene(root);
        
        // Настраиваем ввод через адаптер
        new JavaFXInputAdapter(scene, controller);

        // Настраиваем и запускаем игровой цикл
        JavaFXGameLoop gameLoop = new JavaFXGameLoop(model, view::draw);
        gameLoop.start();

        // Настраиваем и показываем окно
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}