/*
5. 编程题 
 使用集合实现斗地主游戏的部分功能，要求如下： 
 （1）首先准备 54 张扑克牌并打乱顺序。 
 （2）由三个玩家交替摸牌，每人 17 张扑克牌，最后三张留作底牌。 
 （3）查看三个玩家手中的扑克牌和底牌。 
 （4）其中玩家手中的扑克牌需要按照大小顺序打印，规则如下： 
    手中扑克牌从大到小的摆放顺序：大王,小王,2,A,K,Q,J,10,9,8,7,6,5,4,3

第五题打印发牌的过程

@author: Yiqing Wang
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PokerMain {

    public static void main(String[] args) {

        PokerGame game = new PokerGame();
        ArrayList<Card> deck = game.getDeck();
//        // 测试一下排序和洗牌方法
//        System.out.println("deck" + deck);
//        game.sort();
//        System.out.println("sort " + deck);
//        game.shuffle();
//        System.out.println("shuffle " + deck);

        // 发牌（deal the cards）
        // 声明三个玩家和底牌list作为map的values
        HashMap<String, ArrayList<Card>> cardsDealt = new HashMap<>();
        cardsDealt.put("player1", new ArrayList<Card>());
        cardsDealt.put("player2", new ArrayList<Card>());
        cardsDealt.put("player3", new ArrayList<Card>());
        cardsDealt.put("bottom", new ArrayList<Card>());

        // 由三个玩家交替摸牌，每人17张扑克牌，最后三张留作底牌
        for (int i = 0; i < 54; i++ ) {
            if (53 - i < 3) {
                System.out.printf("现在正在发第%d张底牌\n", 3 - 53 + i);
                cardsDealt.get("bottom").add(deck.get(i));
            } else {
                // 第%d张的计算方法：((i+1-(i%3+1))/3) % 17 + 1 = (i/3 - i%3 / 3) % 17 + 1
                System.out.printf("现在正在发第%d张牌给玩家%d\n", (i/3 - i%3 / 3) % 17 + 1, i % 3 + 1);
                switch (i % 3) {
                    case 0:
                        cardsDealt.get("player1").add(deck.get(i));
                        break;
                    case 1:
                        cardsDealt.get("player2").add(deck.get(i));
                        break;
                    case 2:
                        cardsDealt.get("player3").add(deck.get(i));
                        break;
                }
            }
        }

        // 查看三个玩家手中的扑克牌和底牌
        for (Map.Entry<String, ArrayList<Card>> me : cardsDealt.entrySet()){
            // 其中玩家手中的扑克牌需要按照大小顺序打印
            if (!"bottom".equals(me.getKey())) {
                // 通过lambda表达式写Comparator的匿名内部类
                // 先牌面降序，牌面相同花色降序
                me.getValue().sort((Card c1, Card c2) -> {
                    if (c1.getRank().equals(c2.getRank())) {
                        return c1.getSuit().compareTo(c2.getSuit());
                    }
                    return c1.getRank().compareTo(c2.getRank());
//                    // idea提示可以用以下方法取代
//                    Comparator<Card> cardComparator = Comparator.comparing(Card::getRank);
                });
            }
            System.out.println(me);
        }


    }
}
