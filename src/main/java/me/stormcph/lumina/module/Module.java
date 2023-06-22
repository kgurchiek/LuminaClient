package me.stormcph.lumina.module;

import me.stormcph.lumina.event.EventManager;
import me.stormcph.lumina.notification.NotificationManager;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.KeybindSetting;
import me.stormcph.lumina.utils.chat.ChatUtils;
import me.stormcph.lumina.utils.animations.Direction;
import me.stormcph.lumina.utils.animations.impl.DecelerateAnimation;
import net.minecraft.client.MinecraftClient;
import me.stormcph.lumina.utils.animations.Animation;
import net.minecraft.client.option.KeyBinding;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {

    private String name, description, displayName;
    private boolean hasKeybind = true;
    private boolean saveSettings = true;
    private int key;
    private boolean enabled;
    private Category category;

    private KeyBinding keyBinding;
    private final Animation animation = new DecelerateAnimation(250, 1).setDirection(Direction.BACKWARDS);
    private boolean expanded;

    protected MinecraftClient mc = MinecraftClient.getInstance();

    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.displayName = name;
        this.key = 0;
    }

    public static boolean nullCheck(){
        return MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().player == null;
    }

    public void toggle() {
       this.enabled = !this.enabled;

       if(enabled) {
           onEnable();
       }
       else {
           onDisable();
       }
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void addSettings(Setting...settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public Setting getSetting(String name) {
        for(Setting setting : settings) {
            if(setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }

    public void onEnable() {
        if(nullCheck()) return;
        EventManager.register(this);
        NotificationManager.INSTANCE.registerNotification("§a" + this.name, "was enabled.");
    }
    public void onDisable() {
        if(nullCheck()) return;
        EventManager.unregister(this);
        NotificationManager.INSTANCE.registerNotification("§c" + this.name, "was disabled.");
    }

    public void setEnabled(boolean toggled) {
        this.enabled = toggled;
        if(enabled) {
            onEnable();
        }
        else {
            onDisable();
        }
    }

    public static List<Category> categories = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
        ((KeybindSetting) getSetting("Keybind")).setting$setKey(key);
    }

    public boolean hasKeybind() {
        return this.hasKeybind;
    }

    public void removeKeybind() {
        this.hasKeybind = false;
    }


    public boolean savesSettings() {
        return this.saveSettings;
    }
    public void noSave() {
        this.saveSettings = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    protected void sendMsg(String message) {
        ChatUtils.sendMsg(message, true);
    }

    protected void sendPrefixMsg(String message) {
        //ChatUtils.sendPrefixMessage(message);
    }
    
    public Animation getAnimation() {
        return animation;
    }
}
