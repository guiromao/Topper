package co.topper.domain.service;

import co.topper.domain.data.dto.TopDto;

import java.util.List;

public interface TopService {

    List<TopDto> getTop(Integer offset, Integer limit);

}
