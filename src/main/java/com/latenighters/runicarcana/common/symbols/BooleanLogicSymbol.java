package com.latenighters.runicarcana.common.symbols;

import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.categories.SymbolCategory;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class BooleanLogicSymbol extends Symbol {
    public BooleanLogicSymbol() {
        super("symbol_boolean_logic", SymbolTextures.BOOLEAN_LOGIC, SymbolCategory.DEFAULT);
    }

    @Override
    protected void registerFunctions() {

        HashableTuple<String,DataType> AInput = new HashableTuple<>("A",DataType.BOOLEAN);
        HashableTuple<String,DataType> BInput = new HashableTuple<>("B", DataType.BOOLEAN);
        List<HashableTuple<String, DataType>> requiredInputs = new ArrayList<>();
        requiredInputs.add(AInput);
        requiredInputs.add(BInput);

        this.outputs.add(new IFunctional() {
            @Override
            public String getName() {
                return "And";
            }

            @Override
            public List<HashableTuple<String, DataType>> getRequiredInputs() {
                return requiredInputs;
            }

            @Override
            public String getOutputString(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args){
                Object res = executeInWorld(object, chunk, args);
                if (res == null) return new String("null");
                if ((Boolean) res){
                    return new String("True");
                }else{
                    return new String("False");
                }
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {

                Boolean A = null;
                Boolean B = null;

                //TODO: implement an args helper or something
                for(HashableTuple<String, Object> arg : args)
                {
                    switch(arg.getA()) {
                        case "A":
                            A = (Boolean) arg.getB();
                            break;
                        case "B":
                            B = (Boolean) arg.getB();
                            break;
                        default:
                    }
                }

                if(A==null||B==null) return null;

                return A&&B;

            }

            @Override
            public DataType getOutputType() {
                return DataType.BOOLEAN;
            }

            @Override
            public List<HashableTuple<String, DataType>> getTriggers() {
                return null;
            }
        });

        this.outputs.add(new IFunctional() {
            @Override
            public String getName() {
                return "Or";
            }

            @Override
            public List<HashableTuple<String, DataType>> getRequiredInputs() {
                return requiredInputs;
            }

            @Override
            public String getOutputString(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args){
                Object res = executeInWorld(object, chunk, args);
                if (res == null) return new String("null");
                if ((Boolean) res){
                    return new String("True");
                }else{
                    return new String("False");
                }
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {

                Boolean A = null;
                Boolean B = null;

                //TODO: implement an args helper or something
                for(HashableTuple<String, Object> arg : args)
                {
                    switch(arg.getA()) {
                        case "A":
                            A = (Boolean) arg.getB();
                            break;
                        case "B":
                            B = (Boolean) arg.getB();
                            break;
                        default:
                    }
                }

                if(A==null)return B;
                if(B==null)return A;

                return A||B;

            }

            @Override
            public DataType getOutputType() {
                return DataType.BOOLEAN;
            }

            @Override
            public List<HashableTuple<String, DataType>> getTriggers() {
                return null;
            }
        });

        this.outputs.add(new IFunctional() {
            @Override
            public String getName() {
                return "Not";
            }

            @Override
            public List<HashableTuple<String, DataType>> getRequiredInputs() {
                return requiredInputs;
            }

            @Override
            public String getOutputString(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args){
                Object res = executeInWorld(object, chunk, args);
                if (res == null) return new String("null");
                if ((Boolean) res){
                    return new String("True");
                }else{
                    return new String("False");
                }
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {

                Boolean A = null;

                //TODO: implement an args helper or something
                for(HashableTuple<String, Object> arg : args)
                {
                    switch(arg.getA()) {
                        case "A":
                            A = (Boolean) arg.getB();
                            break;
                        default:
                    }
                }

                if(A==null)return null;


                return !A;

            }

            @Override
            public DataType getOutputType() {
                return DataType.BOOLEAN;
            }

            @Override
            public List<HashableTuple<String, DataType>> getTriggers() {
                return null;
            }
        });

        this.outputs.add(new IFunctional() {
            @Override
            public String getName() {
                return "Xor";
            }

            @Override
            public List<HashableTuple<String, DataType>> getRequiredInputs() {
                return requiredInputs;
            }

            @Override
            public String getOutputString(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args){
                Object res = executeInWorld(object, chunk, args);
                if (res == null) return new String("null");
                if ((Boolean) res){
                    return new String("True");
                }else{
                    return new String("False");
                }
            }

            @Override
            public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {

                Boolean A = null;
                Boolean B = null;

                //TODO: implement an args helper or something
                for(HashableTuple<String, Object> arg : args)
                {
                    switch(arg.getA()) {
                        case "A":
                            A = (Boolean) arg.getB();
                            break;
                        case "B":
                            B = (Boolean) arg.getB();
                            break;
                        default:
                    }
                }

                if(A==null)return null;
                if(B==null)return null;

                return A^B;

            }

            @Override
            public DataType getOutputType() {
                return DataType.BOOLEAN;
            }

            @Override
            public List<HashableTuple<String, DataType>> getTriggers() {
                return null;
            }
        });
    }
}
