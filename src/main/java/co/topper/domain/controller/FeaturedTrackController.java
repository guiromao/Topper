package co.topper.domain.controller;

import co.topper.configuration.constants.ControllerConstants;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.service.FeaturedTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerConstants.APP + ControllerConstants.VERSION + "/hour")
public class FeaturedTrackController {

    private final FeaturedTrackService featuredTrackService;

    @Autowired
    public FeaturedTrackController(FeaturedTrackService service) {
        this.featuredTrackService = service;
    }

    @GetMapping
    public TrackDto getTrackOfTheHour() {
        return featuredTrackService.getFeaturedTrack();
    }

}
