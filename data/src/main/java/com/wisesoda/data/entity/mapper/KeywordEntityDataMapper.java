package com.wisesoda.data.entity.mapper;

import com.wisesoda.data.entity.KeywordEntity;
import com.wisesoda.domain.Keyword;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class KeywordEntityDataMapper {

    @Inject
    public KeywordEntityDataMapper() {}

    public Keyword transform(KeywordEntity keywordEntity) {
        Keyword keyword = new Keyword();

        if (keywordEntity != null) {
            keyword.setId(keywordEntity.getId());
            keyword.setCategory(keywordEntity.getCategory());
            keyword.setTitle(keywordEntity.getTitle());
            keyword.setCount(keywordEntity.getCount());
            keyword.setCity(keywordEntity.getCity());
        }
        return keyword;
    }

    public List<Keyword> transform(List<KeywordEntity> keywordEntities) {
        List<Keyword> keywordList = new ArrayList<>();

        Keyword keyword;
        for (KeywordEntity entity : keywordEntities) {
            keyword = transform(entity);
            if (entity != null) {
                keywordList.add(keyword);
            }
        }
        return keywordList;
    }
}
