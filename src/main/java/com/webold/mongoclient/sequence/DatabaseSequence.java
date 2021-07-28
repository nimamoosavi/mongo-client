package com.webold.mongoclient.sequence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatabaseSequence {
    @Id
    private String id;

    private long seq;
}
