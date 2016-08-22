package com.wisesoda.data.repository.datasource;

import android.content.Context;

import com.wisesoda.data.R;
import com.wisesoda.data.entity.BlogEntity;
import com.wisesoda.data.entity.BlogSortType;
import com.wisesoda.data.entity.mapper.BlogEntityCsvMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;

/**
 * 서버 구현이전의 테스트 목적으로 사용한다
 * 서버의 데이터 로딩이 없이 관리자가 제공하는 CSV 파일을 기반으로 생성되는 {@link BlogDataStore}
 */
public class BlogSampleDataStore implements BlogDataStore {
    private final static int LOAD_ITEM_PER_PAGE_COUNT = 15;

    private final Context context;
    private final BlogEntityCsvMapper blogEntityCsvMapper;

    private Collection<BlogEntity> blogEntityCollection;

    public BlogSampleDataStore(Context context, BlogEntityCsvMapper csvMapper) {
        this.context = context;
        this.blogEntityCsvMapper = csvMapper;
        this.blogEntityCollection = parseToBlogList();
    }

    private List<BlogEntity> parseToBlogList() {
        InputStream in = context.getResources().openRawResource(R.raw.blog_sample_1);

        List<BlogEntity> csvBlogEntities = new ArrayList<>();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "euc-kr"));
            while ((line = reader.readLine()) != null) {
                csvBlogEntities.add(blogEntityCsvMapper.transform(line));
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvBlogEntities;
    }

    @Override
    public Observable<List<BlogEntity>> blogEntityList(final String city,
                                                       final String category,
                                                       final String keyword,
                                                       final String sortType,
                                                       final int page) {
        return Observable.create(new Observable.OnSubscribe<List<BlogEntity>>() {
            @Override
            public void call(Subscriber<? super List<BlogEntity>> subscriber) {
                List<BlogEntity> resEntities = new ArrayList<>();
                List<BlogEntity> blogEntities = new ArrayList<>(blogEntityCollection);

                switch (BlogSortType.valueOf(sortType)) {
                    case DATE:
                        Collections.sort(blogEntities, new DateDescCompare());
                        break;
                    case PHOTO:
                        Collections.sort(blogEntities, new PhotoDescCompare());
                        break;
                    case VIEWS:
                        Collections.sort(blogEntities, new ViewsDescCompare());
                        break;
                    case RATE:
                        Collections.sort(blogEntities, new RateDescCompare());
                        break;
                    case BOOKMARK:
                        Collections.sort(blogEntities, new BookmarkDescCompare());
                        break;
                    default:
                        break;
                }

                // 0~9, 10~19, 20~29
                int offset = (page) * LOAD_ITEM_PER_PAGE_COUNT;
                int limit = offset + (LOAD_ITEM_PER_PAGE_COUNT - 1);

                for (int i = offset; i < limit; i++) {
                    if (i < blogEntityCollection.size()) {
                        resEntities.add(blogEntities.get(i));
                    } else {
//                        Log.e("CHECK", "type:" + sortType + "//i:" + i + "//offset:" + offset + "//limit:" + limit + "//total:" + blogEntities.size());
                        break;
                    }
                }

                subscriber.onNext(resEntities);
                subscriber.onCompleted();
            }
        });
    }

    static class DateDescCompare implements Comparator<BlogEntity> {
        @Override
        public int compare(BlogEntity blogEntity, BlogEntity t1) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
                Date date1 = format.parse(blogEntity.getDate());
                Date date2 = format.parse(t1.getDate());
                return date2.compareTo(date1);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class PhotoDescCompare implements Comparator<BlogEntity> {
        @Override
        public int compare(BlogEntity blogEntity, BlogEntity t1) {
            return t1.getImageCount() - blogEntity.getImageCount();
        }
    }

    static class ViewsDescCompare implements Comparator<BlogEntity> {
        @Override
        public int compare(BlogEntity blogEntity, BlogEntity t1) {
            return t1.getViewsCount() - blogEntity.getViewsCount();
        }
    }

    static class RateDescCompare implements Comparator<BlogEntity> {
        @Override
        public int compare(BlogEntity blogEntity, BlogEntity t1) {
            return (int)(t1.getRate() - blogEntity.getRate());
        }
    }

    static class BookmarkDescCompare implements Comparator<BlogEntity> {
        @Override
        public int compare(BlogEntity blogEntity, BlogEntity t1) {
            return t1.getBookmarkCount() - blogEntity.getBookmarkCount();
        }
    }
}
