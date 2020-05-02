package com.latenighters.runicarcana.common.symbols.backend;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolSyncer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.registries.RegistryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DrawnSymbol implements INBTSerializable<CompoundNBT>, IFunctionalObject {

    protected Symbol symbol;
    protected BlockPos drawnOn;
    protected Direction blockFace;
    protected Chunk chunk = null;
    protected int tickCounter = 0;
    protected int workDone = 0;
    protected float workVelocity = 0;
    protected int workShown = 0;
    protected boolean hasUnloadedLinkedInputs = false;

    private static final int SEND_WORK_RANGE = 4;
    private static final float mass              = 50f;
    private static final float constant_drag     = 0.005f;
    private static final float proportional_drag = 0.005f;

    public Map<HashableTuple<String,DataType>, HashableTuple<IFunctionalObject,IFunctional>> linkedInputs = new HashMap<>();

    public World world;

    public DrawnSymbol(CompoundNBT lnbt) {
        this.deserializeNBT(lnbt);
    }

    public DrawnSymbol() {
    }

    @Override
    public BlockPos getBlockPos() {
        return getDrawnOn();
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public BlockPos getDrawnOn() {
        return drawnOn;
    }

    public Direction getBlockFace() {
        return blockFace;
    }

    public ResourceLocation getTexture() {
        return this.symbol.texture;
    }

    public Chunk getChunk(){ return this.chunk; }

    @Override
    public List<IFunctional> getOutputs() {
        return symbol.getOutputs();
    }

    @Override
    public List<HashableTuple<String,DataType>> getInputs() {
        return symbol.getInputs();
    }

    @Override
    public List<IFunctional> getFunctions() {
        return symbol.getFunctions();
    }

    @Override
    public List<HashableTuple<HashableTuple<String, DataType>, IFunctional>> getTriggers() {
        return symbol.getTriggers();
    }

    @Override
    public Map<HashableTuple<String, DataType>, HashableTuple<IFunctionalObject, IFunctional>> getInputLinks() {
        return linkedInputs;
    }

    @Override
    public String getObjectType() {
        return "DrawnSymbol";
    }

    public DrawnSymbol(Symbol symbol, BlockPos drawnOn, Direction blockFace, Chunk chunk) {
        this.symbol = symbol;
        this.drawnOn = drawnOn;
        this.blockFace = blockFace;
        this.world = chunk.getWorld();

        this.chunk = chunk;

    }

    public CompoundNBT basicSerializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("symbol", symbol.getRegistryName().toString());
        nbt.merge(NBTUtil.writeBlockPos(drawnOn));
        nbt.putInt("face", blockFace.getIndex());

        return nbt;
    }
    @Override
    public CompoundNBT serializeNBT() {

        CompoundNBT nbt = basicSerializeNBT();

        ListNBT symbols = new ListNBT();

        if(linkedInputs==null)return nbt;

        linkedInputs.forEach((input, link) ->{
            CompoundNBT linkNBT = new CompoundNBT();
            String className = FunctionalObjects.getName(link.getA().getClass());
            if(className==null)
                return;
            linkNBT.putString("name",input.getA());
            linkNBT.putString("type",input.getB().name);

            linkNBT.put("object",link.getA().basicSerializeNBT());
            linkNBT.putString("function",link.getB().getName());

            linkNBT.putString("class",className);
            symbols.add(linkNBT);
        });
        nbt.put("inputs",symbols);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        drawnOn = NBTUtil.readBlockPos(nbt);
        symbol = RegistryManager.ACTIVE.getRegistry(Symbol.class).getValue(new ResourceLocation(nbt.getString("symbol")));
        blockFace = Direction.byIndex(nbt.getInt("face"));

        if(nbt.contains("inputs"))
        {
            ListNBT list = nbt.getList("inputs",10);
            list.iterator().forEachRemaining(obj ->{
                this.hasUnloadedLinkedInputs = true;
                CompoundNBT entry = (CompoundNBT)obj;
                IFunctionalObject object = FunctionalObjects.getNewObject(entry.getString("class"));
                if(object==null)return;
                object.deserializeNBT((CompoundNBT)entry.get("object"));
                HashableTuple<String,DataType> input = null; // = new HashableTuple<>(entry.getString("name"), DataType.getDataType(entry.getString("type")));
                for(HashableTuple<String,DataType> symbolInput : this.getInputs())
                {
                    if(symbolInput.getA().equals(entry.getString("name")) && symbolInput.getB().equals(DataType.getDataType((entry.getString("type")))))
                        input = symbolInput;
                }
                //IndexibleTuple<IFunctionalObject,IFunctional>>
                HashableTuple<IFunctionalObject, IFunctional> functionalObject = new HashableTuple<>(object,new DummyFunction(entry.getString("function")));
                if(input!=null)
                this.linkedInputs.put(input,functionalObject);
            });
        }
    }



    @Override
    public IFunctionalObject findReal(Chunk chunk) {
        AtomicReference<DrawnSymbol> realSymbol = new AtomicReference<>();
        chunk.getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler ->{
            realSymbol.set(symbolHandler.getSymbolAt(this.drawnOn, this.blockFace));
        });
        return realSymbol.get();
    }

    public boolean tryToLinkInputs(Chunk chunk)
    {
        this.chunk = chunk;
        AtomicBoolean gotAll = new AtomicBoolean(true);

        ArrayList<HashableTuple<String,DataType>> toRemove = new ArrayList<>();

        this.linkedInputs.forEach((input,objectSet) ->{
            if((objectSet.getB() instanceof DummyFunction))
            {
                if(objectSet.getA() instanceof DrawnSymbol)
                {
                    World world = chunk.getWorld();
                    IChunk symbolIChunk = world.getChunk(((DrawnSymbol) objectSet.getA()).drawnOn);
                    if(symbolIChunk instanceof Chunk)
                    {
                        ((Chunk) symbolIChunk).getCapability(RunicArcana.SYMBOL_CAP).ifPresent(cap->{
                            DrawnSymbol symbolToAdd = cap.getSymbolAt(((DrawnSymbol) objectSet.getA()).drawnOn, ((DrawnSymbol) objectSet.getA()).blockFace);
                            if(symbolToAdd==null)
                            {
                                toRemove.add(input);
                                return;
                            }
                            //TODO use a reference here you dumbass
                            ArrayList<IFunctional> functionToAdd = new ArrayList<>();
                            symbolToAdd.getOutputs().forEach(output ->{
                                if(output.getName().equals(objectSet.getB().getName()))
                                    functionToAdd.add(output);
                            });
                            if (functionToAdd.size()==0)
                            {
                                return;
                            }
                            HashableTuple<IFunctionalObject,IFunctional> objectSetToAdd = new HashableTuple<>(symbolToAdd,functionToAdd.get(0));
                            this.linkedInputs.put(input, objectSetToAdd);
                        });
                    }
                    else
                    {
                        gotAll.set(false);
                    }
                }
            }
            //TODO: add code for checking other types of Functional Objects here
        });
        toRemove.forEach(removal ->{
            this.linkedInputs.remove(removal);
        });

        return gotAll.get();
    }

    public void encode(final PacketBuffer buf) {
        this.symbol.encode(this,buf);
    }

    public void decode(final PacketBuffer buf) {
        this.symbol.decode(this,buf);
    }

    //generate a new Drawn Symbol from a packetBuffer
    public DrawnSymbol(final PacketBuffer buf, final Symbol symbol) {
        this.symbol = symbol;
        this.decode(buf);
    }

    public void tick(World world, Chunk chunk)
    {
        if(this.hasUnloadedLinkedInputs) this.hasUnloadedLinkedInputs = this.tryToLinkInputs(chunk);
        this.tickCounter++;
        this.getSymbol().onTick(this, world, chunk, this.getDrawnOn(), this.getBlockFace());

        //TODO do this stuff based on render ticks, not game ticks
        workVelocity += ((float)workDone)/mass - proportional_drag*workVelocity;
        if(workVelocity>0)
            workVelocity -= constant_drag;
        if(workVelocity<0)
            workVelocity = 0;
        workShown += workVelocity;

        workDone = 0;
    }

    public void applyWork(int work)
    {
        workDone += work;
    }

    public void applyServerTorque(int work, Chunk chunk)
    {
        for(PlayerEntity player : chunk.getWorld().getPlayers())
        {
            if(player.chunkCoordX > chunk.getPos().x - SEND_WORK_RANGE && player.chunkCoordX < chunk.getPos().x + SEND_WORK_RANGE &&
                    player.chunkCoordZ > chunk.getPos().z - SEND_WORK_RANGE && player.chunkCoordZ < chunk.getPos().z + SEND_WORK_RANGE)
            {
                SymbolSyncer.AddWorkMessage msg = new SymbolSyncer.AddWorkMessage(work, this, chunk.getPos());
                SymbolSyncer.INSTANCE.sendTo( msg, ((ServerPlayerEntity)(player)).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }



    public int getWork()
    {
        return workShown;
    }

    public int getTicksAlive() {
        return tickCounter;
    }
}