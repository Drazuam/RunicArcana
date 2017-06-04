package com.drazuam.omnimancy.common.enchantment.Signals;

import com.drazuam.omnimancy.common.enchantment.Signals.ModSignal;
import net.minecraft.world.BossInfo;

/**
 * Created by Joel on 2/23/2017.
 */
public interface ISignal {
    public Object doFunction(Object... args);

}
