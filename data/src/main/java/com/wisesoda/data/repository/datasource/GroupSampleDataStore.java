package com.wisesoda.data.repository.datasource;

import android.content.Context;
import android.provider.SyncStateContract;
import android.util.Log;

import com.wisesoda.data.entity.Category;
import com.wisesoda.data.entity.KeywordEntity;
import com.wisesoda.data.entity.mapper.KeywordEntityDataMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GroupSampleDataStore implements GroupDataStore {
    private static final String TAG = GroupSampleDataStore.class.getSimpleName();

    private final Context context;
    public GroupSampleDataStore(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<KeywordEntity>> keywordEntityList(final String city, final String category, final String period) {
        return Observable.create(new Observable.OnSubscribe<List<KeywordEntity>>() {
            @Override
            public void call(Subscriber<? super List<KeywordEntity>> subscriber) {
                List<KeywordEntity> keywordEntities;

                if (city.equals("런던")) {
                    keywordEntities = generateLondon(category, period);
                } else {
                    keywordEntities = Collections.emptyList();
                }

                subscriber.onNext(keywordEntities);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<String>> cityEntityList() {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> cityList = new ArrayList<>();
                cityList.add("런던");
                cityList.add("뉴욕");

                subscriber.onNext(cityList);
                subscriber.onCompleted();
            }
        });
    }

    private List<KeywordEntity> generateLondon(String category, String period) {
        Collection<KeywordEntity> keywordModelCollection = new ArrayList<>();
        KeywordEntity entity = null;

        if (Category.valueOf(category) == Category.PL) {
            entity = new KeywordEntity();
            entity.setId(1);
            entity.setCity("런던");
            entity.setTitle("런던아이");
            entity.setCategory(category);
            keywordModelCollection.add(entity);

            entity = new KeywordEntity();
            entity.setId(2);
            entity.setCity("런던");
            entity.setTitle("빅벤");
            entity.setCategory(category);
            keywordModelCollection.add(entity);

            entity = new KeywordEntity();
            entity.setId(3);
            entity.setCity("런던");
            entity.setTitle("타워브릿지");
            entity.setCategory(category);
            keywordModelCollection.add(entity);

            entity = new KeywordEntity();
            entity.setId(4);
            entity.setCity("런던");
            entity.setTitle("버킹엄궁전");
            entity.setCategory(category);
            keywordModelCollection.add(entity);

            entity = new KeywordEntity();
            entity.setId(5);
            entity.setCity("런던");
            entity.setTitle("대영박물관");
            entity.setCategory(category);
            keywordModelCollection.add(entity);

            entity = new KeywordEntity();
            entity.setId(6);
            entity.setCity("런던");
            entity.setTitle("내셔널갤러리");
            entity.setCategory(category);
            keywordModelCollection.add(entity);

            entity = new KeywordEntity();
            entity.setId(7);
            entity.setCity("런던");
            entity.setTitle("코벤트가든");
            entity.setCategory(category);
            keywordModelCollection.add(entity);

            entity = new KeywordEntity();
            entity.setId(8);
            entity.setCity("런던");
            entity.setTitle("노팅힐");
            entity.setCategory(category);
            keywordModelCollection.add(entity);

            entity = new KeywordEntity();
            entity.setId(9);
            entity.setCity("런던");
            entity.setTitle("피카딜리서커스");
            entity.setCategory(category);
            keywordModelCollection.add(entity);
        } else if (Category.valueOf(category) == Category.RE) {
            for (int i = 0; i < 20; i++) {
                entity = new KeywordEntity();
                entity.setCity("런던");
                entity.setCategory(category);
                entity.setId(Integer.parseInt("100"+i));
                entity.setTitle("레스토랑 키워드#" + i + " " + period);
                keywordModelCollection.add(entity);
            }
        } else if (Category.valueOf(category) == Category.AC) {
            for (int i = 0; i < 20; i++) {
                entity = new KeywordEntity();
                entity.setId(Integer.parseInt("150"+i));
                entity.setCity("런던");
                entity.setCategory(category);
                entity.setTitle("호텔 키워드#" + i + " " + period);
                keywordModelCollection.add(entity);
            }
        } else if (Category.valueOf(category) == Category.TR) {

            for (int i = 0; i < 20; i++) {
                entity = new KeywordEntity();
                entity.setCity("런던");
                entity.setCategory(category);
                entity.setId(Integer.parseInt("1000"+i));
                entity.setTitle("교통 키워드#" + i + " " + period);
                keywordModelCollection.add(entity);
            }
        } else if (Category.valueOf(category) == Category.SH) {
            for (int i = 0; i < 20; i++) {
                entity = new KeywordEntity();
                entity.setCity("런던");
                entity.setCategory(category);
                entity.setId(Integer.parseInt("10000"+i));
                entity.setTitle("쇼핑 키워드#" + i + " " + period);
                keywordModelCollection.add(entity);
            }
        } else if (Category.valueOf(category) == Category.SM) {

            for (int i = 0; i < 20; i++) {
                entity = new KeywordEntity();
                entity.setCity("런던");
                entity.setCategory(category);
                entity.setId(Integer.parseInt("100000"+i));
                entity.setTitle("스마트폰 키워드#" + i + " " + period);
                keywordModelCollection.add(entity);
            }
        }

        keywordModelCollection.add(entity);
        return (List<KeywordEntity>)keywordModelCollection;
    }

}
