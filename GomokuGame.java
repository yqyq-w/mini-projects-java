/**
 2. 编程实现控制台版并支持两人对战的五子棋游戏。 
 （1）绘制棋盘 - 写一个成员方法实现 
 （2）提示黑方和白方分别下棋并重新绘制棋盘 - 写一个成员方法实现。 
 （3）每当一方下棋后判断是否获胜 - 写一个成员方法实现。 
 （4）提示： 采用二维数组来模拟并描述棋盘

 @author: Yiqing Wang
 */

import java.util.*;

public class GomokuGame {
    
    // 有关整体游戏的成员变量
    public static final int[] GAME_MEMBER = {87, 66};  //  'W' ASCII Dec 87；'B' ASCII Dec 66
    public static final char[] GAME_MEMBER_CHINESE = {'白', '黑'}; // '白' \u767d；'黑' \u9ed1
    // 有关每局游戏的变量
    private int[][] gameBoard;
    private int cntMoves;
    private boolean hasWinner;
    // 有关每次落子位置的成员变量
    private int x;
    private int y;
    private int cntNeighbours;
    private int leftY;
    private int rightY;
    private int upX;
    private int downX;

    /**
     * 五子棋游戏构造方法，填充初始棋盘（适用17*17以内）
     * @param boardDimension 棋盘规格，输入17则棋盘为二维数组[17][17]
     */
    public GomokuGame(int boardDimension) {
        System.out.println("欢迎来到五子棋游戏!");
        // 声明参数*参数的二维数组作为棋盘；其中第一行和第一列为棋格的范围
        this.gameBoard = new int[boardDimension][boardDimension];
        // 填充棋盘的第一行和第一列，表示棋格范围
        gameBoard[0][0] = 32; // 空格 ASCII Dec 32
        for (int i = 0; i < boardDimension - 1; i++) {
            gameBoard[0][i + 1] = i;
            gameBoard[i + 1][0] = i;
        }
        // 双重循环填充其它格子为'+'
        for (int i = 1; i < gameBoard.length; i++) {
            for (int j = 1; j < gameBoard[i].length; j++) {
                    gameBoard[i][j] = 43; // '+' ASCII DEC 43
            }
        }
        // 打印输出初始棋盘
        printGameBoard();
    }

    /**
     * 开始五子棋的成员方法，会提示黑方和白方分别下棋、重新绘制棋盘并判断是否产生赢家
     */
    public void startGame() {
        System.out.println("新的一局五子棋游戏开始了!");
        // 新游戏开始时，重新计算总的下棋步数
        setCntMoves(0);
        // 新游戏开始时，未产生赢家
        setHasWinner(false);
        
        // 由用户输入下棋位置
        Scanner sc = new Scanner(System.in);
        while (!hasWinner){
            // 提示黑方或白方下棋，通过cntMoves % 2判断（0或1）
            System.out.printf("该%c方下棋了! 请输入下一步想下棋的位置，格式为\"2 3\", " +
                    "范围在0到%d之间：\n", GAME_MEMBER_CHINESE[cntMoves % 2], gameBoard.length - 2);
            x = sc.nextInt() + 1; // 坐标显示为1 1的实则是数组的[2][2]
            y = sc.nextInt() + 1;
            // 判断该位置是否在棋盘内
            if (withinBoard(x, y)) {
                // 如果输入位置允许下新的棋子，判断是否有五子连线
                if (placePiece()){
                    // 如果有棋子胜出，hasWinner变为true，下一轮循环停止
                    if (winOrNot()){
                        System.out.printf("赢家已诞生，是%c方!\n", GAME_MEMBER_CHINESE[cntMoves % 2]);
                        break;
                    }
                    // 如果没有棋子胜出，继续下棋，步数增加
                    cntMoves++;
                }
            } else {
                // 如果该位置超出棋盘，提示用户重新下棋
                System.out.println("该位置超出棋盘范围，请重新下棋!");
            }
        }
    }

    /**
     * 设置累计下棋步数
     * @param cntMoves
     */
    public void setCntMoves(int cntMoves) {
        this.cntMoves = cntMoves;
    }

    /**
     * 设置是否产生赢家
     * @param hasWinner
     */
    public void setHasWinner(boolean hasWinner) {
        this.hasWinner = hasWinner;
    }

