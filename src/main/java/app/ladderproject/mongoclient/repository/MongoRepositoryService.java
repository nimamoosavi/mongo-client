package app.ladderproject.mongoclient.repository;


import app.ladderproject.crud.repository.GeneralRepository;
import org.bson.types.ObjectId;

public interface MongoRepositoryService<T , I extends ObjectId> extends GeneralRepository<T,I> {
     public void updateField(I id,T t);
}
