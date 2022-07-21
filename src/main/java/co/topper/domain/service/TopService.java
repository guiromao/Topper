package co.topper.domain.service;

import co.topper.domain.data.dto.TopDto;
import co.topper.domain.data.dto.TrackDto;

import java.util.List;

public interface TopService {

    List<TopDto> getTop(Integer limit, Integer offset);

}
