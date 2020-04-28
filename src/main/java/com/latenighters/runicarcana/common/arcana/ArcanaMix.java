package com.latenighters.runicarcana.common.arcana;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.min;
import static java.lang.Math.max;

public class ArcanaMix{
    private final HashMap<ArcanaType,Integer> arcanaMixMap = new HashMap<>();
    private int totalArcana = 0;

    public static final ArcanaMix COMMON = new ArcanaMix()
            .add(ArcanaType.AIR,1000)
            .add(ArcanaType.WATER, 400)
            .add(ArcanaType.FIRE, 100)
            .add(ArcanaType.EARTH, 100);

    public ArcanaMix() {
    }

    public ArcanaMix(ArcanaMix mix) {
        this.add(mix);
    }

    public Integer getTotal()
    {
        return totalArcana;
    }


    public Integer get(ArcanaType type) {
        return arcanaMixMap.getOrDefault(type,0);
    }

    //returns amount that has been removed
    public int remove(ArcanaType type, Integer amount)
    {
        int typeAmount = this.arcanaMixMap.getOrDefault(type,0);
        int removeAmount = max(amount,typeAmount);

        if (amount >= typeAmount) this.arcanaMixMap.remove(type);
        else this.arcanaMixMap.put(type,typeAmount-removeAmount);

        this.totalArcana-=removeAmount;
        return removeAmount;
    }

    public ArcanaMix add(ArcanaType type, Integer amount)
    {
        arcanaMixMap.put(type,arcanaMixMap.getOrDefault(type,0) + amount);
        totalArcana+=amount;
        return this;
    }

    public void zeroize()
    {
        this.arcanaMixMap.clear();
    }

    public void add(ArcanaMix mix)
    {
        mix.arcanaMixMap.forEach(this::add);
        mix.zeroize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return this.mult(1f);
    }

    public ArcanaMix mult(Float scale)
    {
        ArcanaMix scaledMix = new ArcanaMix(this);
        scaledMix.arcanaMixMap.forEach((type,amount)->{
            scaledMix.arcanaMixMap.put(type,(int)(amount*scale));
        });
        return scaledMix;
    }

    public ArcanaMix removeAmount(Integer amount)
    {
        //if the amount to be removed is higher than the total amount in the mix, remove everything
        amount = min(amount,this.totalArcana);

        //we need to remove a set amount that is spread out over the various types
        //to do this, we first calculate the percentage of the total we must remove
        float percentToRemove = ((float)amount)/this.totalArcana;

        //also keep track of amount removed
        AtomicInteger amountRemoved = new AtomicInteger();

        //and last make a new mixture to return
        ArcanaMix newMix = new ArcanaMix();

        //now go through each type and remove the calculated amount, adding to the amount removed
        this.arcanaMixMap.forEach((type,typeAmount)->
        {
            int toRemove = (int)(percentToRemove*typeAmount);
            int typeAmountRemoved = this.remove(type, toRemove);
            amountRemoved.addAndGet(typeAmountRemoved);
            newMix.add(type,typeAmountRemoved);
        });

        //now just keep removing random bits until we've removed enough
        //this is just to compensate for rounding errors
        Object[] keys = this.arcanaMixMap.keySet().toArray();
        Random rand = new Random();
        while(amountRemoved.get()<amount)
        {
            Object key = keys[rand.nextInt(keys.length)];
            int removed = this.remove((ArcanaType) key,1);
            amountRemoved.addAndGet(removed);
            newMix.add((ArcanaType) key,removed);
        }

        return newMix;
    }
}