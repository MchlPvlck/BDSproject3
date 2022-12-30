package org.but.feec.cinema.api.projection;

import java.sql.Timestamp;

public class ProjectionEditView {
    private Long id;
    private Long userId;
    private Long projectionId;
    private Boolean status;
    private Timestamp reservation_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectionId() {
        return projectionId;
    }

    public void setProjectionId(Long projectionId) {
        this.projectionId = projectionId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Timestamp getReservation_date() {
        return reservation_date;
    }

    public void setReservation_date(Timestamp reservation_date) {
        this.reservation_date = reservation_date;
    }

    @Override
    public String toString() {
        return "ProjectionEditView{" +
                "userId=" + userId +
                ", movieId=" + projectionId +
                ", status=" + status +
                ", reservation_date='" + reservation_date + '\'' +
                '}';
    }
}
