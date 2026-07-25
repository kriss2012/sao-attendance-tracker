package com.movie.ticket.controller;

import com.movie.ticket.entity.Booking;
import com.movie.ticket.entity.Movie;
import com.movie.ticket.entity.Show;
import com.movie.ticket.entity.Theatre;
import com.movie.ticket.entity.User;
import com.movie.ticket.service.BookingService;
import com.movie.ticket.service.MovieService;
import com.movie.ticket.service.ShowService;
import com.movie.ticket.service.TheatreService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MovieService movieService;
    private final TheatreService theatreService;
    private final ShowService showService;
    private final BookingService bookingService;

    public AdminController(MovieService movieService, TheatreService theatreService,
                           ShowService showService, BookingService bookingService) {
        this.movieService = movieService;
        this.theatreService = theatreService;
        this.showService = showService;
        this.bookingService = bookingService;
    }

    private boolean isNotAdmin(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        return loggedInUser == null || !"ADMIN".equalsIgnoreCase(loggedInUser.getRole());
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (isNotAdmin(session)) {
            return "redirect:/login?accessDenied=true";
        }

        List<Movie> movies = movieService.getAllMovies();
        List<Theatre> theatres = theatreService.getAllTheatres();
        List<Show> shows = showService.getAllShows();
        List<Booking> bookings = bookingService.getAllBookings();

        double totalRevenue = bookings.stream()
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus()))
                .mapToDouble(Booking::getTotalAmount)
                .sum();

        model.addAttribute("movieCount", movies.size());
        model.addAttribute("theatreCount", theatres.size());
        model.addAttribute("showCount", shows.size());
        model.addAttribute("bookingCount", bookings.size());
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("recentBookings", bookings.size() > 5 ? bookings.subList(0, 5) : bookings);

        return "admin/dashboard";
    }

    // ==========================================
    // MOVIE CRUD
    // ==========================================

    @GetMapping("/movies")
    public String listMovies(HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin/movies";
    }

    @GetMapping("/movies/add")
    public String showAddMovieForm(HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("movie", new Movie());
        return "admin/movie-form";
    }

    @PostMapping("/movies/add")
    public String addMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult,
                           HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        if (bindingResult.hasErrors()) {
            return "admin/movie-form";
        }
        movieService.saveMovie(movie);
        return "redirect:/admin/movies?success=added";
    }

    @GetMapping("/movies/edit/{id}")
    public String showEditMovieForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("movie", movieService.getMovieById(id));
        return "admin/movie-form";
    }

    @PostMapping("/movies/edit/{id}")
    public String editMovie(@PathVariable("id") Long id, @Valid @ModelAttribute("movie") Movie movie,
                            BindingResult bindingResult, HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        if (bindingResult.hasErrors()) {
            return "admin/movie-form";
        }
        movieService.updateMovie(id, movie);
        return "redirect:/admin/movies?success=updated";
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        movieService.deleteMovie(id);
        return "redirect:/admin/movies?success=deleted";
    }

    // ==========================================
    // THEATRE CRUD
    // ==========================================

    @GetMapping("/theatres")
    public String listTheatres(HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("theatres", theatreService.getAllTheatres());
        return "admin/theatres";
    }

    @GetMapping("/theatres/add")
    public String showAddTheatreForm(HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("theatre", new Theatre());
        return "admin/theatre-form";
    }

    @PostMapping("/theatres/add")
    public String addTheatre(@Valid @ModelAttribute("theatre") Theatre theatre, BindingResult bindingResult,
                              HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        if (bindingResult.hasErrors()) {
            return "admin/theatre-form";
        }
        theatreService.saveTheatre(theatre);
        return "redirect:/admin/theatres?success=added";
    }

    @GetMapping("/theatres/edit/{id}")
    public String showEditTheatreForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("theatre", theatreService.getTheatreById(id));
        return "admin/theatre-form";
    }

    @PostMapping("/theatres/edit/{id}")
    public String editTheatre(@PathVariable("id") Long id, @Valid @ModelAttribute("theatre") Theatre theatre,
                              BindingResult bindingResult, HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        if (bindingResult.hasErrors()) {
            return "admin/theatre-form";
        }
        theatreService.updateTheatre(id, theatre);
        return "redirect:/admin/theatres?success=updated";
    }

    @PostMapping("/theatres/delete/{id}")
    public String deleteTheatre(@PathVariable("id") Long id, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        theatreService.deleteTheatre(id);
        return "redirect:/admin/theatres?success=deleted";
    }

    // ==========================================
    // SHOW CRUD
    // ==========================================

    @GetMapping("/shows")
    public String listShows(HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("shows", showService.getAllShows());
        return "admin/shows";
    }

    @GetMapping("/shows/add")
    public String showAddShowForm(HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("show", new Show());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theatres", theatreService.getAllTheatres());
        return "admin/show-form";
    }

    @PostMapping("/shows/add")
    public String addShow(@Valid @ModelAttribute("show") Show show, BindingResult bindingResult,
                          HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        if (bindingResult.hasErrors()) {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("theatres", theatreService.getAllTheatres());
            return "admin/show-form";
        }
        showService.saveShow(show);
        return "redirect:/admin/shows?success=added";
    }

    @GetMapping("/shows/edit/{id}")
    public String showEditShowForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("show", showService.getShowById(id));
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theatres", theatreService.getAllTheatres());
        return "admin/show-form";
    }

    @PostMapping("/shows/edit/{id}")
    public String editShow(@PathVariable("id") Long id, @Valid @ModelAttribute("show") Show show,
                           BindingResult bindingResult, HttpSession session, Model model) {
        if (isNotAdmin(session)) return "redirect:/login";
        if (bindingResult.hasErrors()) {
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("theatres", theatreService.getAllTheatres());
            return "admin/show-form";
        }
        showService.updateShow(id, show);
        return "redirect:/admin/shows?success=updated";
    }

    @PostMapping("/shows/delete/{id}")
    public String deleteShow(@PathVariable("id") Long id, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        showService.deleteShow(id);
        return "redirect:/admin/shows?success=deleted";
    }
}
