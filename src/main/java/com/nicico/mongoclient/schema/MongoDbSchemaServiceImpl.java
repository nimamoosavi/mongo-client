package com.nicico.mongoclient.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.jsonwebtoken.lang.Assert;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
@Component
public class MongoDbSchemaServiceImpl implements MongoDbSchemaService {
    private MongoDatabase database;
    private MongoTemplate mongoTemplate;
    private ObjectMapper objectMapper;

    MongoDbSchemaServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.database = mongoTemplate.getDb().withCodecRegistry(pojoCodecRegistry);
        objectMapper = new ObjectMapper();
    }

    @Override
    public Document saveSchema(String collectionName, Document schema) {
        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put(MONGO_COLL_MOD, collectionName);
        Document validator = retrieveValidatorDocument(collectionName).get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class);
        if (validator == null)
            validator = new Document();
        validator.put(MONGO_SCHEMA, schema);
        jsonSchema.put(MONGO_VALIDATOR, validator);
        return mongoTemplate.executeCommand(new Document(jsonSchema));
    }

    @SneakyThrows
    @Override
    public Boolean saveSchema(String collectionName, FieldValidation schemaFieldValidation) {
        Document result = saveSchema(collectionName, Document.parse(objectMapper.writeValueAsString(schemaFieldValidation)));
        return result.containsKey("ok");
    }

    @Override
    public void drop(String collectionName) {
        this.saveSchema(collectionName, new Document());
    }

    @SneakyThrows
    @Override
    public FieldValidation getJsonSchema(String collectionName) {
        return objectMapper.readValue(getJsonSchemaDocument(collectionName).toJson(), FieldValidation.class);
    }

    @Override
    public Document getJsonSchemaDocument(String collectionName) {
        Document collection = retrieveValidatorDocument(collectionName);
        Assert.notNull(collection, "collection not found");
        return readJsonSchema(collection);
    }

    /**
     * get collection definition document
     *
     * @param collectionName collection name
     * @return collection definition document
     */
    public Document retrieveValidatorDocument(String collectionName) {
        return database.listCollections().filter(Filters.eq("name", collectionName)).first();
    }

    /**
     * @param collection collection definition
     * @return document in $jsonSchema
     */
    private static Document readJsonSchema(Document collection) {
        return (collection.containsKey(MONGO_OPTION)
                && collection.get(MONGO_OPTION, Document.class).containsKey(MONGO_VALIDATOR) &&
                collection.get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class).containsKey(MONGO_SCHEMA))
                ? collection.get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class).get(MONGO_SCHEMA, Document.class) : new Document();
    }

    @Override
    public Boolean saveSchema(String collectionName, Map<String, FieldPropertyDTO> schemaFieldValidation) {
        FieldValidation fieldValidation = getJsonSchema(collectionName);
        for (String fieldKey : schemaFieldValidation.keySet()) {
            String[] fieldName = fieldKey.split(MONGO_FIELD_NAME_SEPARATOR);
            FieldValidation schemaField = getTargetSchema(fieldValidation, fieldName);
            FieldPropertyDTO dto = schemaFieldValidation.get(fieldKey);
            castDtoToValidation(schemaField, dto);
            FieldValidation parentSchemaField = getTargetSchema(fieldValidation, Arrays.copyOfRange(fieldName, 0, fieldName.length - 1));
            if (parentSchemaField.getRequiredFields() == null)
                parentSchemaField.setRequiredFields(new HashSet<>());
            if (dto.getRequired()) {
                parentSchemaField.getRequiredFields().add(fieldName[fieldName.length-1 ]);
            } else {
                parentSchemaField.getRequiredFields().remove(fieldName[fieldName.length-1 ]);
            }

        }
        return saveSchema(collectionName, fieldValidation);
    }

    private void castDtoToValidation(FieldValidation schemaField, FieldPropertyDTO dto) {
        schemaField.setMinimum(dto.getMinimum() == null ? null : dto.getMinimum().doubleValue());
        schemaField.setMaximum(dto.getMaximum() == null ? null : dto.getMaximum().doubleValue());
        schemaField.setEnums(dto.getEnums());
        schemaField.setPattern(dto.getPattern());
        schemaField.setDescription(dto.getDescription());
        schemaField.setType(Collections.singleton(dto.getType()));
    }

    private FieldValidation getTargetSchema(FieldValidation schema, String[] nestedFieldName) {
        if (nestedFieldName != null)
            for (String fieldName : nestedFieldName) {
                if (schema.getProperties() == null)
                    schema.setProperties(new HashMap<>());
                if (!schema.getProperties().containsKey(fieldName))
                    schema.getProperties().put(fieldName, FieldValidation.builder().properties(new HashMap<>()).build());
                schema = schema.getProperties().get(fieldName);
            }

        return schema;
    }

}
