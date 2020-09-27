package net.bfcode.bfbase.util.menu.slot;

import java.util.EnumSet;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

public class ClickOptions {
	
    public static ClickOptions ALLOW_ALL;
    public static ClickOptions DENY_ALL;
    private EnumSet<InventoryAction> allowedActions;
    private EnumSet<ClickType> allowedClickTypes;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public boolean isAllowedAction(final InventoryAction action) {
        return this.allowedActions.contains(action);
    }
    
    public boolean isAllowedClickType(final ClickType clickType) {
        return this.allowedClickTypes.contains(clickType);
    }
    
    static {
        ClickOptions.ALLOW_ALL = builder().allActions().allClickTypes().build();
        ClickOptions.DENY_ALL = builder().build();
    }
    
    public static class Builder
    {
        private EnumSet<InventoryAction> allowedActions;
        private EnumSet<ClickType> allowedClickTypes;
        
        private Builder() {
            this.allowedActions = EnumSet.noneOf(InventoryAction.class);
            this.allowedClickTypes = EnumSet.noneOf(ClickType.class);
        }
        
        public Builder allActions() {
            this.allowedActions = EnumSet.allOf(InventoryAction.class);
            return this;
        }
        
        public Builder allClickTypes() {
            this.allowedClickTypes = EnumSet.allOf(ClickType.class);
            return this;
        }
        
        public Builder allow(final InventoryAction action) {
            this.allowedActions.add(action);
            return this;
        }
        
        public Builder allow(final InventoryAction... actions) {
            for (final InventoryAction action : actions) {
                this.allow(action);
            }
            return this;
        }
        
        public Builder allow(final ClickType clickType) {
            this.allowedClickTypes.add(clickType);
            return this;
        }
        
        public Builder allow(final ClickType... clickTypes) {
            for (final ClickType type : clickTypes) {
                this.allow(type);
            }
            return this;
        }
        
        public ClickOptions build() {
            final ClickOptions options = new ClickOptions();
            options.allowedActions = this.allowedActions;
            options.allowedClickTypes = this.allowedClickTypes;
            return options;
        }
    }
}
