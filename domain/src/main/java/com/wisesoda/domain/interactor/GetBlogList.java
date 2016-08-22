package com.wisesoda.domain.interactor;

import com.wisesoda.domain.Blog;
import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.repository.BlogRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * {@link com.wisesoda.domain.Blog} 복록을 받기를 요청하는 비즈니스 로직
 *
 * 생성자의 매개변수가 주입을 요청하고 있음으로, 그래프에서는 다음의 3가지 매개변수에 대한 의존성을 제공해야 한다
 * 또한, 비즈니스 로직의 세부적인 요청은 없고 단순하게 목록을 가져오기 위한 목적으로 사용됨에 따라, Repository
 * 패턴에 따라 구현의 책임을 위임한다. 즉, 내부 데이터를 사용할지 클라우드 데이터를 사용하지에 대한 선택은 구현자
 * 클래스가 정의한다 (data 프레임워크)
 */
public class GetBlogList extends UseCase {
    private static final int REPRESENT_IMAGE_COUNT = 4;
    private static final String NAVER_TAG = "naver";
    private static final String NAVER_LOW_TYPE_PARAMS = "?type=s1";
    private static final String NAVER_HEIGHT_TYPE_PARAMS = "?type=s2";

    final private String city;
    final private String category;
    final private String keyword;
    final private String sortType;

    private int page = 0;
    private BlogRepository blogRepository;

    @Inject
    public GetBlogList(String city, String category, String keyword, String sortType,
                       BlogRepository blogRepository, ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.city = city;
        this.category = category;
        this.keyword = keyword;
        this.sortType = sortType;
        this.blogRepository = blogRepository;
    }

    public void setOptions(int page) {
        this.page = page;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return blogRepository.blogs(city, category, keyword, sortType, page)
                .delay(2000, TimeUnit.MILLISECONDS)
                .map(new Func1<List<Blog>, List<Blog>>(){
                    @Override
                    public List<Blog> call(List<Blog> blogs) {
                        return extractRepresentBlog(blogs);
                    }
                });
    }

    /**
     * 대표 블로그 추출
     *
     * 네이버 이지미의 경우 "?type=[X]" 형태의 쿼리를 제공한다. 따라서 대표 블로그로 선출되는 경우 보다 선명한 이미지를
     * 제공하기 위해 더 높은 해상도를 가지는 이미지 URL을 생성한다.
     * 네이버 이외의 블로그인 "티스트리", "이글루스" 경우는 별도 썸내일 이미지를 보유하고 있지 않기 때문에 이미지 그대로를
     * 메인 이미지로 사용한다.
     * @param blogList 블로그 목록
     */
    private List<Blog> extractRepresentBlog(List<Blog> blogList) {
        List<Integer> indexList = new ArrayList<>(blogList.size());
        for (int i = 0; i < indexList.size(); i++) {
            indexList.add(i);
        }

        Collections.shuffle(indexList);
        for (int i = 0; i < REPRESENT_IMAGE_COUNT; i++) {
            Blog item = blogList.get(i);
            String url = item.getImageUrl();

            if (url.contains(NAVER_TAG)) {
                String rep = url.replace(NAVER_LOW_TYPE_PARAMS, NAVER_HEIGHT_TYPE_PARAMS);
                item.setImageRepresent(rep);
            } else {
                item.setImageRepresent(item.getImageUrl());
            }

            blogList.set(i, item);
        }
        return blogList;
    }
}
