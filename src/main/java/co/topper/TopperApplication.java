package co.topper;

import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.service.MusicSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Set;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class TopperApplication implements CommandLineRunner {

	@Autowired
	private MusicSearchService discoveryService;

	public static void main(String[] args) {
		SpringApplication.run(TopperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Set<TrackDto> tracks = discoveryService.searchTracks("civilization");

		tracks.forEach(track -> {
			System.out.print(track);
			//track.getArtistIds().forEach(id -> System.out.print(" | " + discoveryService.findArtistById(id)));
			System.out.println();
		});

	}
}
