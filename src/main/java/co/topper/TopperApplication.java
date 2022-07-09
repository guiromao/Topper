package co.topper;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.service.MusicDiscoveryService;
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
	private MusicDiscoveryService discoveryService;

	public static void main(String[] args) {
		SpringApplication.run(TopperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Set<TrackDto> tracks = discoveryService.searchTracks("I am the walrus");

		tracks.forEach(track -> {
			System.out.print(track);
			track.getArtistIds().forEach(id -> System.out.print(" | " + discoveryService.findArtistById(id)));
			System.out.println();
		});

		Set<AlbumDto> albums = discoveryService.searchAlbums("Meteora");

		albums.forEach(System.out::println);
	}
}
