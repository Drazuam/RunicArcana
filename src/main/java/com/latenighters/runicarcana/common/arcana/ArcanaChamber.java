package com.latenighters.runicarcana.common.arcana;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class ArcanaChamber implements INBTSerializable<CompoundNBT> {
    final protected ArcanaMix storedArcana = new ArcanaMix();
    private int maxStorage;
    protected int currentArcanaTotal = 0;
    public int id;

    public ArcanaChamber(int maxStorage, int id) {
        this.maxStorage = maxStorage;
        this.id = id;
    }

    public ArcanaChamber(CompoundNBT nbt) {
        this.deserializeNBT(nbt);
    }

    public int getArcanaAmount()
    {
        return currentArcanaTotal;
    }

    public ArcanaMix removeArcana(int amount)
    {
        ArcanaMix retval = this.storedArcana.removeAmount(amount);
        this.currentArcanaTotal = this.storedArcana.getTotal();
        return retval;
    }

    public void transferToArcanaChamber(ArcanaChamber chamber, int amount)
    {
        ArcanaMix toTransfer = this.removeArcana(amount);
        chamber.addArcana(toTransfer);

        //put the leftovers back in
        this.addArcana(toTransfer);
    }

    //returns leftover after adding operation
    public ArcanaMix addArcana(ArcanaMix mix)
    {
        //if the mix we're trying to add doesn't have anything in it, dont add anything
        if(mix.getTotal()<=0)
            return mix;

        //if we're at capacity, then the entire addition is leftover
        if(currentArcanaTotal >= maxStorage)
            return mix;

        //if we're adding too much, we need to only add a portion of the mix
        if(currentArcanaTotal + mix.getTotal() > maxStorage){
            this.storedArcana.add(mix.removeAmount(maxStorage-currentArcanaTotal));
        }
        //otherwise just add the mix wholesale
        else
        {
            this.storedArcana.add(mix);
        }

        this.currentArcanaTotal = this.storedArcana.getTotal();

        return mix;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("id",id);
        nbt.putInt("max_arcana",this.maxStorage);
        nbt.put("arcana",this.storedArcana.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.maxStorage = nbt.getInt("max_arcana");
        this.id = nbt.getInt("id");
        this.storedArcana.zeroize();
        this.storedArcana.deserializeNBT(nbt.getList("arcana",10));
        this.currentArcanaTotal = this.storedArcana.getTotal();
    }
}
