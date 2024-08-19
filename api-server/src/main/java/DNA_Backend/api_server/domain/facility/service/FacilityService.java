package DNA_Backend.api_server.domain.facility.service;

import static DNA_Backend.api_server.domain.facility.exception.FacilityExceptionMessage.FACILITY_NOT_FOUND;

import DNA_Backend.api_server.domain.facility.dto.FacilityDto.FacilityResponse;
import DNA_Backend.api_server.domain.facility.dto.FacilityDto.LocationTotalFacilityCountResponse;
import DNA_Backend.api_server.domain.facility.exception.FacilityException;
import DNA_Backend.api_server.domain.facility.model.Facility;
import DNA_Backend.api_server.domain.facility.model.FacilityType;
import DNA_Backend.api_server.domain.facility.repository.FacilityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    // id로 조회
    @Transactional(readOnly = true)
    public Facility findFacility(Long facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(()->new FacilityException(FACILITY_NOT_FOUND.getMessage()));
    }

    // 시설 검색 by 위도, 경도 & 타입
    @Transactional(readOnly = true)
    public List<FacilityResponse> searchFacilities(double latMin, double latMax, double lngMin, double lngMax, String facilityType) {
        FacilityType type = FacilityType.fromValue(facilityType);
        List<Facility> facilities = facilityRepository.findByLatitudeBetweenAndLongitudeBetweenAndType(
                latMin, latMax, lngMin, lngMax, type);
        return facilities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // 시설 검색 by 지역 id & 타입
    @Transactional(readOnly = true)
    public List<FacilityResponse> searchFacilitiesByLocationIdAndType(Long locationId, String facilityType) {
        FacilityType type = FacilityType.fromValue(facilityType);
        List<Facility> facilities = facilityRepository.findByLocationIdAndType(locationId, type);
        return facilities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private FacilityResponse toResponseDto(Facility facility) {
        return new FacilityResponse(
                facility.getId(),
                facility.getName(),
                facility.getType().getValue(),
                facility.getAddress(),
                facility.getLatitude(),
                facility.getLongitude());
    }

    // 전체 지역 총 시설 수 조회
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "AllLocationTotalFacilityCounts", cacheManager = "redisCacheManager")
    public List<LocationTotalFacilityCountResponse> getAllLocationTotalFacilityCounts() {
        return facilityRepository.countTotalFacilitiesGroupedByLocation();
    }

    // 단일 지역 총 시설 수 조회
    @Transactional(readOnly = true)
    public LocationTotalFacilityCountResponse getLocationTotalFacilityCount(Long locationId) {
        return toTotalFacilityCountResponseDto(locationId);
    }

    private LocationTotalFacilityCountResponse toTotalFacilityCountResponseDto(Long locationId) {
        long facilityCount = facilityRepository.countByLocationId(locationId);
        return new LocationTotalFacilityCountResponse(
                locationId,
                facilityCount
        );
    }

}
