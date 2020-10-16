package runner;

import logic.Manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Runner {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        boolean contPlay = true;
        int x;
        int y;

        Manager manager = setupPlay(reader);

        System.out.println("Enter in coordinates in the form of \"x, y\" to reveal a location. Example: 1, 2");
        System.out.println("Prepend an \"f,\" followed by a set of coordinates if you want to flag a spot. Example: f, 1, 2");

        try {
            do {
                input = reader.readLine();
                if (input.contains(",")) {
                    String[] coordinates = input.split("\\s?,\\s?");

                    if (coordinates.length == 3 && coordinates[0].equals("f")) {
                        x = parseUintIfPossible(coordinates[1]);
                        y = parseUintIfPossible(coordinates[2]);

                        if (x != -1 && y != -1) {
                            manager.flagSquare(x, y);
                            manager.printPlayGrid(false);
                        }
                    } else if (coordinates.length == 2) {
                        x = parseUintIfPossible(coordinates[0]);
                        y = parseUintIfPossible(coordinates[1]);

                        if (x != -1 && y != -1) {
                            contPlay = manager.touchSquare(x, y);
                            if (!contPlay) {
                                manager.printPlayGrid(true);
                                System.out.println("BOOM! Too bad, you're dead :(");
                            } else {
                                manager.printPlayGrid(false);
                            }
                        }
                    } else {
                        System.out.println("Enter a valid x, y coordinate");
                    }
                }

                if (!contPlay) {
                    System.out.println("Enter in 'y' if you want to restart else enter in anything to exit.");
                    input = reader.readLine();
                    contPlay = input.equals("y") || input.equals("Y");
                    if (contPlay) {
                        manager = setupPlay(reader);
                    }
                }
            } while (!input.equals("exit") && contPlay);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static Manager setupPlay(BufferedReader reader) {

        String input = "";
        try {
            System.out.println("Enter in board dimensions in the form of n*m OR just hit enter to use default dimensions. Example: 5*6");
            input = reader.readLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (input.contains("*")) {
            String[] dimension = input.split("\\s?\\*\\s?");

            if (dimension.length == 2) {
                int x = parseUintIfPossible(dimension[0]);
                int y = parseUintIfPossible(dimension[1]);
                if (x != -1 && y != -1) {
                    return new Manager(x, y);
                }
            }
        }

        System.out.println("No setup parameters or invalid parameters. Using default grid size of 10x10");
        return new Manager();
    }

    private static int parseUintIfPossible(String str) {
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (Exception e) {
            num = -1;
        }
        return num;
    }

}