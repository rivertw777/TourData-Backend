package TourData.backend.domain.facility.model;

import TourData.backend.domain.location.model.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@Entity
@Table(name = "facilities")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", insertable = false, updatable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = false, updatable = false)
    private FacilityType type;

    @NotNull
    @Column(name = "address", insertable = false, updatable = false)
    private String address;

    @NotNull
    @Column(name = "latitude", insertable = false, updatable = false)
    private double latitude;

    @NotNull
    @Column(name = "longitude", insertable = false, updatable = false)
    private double longitude;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", insertable = false, updatable = false)
    private Location location;

    @OneToMany(mappedBy = "facility")
    private List<FacilityBookmark> facilityBookmarks = new ArrayList<>();

    public void addFacilityBookmarks(FacilityBookmark facilityBookmark) {
        this.facilityBookmarks.add(facilityBookmark);
    }

}
