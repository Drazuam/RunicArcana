package com.latenighters.runicarcana.common.arcana;

public class ArcanaChamber {
    final protected ArcanaMix storedArcana = new ArcanaMix();
    public final int maxStorage;
    protected int currentArcanaTotal = 0;
    private final int id;

    public ArcanaChamber(int maxStorage, int id) {
        this.maxStorage = maxStorage;
        this.id = id;
    }

    public int getArcanaAmount()
    {
        return currentArcanaTotal;
    }

    public ArcanaMix removeArcana(int amount)
    {
        return this.storedArcana.removeAmount(amount);
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

        return mix;
    }
}
