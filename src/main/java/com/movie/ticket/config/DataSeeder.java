package com.movie.ticket.config;

import com.movie.ticket.entity.Movie;
import com.movie.ticket.entity.Show;
import com.movie.ticket.entity.Theatre;
import com.movie.ticket.entity.User;
import com.movie.ticket.repository.MovieRepository;
import com.movie.ticket.repository.TheatreRepository;
import com.movie.ticket.repository.UserRepository;
import com.movie.ticket.service.ShowService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ShowService showService;

    public DataSeeder(UserRepository userRepository, MovieRepository movieRepository,
                      TheatreRepository theatreRepository, ShowService showService) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.showService = showService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Users
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Admin Manager");
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin123");
            admin.setPhone("9988776655");
            admin.setRole("ADMIN");
            userRepository.save(admin);

            User customer = new User();
            customer.setName("John Doe");
            customer.setEmail("user@gmail.com");
            customer.setPassword("user123");
            customer.setPhone("9988112233");
            customer.setRole("CUSTOMER");
            userRepository.save(customer);
        }

        // 2. Seed Theatres
        Theatre pvr = null;
        Theatre inox = null;
        if (theatreRepository.count() == 0) {
            pvr = new Theatre();
            pvr.setName("PVR: Nexus Mall");
            pvr.setCity("Bengaluru");
            pvr.setAddress("Koramangala, 5th Block");
            pvr.setCapacity(50); // Will create 50 seats
            pvr = theatreRepository.save(pvr);

            inox = new Theatre();
            inox.setName("INOX: Galleria Mall");
            inox.setCity("Mumbai");
            inox.setAddress("Yelahanka, Mumbai Highway");
            inox.setCapacity(40); // Will create 40 seats
            inox = theatreRepository.save(inox);
        } else {
            pvr = theatreRepository.findAll().get(0);
            inox = theatreRepository.findAll().get(0);
        }

        // 3. Seed Movies
        Movie avengers = null;
        Movie interstellar = null;
        Movie darkKnight = null;
        if (movieRepository.count() == 0) {
            avengers = new Movie();
            avengers.setTitle("Avengers: Endgame");
            avengers.setDescription("After the devastating events of Avengers: Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe.");
            avengers.setDurationMinutes(181);
            avengers.setLanguage("English");
            avengers.setGenre("Action, Sci-Fi");
            avengers.setPosterUrl("https://images.unsplash.com/photo-1594909122845-11baa439b7bf?q=80&w=300&auto=format&fit=crop");
            avengers.setTrailerUrl("https://www.youtube.com/embed/TcMBFSGVi4A");
            avengers.setReleaseDate(LocalDate.of(2019, 4, 26));
            avengers = movieRepository.save(avengers);

            interstellar = new Movie();
            interstellar.setTitle("Interstellar");
            interstellar.setDescription("When Earth becomes uninhabitable, a team of explorers undertakes the most important mission in human history: traveling beyond this galaxy to discover whether mankind has a future among the stars.");
            interstellar.setDurationMinutes(169);
            interstellar.setLanguage("English");
            interstellar.setGenre("Sci-Fi, Adventure");
            interstellar.setPosterUrl("https://images.unsplash.com/photo-1534447677768-be436bb09401?q=80&w=300&auto=format&fit=crop");
            interstellar.setTrailerUrl("https://www.youtube.com/embed/zSWdZVtXT7E");
            interstellar.setReleaseDate(LocalDate.of(2014, 11, 7));
            interstellar = movieRepository.save(interstellar);

            darkKnight = new Movie();
            darkKnight.setTitle("The Dark Knight");
            darkKnight.setDescription("When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
            darkKnight.setDurationMinutes(152);
            darkKnight.setLanguage("English");
            darkKnight.setGenre("Action, Crime, Drama");
            darkKnight.setPosterUrl("https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?q=80&w=300&auto=format&fit=crop");
            darkKnight.setTrailerUrl("https://www.youtube.com/embed/LDG9bisJEaI");
            darkKnight.setReleaseDate(LocalDate.of(2008, 7, 18));
            darkKnight = movieRepository.save(darkKnight);
        } else {
            avengers = movieRepository.findAll().get(0);
            interstellar = movieRepository.findAll().get(0);
            darkKnight = movieRepository.findAll().get(0);
        }

        // 4. Seed Shows (which automatically seeds Seats)
        if (showService.getAllShows().isEmpty()) {
            // Show 1: Avengers Today at 14:00 (PVR)
            Show show1 = new Show();
            show1.setMovie(avengers);
            show1.setTheatre(pvr);
            show1.setShowDate(LocalDate.now());
            show1.setShowTime(LocalTime.of(14, 0));
            show1.setTicketPrice(250.0);
            showService.saveShow(show1);

            // Show 2: Avengers Today at 19:00 (PVR)
            Show show2 = new Show();
            show2.setMovie(avengers);
            show2.setTheatre(pvr);
            show2.setShowDate(LocalDate.now());
            show2.setShowTime(LocalTime.of(19, 0));
            show2.setTicketPrice(280.0);
            showService.saveShow(show2);

            // Show 3: Interstellar Today at 15:30 (INOX)
            Show show3 = new Show();
            show3.setMovie(interstellar);
            show3.setTheatre(inox);
            show3.setShowDate(LocalDate.now());
            show3.setShowTime(LocalTime.of(15, 30));
            show3.setTicketPrice(220.0);
            showService.saveShow(show3);

            // Show 4: The Dark Knight Tomorrow at 18:00 (PVR)
            Show show4 = new Show();
            show4.setMovie(darkKnight);
            show4.setTheatre(pvr);
            show4.setShowDate(LocalDate.now().plusDays(1));
            show4.setShowTime(LocalTime.of(18, 0));
            show4.setTicketPrice(260.0);
            showService.saveShow(show4);
        }
    }
}
