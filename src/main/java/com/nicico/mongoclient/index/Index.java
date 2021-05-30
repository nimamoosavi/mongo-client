package com.nicico.mongoclient.index;

import lombok.*;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexFilter;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Index implements IndexDefinition {
    private final Map<String, Sort.Direction> fieldSpec = new LinkedHashMap<>();
    @Nullable
    private String name;
    private boolean unique;
    private boolean sparse;
    private boolean background;
    private long expire;
    private Optional<IndexFilter> filter;
    private Optional<Collation> collation;
    private String key;
    private Sort.Direction direction;

    @Override
    public Document getIndexKeys() {
        Document document = new Document();
        fieldSpec.put(key,direction);
        for (Map.Entry<String, Sort.Direction> entry : fieldSpec.entrySet()) {
            document.put(entry.getKey(), Sort.Direction.ASC.equals(entry.getValue()) ? 1 : -1);
        }
        return document;
    }

    @Override
    public Document getIndexOptions() {
        Document document = new Document();
        if (StringUtils.hasText(name)) {
            document.put("name", name);
        }
        if (unique) {
            document.put("unique", this.unique);
        }
        if (sparse) {
            document.put("sparse", this.sparse);
        }
        if (background) {
            document.put("background", this.background);
        }
        if (expire >= 0) {
            document.put("expireAfterSeconds", this.expire);
        }
        filter.ifPresent(val -> document.put("partialFilterExpression", val.getFilterObject()));
        collation.ifPresent(val -> document.append("collation", val.toDocument()));
        return document;
    }

    public static IndexBuilder builder() {
        return new IndexBuilder();
    }

    public static class IndexBuilder {
        private String key;
        private Sort.Direction direction;
        private @Nullable
        String name;
        private boolean unique = false;
        private boolean sparse = false;
        private boolean background = false;
        private long expire = -1;
        private IndexFilter filter;
        private Collation collation;


        public IndexBuilder key(String key) {
            this.key = key;
            return this;
        }

        public IndexBuilder direction(Sort.Direction direction) {
            this.direction = direction;
            return this;
        }

        public IndexBuilder name(String name) {
            this.name = name;
            return this;
        }

        public IndexBuilder unique(boolean unique) {
            this.unique = unique;
            return this;
        }

        public IndexBuilder sparse(boolean sparse) {
            this.sparse = sparse;
            return this;
        }

        public IndexBuilder background(boolean background) {
            this.background = background;
            return this;
        }

        public IndexBuilder expire(Long expire) {
            this.expire = expire;
            return this;
        }

        public IndexBuilder filter(IndexFilter filter) {
            this.filter = filter;
            return this;
        }

        public IndexBuilder collation(Collation collation) {
            this.collation = collation;
            return this;
        }

        public Index build() {
            return new Index(name, unique, sparse, background, expire, Optional.ofNullable(filter), Optional.ofNullable(collation),key,direction);
        }
    }

}
