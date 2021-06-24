/*
5. 编程题 
 使用集合实现斗地主游戏的部分功能，要求如下： 
 （1）首先准备54 张扑克牌并打乱顺序。 
 （2）由三个玩家交替摸牌，每人 17 张扑克牌，最后三张留作底牌。 
 （3）查看三个玩家手中的扑克牌和底牌。 
 （4）其中玩家手中的扑克牌需要按照大小顺序打印，规则如下： 
    手中扑克牌从大到小的摆放顺序：大王,小王,2,A,K,Q,J,10,9,8,7,6,5,4,3

第五题打印发牌的过程

@author: Yiqing Wang
 */

import java.util.ArrayList;

public class Card {

    // 花色
    private SuitEnum suit;
    // 牌面
    private RankEnum rank;

    public Card() {
    }

    // 有参构造方法
    public Card(SuitEnum suit, RankEnum rank) {
        setSuit(suit);
        setRank(rank);
    }

    // getter & setter
    public SuitEnum getSuit() {
        return suit;
    }

    public void setSuit(SuitEnum suit) {
        this.suit = suit;
    }

    public RankEnum getRank() {
        return rank;
    }

    public void setRank(RankEnum rank) {
        this.rank = rank;
    }

    // 重写toString方法
    @Override
    public String toString() {
        return suit.getSuit() + rank.getRank();
    }

}

