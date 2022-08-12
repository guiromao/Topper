package co.topper.domain.controller;

import co.topper.domain.data.dto.TopDto;
import co.topper.domain.service.TopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static co.topper.configuration.constants.ControllerConstants.*;

@RestController
@RequestMapping(APP + VERSION + "/top")
public class TopController {

    private final TopService topService;

    @Autowired
    public TopController(TopService topService) {
        this.topService = topService;
    }

    @GetMapping
    public List<TopDto> getTop(@RequestParam("page") Integer page) {
        return topService.getTop(page);
    }

}
