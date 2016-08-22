package com.wisesoda.android.model.mapper;

import com.wisesoda.android.model.KeywordModel;
import com.wisesoda.domain.Keyword;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class KeywordModelDataMapper {

    @Inject
    public KeywordModelDataMapper() {}

    public KeywordModel transform(Keyword entity) {
        KeywordModel model = new KeywordModel();

        if (entity != null) {
            model.setId(entity.getId());
            model.setCategory(entity.getCategory());
            model.setTitle(entity.getTitle());
            model.setCount(entity.getCount());
            model.setCity(entity.getCity());
        }

        return model;
    }

    public List<KeywordModel> transform(List<Keyword> keywords) {
        List<KeywordModel> modelList = new ArrayList<>();

        KeywordModel model;
        for (Keyword entity : keywords) {
            model = transform(entity);
            if (entity != null) {
                modelList.add(model);
            }
        }
        return modelList;
    }
}
