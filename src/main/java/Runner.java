import characters.enemies.Enemy;
import characters.enemies.Goblin;
import characters.players.Mage;
import characters.players.Player;
import characters.players.Ranger;
import characters.players.Warrior;
import enums.Consumable;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Runner {
    private final int DELAY;
    private Player player;
    private List<Enemy> enemies;
    private int currentRound;
    private Scanner scanner;


    public static void main(String[] args) throws InterruptedException {
        new Runner().play();
    }

    public Runner(){
        enemies = new ArrayList<>();
        DELAY = 1500;
        currentRound = 1;
        scanner = new Scanner(System.in);;
    }

    public void play() throws InterruptedException {
        System.out.println("Welcome to Arena\n");
        player = getPlayer();
        while(player.isAlive()){
            System.out.println();
            playRound();
        }
        System.out.println("You made it to round " + Integer.toString(currentRound));

    }

    private void playRound() throws InterruptedException {
        System.out.println("ROUND " + Integer.toString(currentRound));
        enemies.clear();

        for(int i = 0; i < currentRound; i++){
            enemies.add(new Goblin());
        }

        int healthBefore;
        int healthAfter;

        while(player.isAlive() && enemies.stream().anyMatch(e -> e.isAlive())){
            Enemy enemy = enemies.get(0);


            healthBefore = enemy.getHp();
            player.attack(enemy);
            healthAfter = enemy.getHp();

            System.out.println("Player attacks Goblin...");
            Thread.sleep(DELAY);
            if(healthBefore - healthAfter == 0){
                System.out.println("Player misses!");
            } else {
                System.out.println(String.format("Player attacks goblin for %d damage", healthBefore - healthAfter));
            }
            Thread.sleep(DELAY);

            if(enemy.isAlive()){
                System.out.println("Goblin attacks Player...");
                Thread.sleep(DELAY);
                healthBefore = player.getHp();
                enemy.attack(player);
                healthAfter = player.getHp();

                if(healthBefore - healthAfter == 0){
                    System.out.println("Goblin misses!");
                } else {
                    System.out.println(String.format("Goblin attacks player for %d damage", healthBefore - healthAfter));
                }

            } else {
                System.out.println("Enemy Goblin died...");
                int count = enemies.stream().map(e -> e.isAlive()? 1 : 0).reduce(0, (r,c) -> r + c);
                System.out.println(Integer.toString(count) + " Goblins left");
                enemies.remove(enemy);
                enemies.add(enemy);
            }
            System.out.println();
        }

        if(player.isAlive()){
            System.out.println("Congratulations you beat round " + Integer.toString(currentRound));
            healthBefore = player.getHp();
            player.takeConsumable(Consumable.HEALTH_POTION);
            healthAfter = player.getHp();
            Thread.sleep(DELAY);
            System.out.println(String.format("Player drinks a potion and heals for %d", healthAfter - healthBefore));
            System.out.println();
            currentRound++;
        } else {
            System.out.println("You died!");
        }
    }

    public Player getPlayer(){
        while(true){
            System.out.println("Choose your Character: ");
            System.out.println("\tPress m to choose Mage");
            System.out.println("\tPress r to choose Ranger");
            System.out.println("\tPress w to choose Warrior");
            System.out.println("\tPress q to Quit");
            System.out.println();

            String input = scanner.nextLine();
            char selection = input.toLowerCase().trim().charAt(0);

            switch(selection){
                case 'w':
                    System.out.println("You chose Warrior!");
                    return new Warrior();
                case 'r':
                    System.out.println("You chose Ranger!");
                    return new Ranger();
                case 'm':
                    System.out.println("You chose Mage!");
                    return new Mage();
                case 'q':
                    System.out.println("Exiting...");
                    System.exit(0);
            }
            System.out.println();
        }
    }
}
