package org.but.feec.cinema.services;

import org.but.feec.cinema.api.dummy.DummyBasicView;
import org.but.feec.cinema.data.DummyRepository;

import java.util.List;

public class DummyService {

    private DummyRepository dummyRepository;

    public DummyService(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    public List<DummyBasicView> getDummyBasicView() {
        return dummyRepository.getDummyBasicView();
    }

    public void createString(DummyBasicView dummyBasicView) {
        dummyRepository.createString(dummyBasicView);
    }

}
