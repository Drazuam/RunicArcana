package com.drazuam.omnimancy.common.enchantment.Signals;

import java.io.Serializable;

/**
 * Created by Joel on 2/26/2017.
 */
public class CompiledSymbol implements Serializable{

    public short ID;
    public byte[] connections;
    public String variable;

    public CompiledSymbol(short newID, int connectionNumber)
    {
        ID = newID;
        connections = new byte[connectionNumber*3];
        variable = "";
    }

    public CompiledSymbol(short newID, byte[] newConnections)
    {
        ID = newID;
        connections = newConnections.clone();
    }

    public CompiledSymbol(short newID, int connectionNumber, String newVariable)
    {
        ID = newID;
        connections = new byte[connectionNumber*3];
        variable = newVariable;
    }

    public void addAction(byte signalthis, byte signalother, byte dust, int number)
    {
        connections[number*3]   = dust;
        connections[number*3+1] = signalthis;
        connections[number*3+2] = signalother;
    }

}
