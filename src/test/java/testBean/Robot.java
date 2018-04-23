package testBean;

/**
 * 机器人
 *
 * @author zj
 * @date 2018-4-21
 */
public class Robot {

    private Hand hand;

    private Mouth mouth;

    public void show() {
        hand.waveHand();
        mouth.speek();
    }

    public Robot(){}

    public Robot(Hand hand,Mouth mouth){
        this.hand = hand;
        this.mouth = mouth;
    }
}
