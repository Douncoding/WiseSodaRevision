package com.wisesoda.android.view.fragment;


import android.support.v4.app.Fragment;

//import com.wisesoda.android.presentation.WiseSodaApplication;
//import com.wisesoda.android.internal.di.HasComponent;
import com.wisesoda.android.WiseSodaApplication;
import com.wisesoda.android.internal.di.HasComponent;
import com.wisesoda.android.internal.di.components.ApplicationComponent;

/**
 *
 */
public class BaseFragment extends Fragment {

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>)getActivity()).getComponent());
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((WiseSodaApplication)(getActivity().getApplication())).getApplicationComponent();
    }
}
