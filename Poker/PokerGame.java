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
import java.util.Collections;

public class PokerGame {

    private ArrayList<Card> deck = new ArrayList<Card>();
//    // 或者不写枚举直接用以下数组；
//    private String[] jokers = {"RedJoker","BlackJoker"};
//    private String[] ranks = {"A", "2", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3"};
//    private String[] suits = {"♠️", "♥️", "♣️", "♦️"};

    // 构造方法
    public PokerGame() {
        // 获取枚举类的所有对象
        SuitEnum[] suits = SuitEnum.values();
        RankEnum[] ranks = RankEnum.values();
        // 先添加52张主牌
        for (int i = 0; i < 52; i++) {
            // 因为四种花色，13种牌面，可以用取余数的方法；再加上大小王在枚举类中的个数
            deck.add(new Card(suits[i % 4 + 2], ranks[i % 13 + 1]));
        }
        // 增加大小王
        deck.add(new Card(SuitEnum.RED_JOKER, RankEnum.JOKER));
        deck.add(new Card(SuitEnum.Black_JOKER, RankEnum.JOKER));
        // 洗牌打乱顺序
        shuffle();
    }

    public PokerGame(ArrayList<Card> deck) {
        setDeck(deck);
    }

    // 洗牌方法
    public void shuffle() {
        Collections.shuffle(deck);
    }

    // getter & setter
    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    // 排序方法，牌面降序，牌面相同花色降序
    public void sort() {
         // 通过lambda表达式写Comparator的匿名内部类
        deck.sort((Card c1, Card c2) -> {
                    if (c1.getRank().equals(c2.getRank())) {
                        return c1.getSuit().compareTo(c2.getSuit());
                    }
                    return c1.getRank().compareTo(c2.getRank());
//                    idea提示可以用以下方法取代
//                    Comparator<Card> cardComparator = Comparator.comparing(Card::getRank);
                });
    }

}
