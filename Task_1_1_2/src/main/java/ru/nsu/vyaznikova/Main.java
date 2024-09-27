package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the main game logic of a Blackjack game.
 */
public class Main {

    /**
     * The main method of the program, which starts the game.
     *
     * @param args Command line arguments, not used in this program.
     */
    public static void main(String[] args) {
        doAll();
    }

    /**
     * The main game loop, handling all aspects of the Blackjack game.
     */
    public static void doAll() {
        Scanner scanner = new Scanner(System.in);
        int playerScore = 0;
        int dealerScore = 0;

        System.out.println("Добро пожаловать в Блэкджек!");

        int round = 1;
        boolean end = false;
        while (!end) {
            System.out.println("\nРаунд " + round);
            System.out.println("Если захотите завершить игру, введите \"2\".");
            Deck deck = new Deck();
            deck.shuffle();
            Player player = new HumanPlayer(deck);
            Dealer dealer = new Dealer(deck);

            player.takeCard();
            player.takeCard();
            dealer.takeCard();
            dealer.takeCard();

            if (player.calculateScore() == 21 && player.getCards().size() == 2) {
                System.out.println("Вы выиграли раунд! У вас Blackjack!");
                playerScore++;
                System.out.println("Счет: " + playerScore + ":" + dealerScore);
                round++;
                continue;
            }

            if (dealer.calculateScore() == 21 && dealer.getCards().size() == 2) {
                System.out.println("Дилер выиграл раунд! У него Blackjack!");
                dealerScore++;
                System.out.println("Счет: " + playerScore + ":" + dealerScore);
                round++;
                continue;
            }

            System.out.println("\nДилер раздал карты");
            System.out.println("Ваши карты: "
                    + player.getCards()
                    + " => "
                    + player.calculateScore());
            System.out.println("Карты дилера: ["
                    + dealer.getCards().get(0) + ", <закрытая карта>]");

            while (player.calculateScore() < 21) {
                System.out.println("\nВаш ход");
                System.out.println("-------");
                System.out.println("Введите \"1\", чтобы взять карту,"
                        + "и \"0\", чтобы остановиться...");

                BotReader.setPscore(player.calculateScore());
                int choice = BotReader.read();

                if (choice == 2) {

                    System.out.println("\nИгра окончена! Счет "
                            + playerScore + ":" + dealerScore + ".");

                    end = true;
                    break; // Прерываем цикл
                } else if (choice == 1) {
                    player.takeCard();
                    System.out.println("Вы открыли карту "
                            + player.getCards().get(player.getCards().size() - 1));
                    System.out.println("Ваши карты: "
                            + player.getCards()
                            + " => " + player.calculateScore());

                } else if (choice == 0) {
                    break;
                } else {
                    System.out.println("Неверный ввод. Пожалуйста, введите 1 или 0.");
                }
            }

            if (player.calculateScore() > 21) {
                System.out.println("Вы проиграли раунд! Превышен лимит очков.");
                dealerScore++;
            } else {
                System.out.println("\nХод дилера");
                System.out.println("-------");
                System.out.println("Дилер открывает закрытую карту "
                        + dealer.getCards().get(1));
                System.out.println("Ваши карты: "
                        + player.getCards()
                        + " => "
                        + player.calculateScore());
                System.out.println("Карты дилера: "
                        + dealer.getCards()
                        + " => "
                        + dealer.calculateScore());

                while (dealer.calculateScore() < 17) {
                    dealer.takeCard();
                    System.out.println("Дилер открывает карту "
                            + dealer.getCards().get(dealer.getCards().size() - 1));
                    System.out.println("Ваши карты: "
                            + player.getCards()
                            + " => "
                            + player.calculateScore());
                    System.out.println("Карты дилера: "
                            + dealer.getCards()
                            + " => "
                            + dealer.calculateScore());
                }


                if (player.calculateScore() > 21) {
                    System.out.println("Вы проиграли раунд! Превышен лимит очков.");
                    dealerScore++;
                } else if (dealer.calculateScore() > 21) {
                    System.out.println("Вы выиграли раунд! Дилер превысил лимит очков.");
                    playerScore++;
                } else if (player.calculateScore() > dealer.calculateScore()) {
                    System.out.println("Вы выиграли раунд! У вас больше очков.");
                    playerScore++;
                } else if (dealer.calculateScore() > player.calculateScore()) {
                    System.out.println("Вы проиграли раунд! У дилера больше очков.");
                    dealerScore++;
                } else {
                    System.out.println("Ничья! У вас одинаковое количество очков.");
                }
            }

            System.out.println("Счет: " + playerScore + ":" + dealerScore);
            round++;
        }

        if (playerScore > dealerScore) {
            System.out.println("Вы победили!");
        } else if (dealerScore > playerScore) {
            System.out.println("Дилер победил!");
        } else {
            System.out.println("Ничья!");
        }
    }
}