package cc.carm.outsource.plugin.parkourcompetition.util;

import cc.carm.outsource.plugin.parkourcompetition.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CountdownRunnable extends BukkitRunnable {

    protected final int fullTime;
    protected final @NotNull Consumer<Integer> timeConsumer;
    protected final @Nullable Runnable afterAll;

    private int time;

    public CountdownRunnable(int fullTime, @NotNull Consumer<Integer> timeConsumer, @Nullable Runnable afterAll) {
        this.fullTime = fullTime;
        this.timeConsumer = timeConsumer;
        this.afterAll = afterAll;
    }

    @Override
    public void run() {
        timeConsumer.accept(fullTime - time);
        time++;

        if (time >= fullTime) {
            if (afterAll != null) afterAll.run();
            this.cancel();
        }

    }


    public static @NotNull CountdownRunnable start(int fullTime,
                                                   @NotNull Consumer<Integer> timeConsumer,
                                                   @Nullable Runnable afterAll) {
        CountdownRunnable cr = new CountdownRunnable(fullTime, timeConsumer, afterAll);
        cr.runTaskTimer(Main.getInstance(), 0, 20);
        return cr;
    }

    public static @NotNull CountdownRunnable start(int fullTime,
                                                   @NotNull Consumer<Integer> timeConsumer) {
        return start(fullTime, timeConsumer, null);
    }

}