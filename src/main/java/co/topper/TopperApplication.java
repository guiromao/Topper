package co.topper;

import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Set;

@SpringBootApplication
@EnableCaching
public class TopperApplication implements CommandLineRunner {

	@Autowired
	private SearchService searchService;

	public static void main(String[] args) {
		SpringApplication.run(TopperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Set<TrackDto> tracks = searchService.searchTracks("I am the walrus");

		tracks.forEach(track -> {
			System.out.print(track);
			track.getArtistIds().forEach(id -> System.out.print(" | " + searchService.findArtistById(id)));
			System.out.println();
		});
	}
}
