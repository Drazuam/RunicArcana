package com.drazuam.runicarcana.common.keybind;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * Created by Joel on 2/21/2017.
 */
public class ModKeybind {


    public static KeyBinding keybindChalk;

    public static void init(){
        keybindChalk = new KeyBinding("Chalk GUI", Keyboard.KEY_LMENU, "RunicArcana");
        ClientRegistry.registerKeyBinding(keybindChalk);
    }





}
