package logic;

import java.util.Random;

public class Manager {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    private Gridstate[][] grid;
    private Random rand = new Random();

    public Manager() {
        this(10, 10);
    }

    public Manager(int x, int y) {
        generateGrid(x, y);
    }

    private void generateGrid(int x, int y) {
        grid = generateBombs(x, y);
        //printBombGrid(grid);
        printPlayGrid(false);
    }

    private Gridstate[][] generateBombs(int x, int y) {
        int spawnChance = 20;
        Gridstate[][] grid = new Gridstate[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                grid[i][j] = rand.nextInt(100) < (spawnChance - 1) ? Gridstate.BOMB : Gridstate.UNTOUCHED;
            }
        }
        return grid;
    }

    private static void printBombGrid(Gridstate[][] grid) {
        for (int y = grid[0].length - 1; y > -1; y--) {
            for (Gridstate[] gridstates : grid) {
                if (gridstates[y] == Gridstate.BOMB) {
                    System.out.print("1  ");
                } else {
                    System.out.print("0  ");
                }
            }
            System.out.print("\n");
        }
    }

    public void printPlayGrid(boolean printBombs) {
        System.out.println("----------------------------");
        for (int y = grid[0].length - 1; y > -1; y--) {
            for (Gridstate[] gridstates : grid) {
                switch (gridstates[y]) {
                    case BOMB:
                        if (printBombs) {
                            System.out.print(ANSI_RED + "*  " + ANSI_RESET);
                        } else {
                            System.out.print("?  ");
                        }
                        break;
                    case UNTOUCHED:
                        System.out.print("?  ");
                        break;
                    case FLAGTRUE:
                    case FLAGFALSE:
                        System.out.print(ANSI_PURPLE + "#  " + ANSI_RESET);
                        break;
                    default:
                        System.out.print(ANSI_BLUE + gridstates[y].ordinal() + "  " + ANSI_RESET);
                }
            }
            System.out.print("\n");
        }
    }

    public boolean touchSquare(int x, int y) {
        switch (grid[x][y]) {
            case FLAGFALSE:
            case UNTOUCHED:
                grid[x][y] = neighbouringBombs(x, y);
                if (grid[x][y] == Gridstate.ZERO) {
                    revealAdjacentZeros(x, y);
                }
                break;
            case FLAGTRUE:
            case BOMB:
                return false;
            default:
        }
        return true;
    }

    public void flagSquare(int x, int y) {
        if (grid[x][y] == Gridstate.BOMB) {
            grid[x][y] = Gridstate.FLAGTRUE;
        } else if (grid[x][y] == Gridstate.FLAGTRUE) {
            grid[x][y] = Gridstate.BOMB;
        } else if (grid[x][y] == Gridstate.UNTOUCHED) {
            grid[x][y] = Gridstate.FLAGFALSE;
        } else if (grid[x][y] == Gridstate.FLAGFALSE) {
            grid[x][y] = Gridstate.UNTOUCHED;
        }
    }

    private Gridstate neighbouringBombs(int x, int y) {
        int count = 0;
        boolean leftValid = false;
        boolean rightValid = false;
        boolean downValid = false;
        boolean upValid = false;

        // check left
        if (x > 0) {
            leftValid = true;
            if (grid[x - 1][y] == Gridstate.BOMB || grid[x - 1][y] == Gridstate.FLAGTRUE) {
                count++;
            }
        }
        // check right
        if (x < grid.length - 1) {
            rightValid = true;
            if (grid[x + 1][y] == Gridstate.BOMB || grid[x + 1][y] == Gridstate.FLAGTRUE) {
                count++;
            }
        }
        // check down
        if (y > 0) {
            downValid = true;
            if (grid[x][y - 1] == Gridstate.BOMB || grid[x][y - 1] == Gridstate.FLAGTRUE) {
                count++;
            }
        }
        // check up
        if (y < grid[0].length - 1) {
            upValid = true;
            if (grid[x][y + 1] == Gridstate.BOMB || grid[x][y + 1] == Gridstate.FLAGTRUE) {
                count++;
            }
        }
        // check down+left
        if (leftValid && downValid && (grid[x - 1][y - 1] == Gridstate.BOMB || grid[x - 1][y - 1] == Gridstate.FLAGTRUE)) {
            count++;
        }
        // check down+right
        if (rightValid && downValid && (grid[x + 1][y - 1] == Gridstate.BOMB || grid[x + 1][y - 1] == Gridstate.FLAGTRUE)) {
            count++;
        }
        // check up+left
        if (leftValid && upValid && (grid[x - 1][y + 1] == Gridstate.BOMB || grid[x - 1][y + 1] == Gridstate.FLAGTRUE)) {
            count++;
        }
        // check up+right
        if (rightValid && upValid && (grid[x + 1][y + 1] == Gridstate.BOMB || grid[x + 1][y + 1] == Gridstate.FLAGTRUE)) {
            count++;
        }

        return Gridstate.values()[count];
    }

    private void revealAdjacentZeros(int x, int y) {
        boolean leftValid = false;
        boolean rightValid = false;
        boolean downValid = false;
        boolean upValid = false;

        // check left
        if (x > 0) {
            leftValid = true;
            touchSquare(x - 1, y);
        }
        // check right
        if (x < grid.length - 1) {
            rightValid = true;
            touchSquare(x + 1, y);
        }
        // check down
        if (y > 0) {
            downValid = true;
            touchSquare(x, y - 1);
        }
        // check up
        if (y < grid[0].length - 1) {
            upValid = true;
            touchSquare(x, y + 1);
        }
        // check down+left
        if (leftValid && downValid) {
            touchSquare(x - 1, y - 1);
        }
        // check down+right
        if (rightValid && downValid) {
            touchSquare(x + 1, y - 1);
        }
        // check up+left
        if (leftValid && upValid) {
            touchSquare(x - 1, y + 1);
        }
        // check up+right
        if (rightValid && upValid) {
            touchSquare(x + 1, y + 1);
        }
    }
}
