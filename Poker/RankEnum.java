/*
 扑克牌从大到小的摆放顺序：大王,小王,2,A,K,Q,J,10,9,8,7,6,5,4,3
 其中大小王为副牌，正牌的13个牌面牌为：ACE, DEUCE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE

@author: Yiqing Wang
 */

public enum RankEnum {

    // 正牌牌面，声明的顺序可用于后续排序
    JOKER("Joker"), ACE("A"), DEUCE("2"), KING("K"), QUEEN("Q"), JACK("J"), TEN("10"),
    NINE("9"), EIGHT("8"), SEVEN("7"), SIX("6"), FIVE("5"), FOUR("4"), THREE("3");

    // 描述牌面类型的成员变量
    private final String rank;

    // 私有化构造方法
    private RankEnum(String rank){
        this.rank = rank;
    }

    // 通过公有方法得到牌面类型
    public String getRank() {
        return rank;
    }

}
