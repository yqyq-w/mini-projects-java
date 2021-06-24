/*
 正牌花色包括：diamond方块♦️, spade黑桃♠️, club梅花♣️, heart红桃♥️
 花色大小跟打牌规则有关，常说的是：黑桃，红桃，梅花，方块

第五题打印发牌的过程

@author: Yiqing Wang
 */

public enum SuitEnum {

    // 正牌花色，声明的顺序可用于后续排序
    RED_JOKER("Red"), Black_JOKER("Black"), SPADE("♠️️"), Heart("♥️"), CLUB("♣️"), DIAMOND("♦️");

    // 描述花色的成员变量
    private final String suit;

    // 私有化构造方法
    private SuitEnum(String suit){
        this.suit = suit;
    }

    // 通过公有方法得到花色类型
    public String getSuit() {
        return suit;
    }

}
