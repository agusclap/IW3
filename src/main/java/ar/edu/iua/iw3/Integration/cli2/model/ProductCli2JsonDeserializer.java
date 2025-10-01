package ar.edu.iua.iw3.integration.cli2.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ar.edu.iua.iw3.integration.cli2.model.ComponentCli2;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.JsonUtiles;

public class ProductCli2JsonDeserializer extends StdDeserializer<ProductCli2> {

    private static final long serialVersionUID = 3337415861947752725L;

    private final ICategoryBusiness categoryBusiness;

    public ProductCli2JsonDeserializer(Class<?> vc, ICategoryBusiness categoryBusiness) {
        super(vc);
        this.categoryBusiness = categoryBusiness;
    }

    @Override
    public ProductCli2 deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        ProductCli2 product = new ProductCli2();

        String name = JsonUtiles.getString(node, "product,description,product_description,product_name".split(","), null);
        if (name == null || name.isBlank()) {
            throw JsonMappingException.from(jp, "La descripción del producto es obligatoria");
        }
        product.setProduct(name);

        product.setPrecio(JsonUtiles.getDouble(node, "price,precio,product_price".split(","), 0d));
        product.setStock(JsonUtiles.getBoolean(node, "stock,in_stock,available".split(","), false));

        String expirationRaw = JsonUtiles.getString(node, "expirationDate,expiration_date,expires_at".split(","), null);
        if (expirationRaw != null && !expirationRaw.isBlank()) {
            product.setExpirationDate(parseDate(expirationRaw));
        }

        String categoryName = JsonUtiles
                .getString(node, "category,product_category,category_product".split(","), null);
        if (categoryName != null && !categoryName.isBlank() && categoryBusiness != null) {
            try {
                product.setCategory(categoryBusiness.load(categoryName));
            } catch (NotFoundException | BusinessException e) {
                // Ignorar categorías inválidas y continuar
            }
        }

        if (node.get("components") != null && node.get("components").isArray()) {
            Set<ComponentCli2> components = new HashSet<>();
            for (JsonNode componentNode : node.get("components")) {
                String componentName = JsonUtiles
                        .getString(componentNode, "component,name,component_name".split(","), null);
                if (componentName != null && !componentName.isBlank()) {
                    ComponentCli2 component = new ComponentCli2();
                    component.setComponent(componentName);
                    components.add(component);
                }
            }
            if (!components.isEmpty()) {
                product.setComponents(components);
            }
        }

        return product;
    }

    private Date parseDate(String value) throws JsonMappingException {
        try {
            return Date.from(Instant.parse(value));
        } catch (DateTimeParseException ex) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                return df.parse(value);
            } catch (ParseException e) {
                throw JsonMappingException.fromUnexpectedIOE(new IOException("Formato de fecha inválido: " + value));
            }
        }
    }
}
