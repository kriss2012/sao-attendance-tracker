package com.movie.ticket.controller;

import com.movie.ticket.entity.Movie;
import com.movie.ticket.entity.Show;
import com.movie.ticket.service.MovieService;
import com.movie.ticket.service.ShowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    private final MovieService movieService;
    private final ShowService showService;

    public HomeController(MovieService movieService, ShowService showService) {
        this.movieService = movieService;
        this.showService = showService;
    }

    @GetMapping("/")
    public String index(@RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "genre", required = false) String genre,
                        Model model) {
        List<Movie> movies;
        if (search != null && !search.trim().isEmpty()) {
            movies = movieService.searchMovies(search);
            model.addAttribute("searchQuery", search);
        } else if (genre != null && !genre.trim().isEmpty()) {
            movies = movieService.filterByGenre(genre);
            model.addAttribute("selectedGenre", genre);
        } else {
            movies = movieService.getAllMovies();
        }

        model.addAttribute("movies", movies);
        return "home";
    }

    @GetMapping("/movies/{id}")
    public String showMovieDetails(@PathVariable("id") Long movieId, Model model) {
        Movie movie = movieService.getMovieById(movieId);
        model.addAttribute("movie", movie);
        return "movie-details";
    }

    @GetMapping("/movies/{id}/theatres")
    public String selectTheatre(@PathVariable("id") Long movieId,
                                 @RequestParam(value = "date", required = false) String dateStr,
                                 Model model) {
        Movie movie = movieService.getMovieById(movieId);
        LocalDate selectedDate = (dateStr != null && !dateStr.isEmpty()) 
                ? LocalDate.parse(dateStr) 
                : LocalDate.now();

        List<Show> shows = showService.getShowsByMovieAndDate(movieId, selectedDate);
        
        model.addAttribute("movie", movie);
        model.addAttribute("shows", shows);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("dates", List.of(
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(3)
        ));
        return "theatre-select";
    }
}
