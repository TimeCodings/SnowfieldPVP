package dev.timecoding.snowfieldpvp.event.countdown;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SnowfieldCountdownEndEvent extends Event{
    
    private static final HandlerList handlers = new HandlerList();
    
    private Integer id;
    
    public SnowfieldCountdownEndEvent(Integer runnableid){
        this.id = runnableid;
    }
    
    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
