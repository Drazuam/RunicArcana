package com.drazuam.omnimancy.common.keybind;

import com.drazuam.omnimancy.common.Omnimancy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import javax.swing.text.JTextComponent;

/**
 * Created by Joel on 2/21/2017.
 */
public class ModKeybind {


    public static KeyBinding keybindChalk;

    public static void init(){
        keybindChalk = new KeyBinding("Chalk GUI", Keyboard.KEY_LMENU, "Omnimancy");
        ClientRegistry.registerKeyBinding(keybindChalk);
    }





}
