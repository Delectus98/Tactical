package app.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultArraySerializers;

import java.util.ArrayList;


public class ArrayListSerializer extends Serializer<ArrayList<?>> {
    private DefaultArraySerializers.ObjectArraySerializer oas = new DefaultArraySerializers.ObjectArraySerializer();

    public ArrayListSerializer() {
        oas.setElementsAreSameType(false);//Choose true  for optimization if your ArrayLists contain ONLY objects of the same class
        oas.setElementsCanBeNull(true);//can be set to false if all elements are "!= null"
    }

    @Override
    public ArrayList<?> read(Kryo kryo, Input in, Class<ArrayList<?>> bagClass) {
        Object[] content = kryo.readObjectOrNull(in, Object[].class, oas);
        ArrayList<Object> bag = new ArrayList<Object>(content.length);
        for (int i = 0; i < content.length; i++) {
            bag.add(content[i]);
        }
        return bag;
    }

    @Override
    public void write(Kryo kryo, Output out, ArrayList<?> list) {
        kryo.writeObjectOrNull(out, list.toArray(), oas);
    }
}
