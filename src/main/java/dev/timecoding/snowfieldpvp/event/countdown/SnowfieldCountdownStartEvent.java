package dev.timecoding.snowfieldpvp.event.countdown;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SnowfieldCountdownStartEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Integer id;
    private boolean cancelled;

    public SnowfieldCountdownStartEvent(Integer runnableid){
        this.id = runnableid;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
