package ar.edu.iua.iw3.integration.cli2.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ComponentCli2JsonSerializer extends StdSerializer<ComponentCli2> {

    protected ComponentCli2JsonSerializer(Class<ComponentCli2> t) {
        super(t);
        
    }

    @Override
    public void serialize(ComponentCli2 arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
        arg1.writeStartObject(); // {
        arg1.writeNumberField("id", arg0.getId());
        arg1.writeStringField("component", arg0.getComponent());
        arg1.writeEndObject(); // }
        
    }

}
