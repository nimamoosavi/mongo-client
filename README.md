# Mongo-Client

this project use for connect to mongodb service and implement the repository layer in crud project


![mongo-client Diagram](https://github.com/nimamoosavi/mongo-client/wiki/images/Diagram.png)

#### Requirement

The library works with Java 8+, ladder Core 1.0.1+ and implemented Crud Project

## [Core](https://github.com/nimamoosavi/core/wiki)

## [Crud](https://github.com/nimamoosavi/crud/wiki)

MongoTemplate to provide below feature
> - Implementation of GeneralRepository for [CRUD Project](https://github.com/nimamoosavi/crud/wiki)
> - Sequence service
> - Modify fieldName dynamically before save and after fetch even in runtime
> - relocate nested field of a map object dynamically even in runtime which means before save and after fetch
> - Change field name by content of desired variable


## Hello World

#### Example of default implementation of GeneralRepository

```java

@Component
public class CostHeaderGeneralRepository extends MongoRepositoryServiceImpl<CostHeader, ObjectId> {

}

@Repository
public interface CostHeaderRepository extends MongoRepository<CostHeader, ObjectId> {
}
```

####  if you want to update record without losing previous field ,use updateFields method

```java

@Component
public class CostHeaderService {
    @Autowire
    CostHeaderGeneralRepository costHeaderGeneralRepository;

    public void updateFields(ObjectId id, CostHeader costHeader) {
        costHeaderGeneralRepository.updateField(id, costHeader);
    }

}
```
#### if MongoFieldMapper exists, SimpleMongoRepository will call it before and after CRUD in MongoDB, otherwise continues to its business
#### for providing Sequence ,Move and Rename features, you must generate below bean, This bean allows you to rename and relocate and change fields dynamically
```java

@Component
public class CostHeaderMapper extends MongoFieldMapper<CostHeader> {

}
```
#### if you want to modify filed of document before save or after fetch ,follow the below example
```java
public class MyService{
    @Autowire
    CostHeaderMapper costHeaderMapper;
    public void customFunc(){
        costHeaderMapper.getVariableGeneratorFields().put("dynamicFields.name".split(com.nicico.mongoclient.mapper.MongoFieldMapper.MONGO_FIELD_NAME_SEPARATOR)
                ,
                new ValueModifier<Object>() {
                    @Override
                    public void before(Object o, org.bson.Document doc) {

                    }

                    @Override
                    public void after(org.bson.Document doc) {

                    }
                }
        );
    }

}

```

#### Generate Sequence Number with specific sequenceName and set it to a document before saving.

```java

@Document
public class CostHeader {

    @Sequence(name = "Cost_Header_Number")
    public Long number;

    public void setNumber(Long number) {
        this.number = number;
    }
}
```
###### original document
```json
{
  "number": null
}
```
###### persisted document in MongoDB
```json
{
  "number": 80
}
```
#### if you need sequence number for your business , you can take it with below method.

```java
public class SequenceUtil {
    @AutoWired
    SequenceGeneratorService sequenceGeneratorService;

    public Long getNextSequence(String seqName) {
        return sequenceGeneratorService.generateSequence();
    }
}

```

#### MongoTemplate let you set fields name fixed,
#### if you need setting fields name with content of other field, you can use below example

```java

public class CostRow {
    private String type;
    @FieldName(name = "type")
    private Map<String, Object> dynamicFields;
}

```
###### original document
```json
{
  "type": "train",
  "dynamicFields": {
    "name": "Green Train"
  }
}
```
###### persisted document in MongoDB
```json
{
  "type": "train",
  "train": {
    "name": "Green Train"
  }
}
```

#### if you need to relocate nested fields of a map to everywhere in a document, you can use RelocateField annotation
```java

public class CostRow {
    private String type;
    @RelocateField(source = {"staticFields", "dynamicFields.name"}, target = {"stat", "name"})
    private Map<String, Object> dynamicFields;
}
```
###### original document
```json
{
  "type": "train",
  "staticFields": {
    "price": 1000
  },
  "dynamicFields": {
    "number": 80,
    "name": "Green Train"
  }
}
```
###### persisted document in MongoDB
```json
{
  "type": "train",
  "stat": {
    "price": 1000
  },
  "name": "Green Train",
  "dynamicFields": {
    "number": 80
  }
}
```
