package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import com.sun.javafx.geom.Vec3f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class DetectSymbol extends Symbol {

    public DetectSymbol() {
        super("symbol_detect", SymbolTextures.DETECT, SymbolCategory.DEFAULT);
    }

    @Override
    protected void registerFunctions() {

        outputs.add(new IFunctional() {
            @Override
            public String getName() {
                return "Get Entity";
            }

            @Override
            public List<HashableTuple<String, DataType>> getRequiredInputs() {
                return null;
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {

                DrawnSymbol symbol = (DrawnSymbol)object;

//                AxisAlignedBB searchBox = new AxisAlignedBB(symbol.getDrawnOn(), symbol.getDrawnOn().offset(symbol.getBlockFace(),2));

                BlockPos corner1 = symbol.getDrawnOn().add(-0.2,-0.2,-0.2);
                BlockPos corner2;

                switch(symbol.getBlockFace())
                {
                    case DOWN:
                        corner2 = symbol.getDrawnOn().add(1.2,-0.2,1.2);
                        break;
                    case WEST:
                        corner2 = symbol.getDrawnOn().add(-0.2,1.2,1.2);
                        break;
                    case NORTH:
                        corner2 = symbol.getDrawnOn().add(1.2,1.2,-0.2);
                        break;
                    case UP:
                    case EAST:
                    case SOUTH:
                        corner2 = symbol.getDrawnOn().add(1.2,1.2,1.2);
                        break;
                    default:
                        //TODO: help what the hell is going on for this to happen
                        corner2 = symbol.getDrawnOn().add(1.2,1.2,1.20);
                }

                AxisAlignedBB searchBox = new AxisAlignedBB(corner1,corner2.offset(symbol.getBlockFace()));

                List<LivingEntity> livingEntityList = new ArrayList<>();
                chunk.getEntitiesOfTypeWithinAABB(LivingEntity.class, searchBox, livingEntityList, null);

                if(livingEntityList.size()==0)
                    return null;

                if(symbol.getTicksAlive()%20==0)
                    symbol.applyServerTorque(50, chunk);

                return livingEntityList.get(0);
            }

            @Override
            public DataType getOutputType() {
                return DataType.ENTITY;
            }

            @Override
            public List<HashableTuple<String, DataType>> getTriggers() {
                return null;
            }
        });

    }
}
