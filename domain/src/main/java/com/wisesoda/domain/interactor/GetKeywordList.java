package com.wisesoda.domain.interactor;

import com.wisesoda.domain.executor.PostExecutionThread;
import com.wisesoda.domain.executor.ThreadExecutor;
import com.wisesoda.domain.repository.GroupRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * {@link com.wisesoda.domain.Blog} 복록을 받기를 요청하는 비즈니스 로직
 *
 * 생성자의 매개변수가 주입을 요청하고 있음으로, 그래프에서는 다음의 3가지 매개변수에 대한 의존성을 제공해야 한다
 * 또한, 비즈니스 로직의 세부적인 요청은 없고 단순하게 목록을 가져오기 위한 목적으로 사용됨에 따라, Repository
 * 패턴에 따라 구현의 책임을 위임한다. 즉, 내부 데이터를 사용할지 클라우드 데이터를 사용하지에 대한 선택은 구현자
 * 클래스가 정의한다 (data 프레임워크)
 */
public class GetKeywordList extends UseCase {
    private String city;
    private String category;
    private String period;

    private GroupRepository groupRepository;

    @Inject
    public GetKeywordList(GroupRepository groupRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.groupRepository = groupRepository;
    }

    public void setOptions(String city, String category, String period) {
        this.city = city;
        this.category = category;
        this.period = period;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.groupRepository.keywords(this.city, this.category, this.period);
    }
}
