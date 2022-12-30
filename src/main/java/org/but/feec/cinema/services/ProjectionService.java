package org.but.feec.cinema.services;

import org.but.feec.cinema.api.projection.*;
import org.but.feec.cinema.data.ProjectionRepository;

import java.util.List;

public class ProjectionService {

    private ProjectionRepository projectionRepository;

    public ProjectionService(ProjectionRepository projectionRepository) {
        this.projectionRepository = projectionRepository;
    }

    public ProjectionDetailView getProjectionDetailView(Long id) {
        return projectionRepository.findProjectionDetailView(id);
    }

    public List<ProjectionBasicView> getProjectionBasicView() {
        return projectionRepository.getProjectionBasicView();
    }

    public void createProjection(ProjectionCreateView projectionCreateView) {
        projectionRepository.createProjection(projectionCreateView);
    }

    public void editProjection(ProjectionEditView projectionEditView) {
        projectionRepository.editProjection(projectionEditView);
    }

    public void deleteProjection(ProjectionDeleteView projectionDeleteView) {
        projectionRepository.deleteProjection(projectionDeleteView);
    }

    public List<ProjectionBasicView> getProjectionFindView(String find, String choice) {
        return projectionRepository.getProjectionFindView(find, choice);
    }

}