    /**
     * 绘制棋盘 
     */
    public void printGameBoard(){
        System.out.println("现在的棋盘如下: ");
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                // 如果是第一行和第一列的范围，用十六进制格式打印输出（空格除外）
                if (i+j != 0 && i*j == 0) {
                    System.out.printf("%x ", gameBoard[i][j]);
                } else {
                    System.out.printf("%c ", gameBoard[i][j]);
                }
            }
            System.out.print("\n");
        }
    }

    /**
     * 放置新棋子的方法
     * @return 返回是否能放置新棋子
     */
    private boolean placePiece() {
        // 如果落子点目前的元素是'+'，则放置新棋子，并答应输出相关提示
        if (gameBoard[x][y] == '+') {
            gameBoard[x][y] = GAME_MEMBER[cntMoves % 2];
            System.out.printf("新下了一颗%c棋！\n", GAME_MEMBER_CHINESE[cntMoves % 2]);
            printGameBoard();
            return true;
        } else {
            // 如果已有棋子则提示需要重新下棋
            System.out.println("该位置已有棋子，请重新下棋！");
            return false;
        }
    }

    /**
     * 每当一方下棋后判断是否获胜
     * @return 返回是否产生赢家
     */
    private boolean winOrNot() {
        // 1. 左右，同一行 (x, leftY) (x, rightY)
        // 重置新下棋子的邻居坐标位置和同色棋子计数
        resetNeighbours();
        // 如果邻居坐标在棋盘范围内，且邻居棋子同色，那么计数累加，并更新邻居坐标
        while (withinBoard(leftY) && hasSameColor(x, leftY)) {
            cntNeighbours++;
            leftY--;
            }
        while (withinBoard(rightY) && hasSameColor(x, rightY)) {
            cntNeighbours++;
            leftY++;
        }
        if (cntNeighbours >= 5) {
            hasWinner = true;
            return true;
        }
        // 2. 上下，同一列  (upX, y) (downX, y)
        resetNeighbours();
        while (withinBoard(upX) && hasSameColor(upX, y)) {
            cntNeighbours++;
            upX--;
        }
        while (withinBoard(downX) && hasSameColor(downX, y)) {
            cntNeighbours++;
            downX++;
        }
        if (cntNeighbours >= 5) {
            hasWinner = true;
            return true;
        }
        // 3. 左上，右下 (upX, leftY) (downX, rightY)
        resetNeighbours();
        while (withinBoard(upX, leftY) && hasSameColor(upX, leftY)) {
            cntNeighbours++;
            upX--;
            leftY--;
        }
        while (withinBoard(downX, rightY) && hasSameColor(downX, rightY)) {
            cntNeighbours++;
            downX++;
            rightY++;
        }
        if (cntNeighbours >= 5) {
            hasWinner = true;
            return true;
        }
        // 4. 左下，右上 (downX, leftY) (upX, rightY)
        resetNeighbours();
        while (withinBoard(downX, leftY) && hasSameColor(downX, leftY)) {
            cntNeighbours++;
            downX++;
            leftY--;
        }
        while (withinBoard(upX, rightY) && hasSameColor(upX, rightY)) {
            cntNeighbours++;
            upX--;
            rightY++;
        }
        if (cntNeighbours >= 5) {
            hasWinner = true;
            return true;
        }

        // 如果四个方向都没有5子连成线则返回false
        return false;
    }

    /**
     * 判断输入坐标是否在下棋的棋盘内
     * @param nums 需要判断的坐标值作为可变长参数
     * @return 返回用户指定的坐标是否在棋盘范围内
     */
    private boolean withinBoard(int ... nums){
        for (int num : nums) {
            if (num < 1 || num > gameBoard.length-1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 重置邻居坐标位置和同色棋子计数的方法
     */
    private void resetNeighbours(){
        cntNeighbours = 1; // 新下的棋子本身
        leftY = y - 1;
        rightY = y + 1;
        upX = x - 1;
        downX = x + 1;
    }

    /**
     * 判断邻居棋子和新下棋子是否同色的方法
     * @param x 需判断邻居棋子的行坐标
     * @param y 需判断邻居棋子的列坐标
     * @return 返回该邻居棋子是否同色
     */
    private boolean hasSameColor(int x, int y){
        return gameBoard[x][y] == GAME_MEMBER[cntMoves % 2];
    }


    /**
     * 通过主方法开始五子棋游戏
     */
    public static void main(String[] args) {
        GomokuGame game = new GomokuGame(17);
        game.startGame();
    }

}


