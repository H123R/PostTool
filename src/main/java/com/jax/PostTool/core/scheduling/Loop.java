package com.jax.PostTool.core.scheduling;

public class Loop {

    public static interface LoopInterface {

        void exe();

    }

    public static interface ConsumerLoopInterface {

        void exe(Integer i);

    }

    public static void loop(LoopInterface loopInterface, Integer count) {
        for (int i = 0; i < count; i++) {
            loopInterface.exe();
        }
    }
    public static void loop(ConsumerLoopInterface loopInterface, Integer count) {
        for (int i = 0; i < count; i++) {
            loopInterface.exe(i);
        }
    }

}
