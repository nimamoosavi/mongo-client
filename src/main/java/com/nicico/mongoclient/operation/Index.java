package com.nicico.mongoclient.operation;

import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexFilter;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Index implements IndexDefinition {
    private final Map<String, Sort.Direction> fieldSpec = new LinkedHashMap<>();
    private @Nullable
    String name;
    private boolean unique;
    private boolean sparse;
    private boolean background;
    private long expire;
    private Optional<IndexFilter> filter;
    private Optional<Collation> collation;


    public Index(@Nullable String name, boolean unique, boolean sparse, boolean background, long expire, IndexFilter filter, Collation collation, @NonNull String key, @NonNull Sort.Direction direction) {
        this.name = name;
        this.unique = unique;
        this.sparse = sparse;
        this.background = background;
        this.expire = expire;
        this.filter = Optional.ofNullable(filter);
        this.collation = Optional.ofNullable(collation);
        fieldSpec.put(key, direction);
    }

    @Override
    public Document getIndexKeys() {
        Document document = new Document();

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

        public static IndexBuilder builder() {
            return new IndexBuilder();
        }

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
            return new Index(this.name, this.unique, this.sparse, this.background, this.expire, this.filter, this.collation, this.key, this.direction);
        }
    }

    public Map<String, Sort.Direction> getFieldSpec() {
        return fieldSpec;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isSparse() {
        return sparse;
    }

    public void setSparse(boolean sparse) {
        this.sparse = sparse;
    }

    public boolean isBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public Optional<IndexFilter> getFilter() {
        return filter;
    }

    public void setFilter(IndexFilter filter) {
        this.filter = Optional.ofNullable(filter);
    }

    public Optional<Collation> getCollation() {
        return collation;
    }

    public void setCollation(Collation collation) {
        this.collation = Optional.ofNullable(collation);
    }
}
