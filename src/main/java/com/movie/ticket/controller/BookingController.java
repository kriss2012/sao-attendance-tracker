package com.movie.ticket.controller;

import com.movie.ticket.dto.BookingRequestDTO;
import com.movie.ticket.entity.*;
import com.movie.ticket.repository.SeatRepository;
import com.movie.ticket.service.BookingService;
import com.movie.ticket.service.PaymentService;
import com.movie.ticket.service.ShowService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ShowService showService;
    private final PaymentService paymentService;
    private final SeatRepository seatRepository;

    public BookingController(BookingService bookingService, ShowService showService,
                             PaymentService paymentService, SeatRepository seatRepository) {
        this.bookingService = bookingService;
        this.showService = showService;
        this.paymentService = paymentService;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/seats/{showId}")
    public String showSeats(@PathVariable("showId") Long showId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        
        Show show = showService.getShowById(showId);
        List<Seat> seats = seatRepository.findByShowShowId(showId);
        
        model.addAttribute("show", show);
        model.addAttribute("seats", seats);
        model.addAttribute("bookingRequest", new BookingRequestDTO());
        return "seat-selection";
    }

    @PostMapping("/book")
    public String createBooking(@ModelAttribute("bookingRequest") BookingRequestDTO bookingRequest,
                                HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            Booking booking = bookingService.createBooking(
                    loggedInUser.getUserId(),
                    bookingRequest.getShowId(),
                    bookingRequest.getSeatNumbers()
            );
            return "redirect:/bookings/" + booking.getBookingId() + "/summary";
        } catch (Exception e) {
            // In case of double-booking or illegal choices, redirect back to seat selection with error
            model.addAttribute("errorMessage", e.getMessage());
            Show show = showService.getShowById(bookingRequest.getShowId());
            model.addAttribute("show", show);
            model.addAttribute("seats", seatRepository.findByShowShowId(bookingRequest.getShowId()));
            return "seat-selection";
        }
    }

    @GetMapping("/{id}/summary")
    public String showBookingSummary(@PathVariable("id") Long bookingId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Booking booking = bookingService.getBookingById(bookingId);
        // Security check: ensure this booking belongs to the logged-in user
        if (!booking.getUser().getUserId().equals(loggedInUser.getUserId())) {
            return "redirect:/";
        }

        model.addAttribute("booking", booking);
        return "booking-summary";
    }

    @PostMapping("/{id}/pay")
    public String payForBooking(@PathVariable("id") Long bookingId,
                                 @RequestParam("paymentMethod") String paymentMethod,
                                 HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        try {
            Payment payment = paymentService.processPayment(bookingId, paymentMethod);
            if ("SUCCESS".equals(payment.getStatus())) {
                return "redirect:/bookings/" + bookingId + "/success";
            } else {
                model.addAttribute("errorMessage", "Payment failed! Please try another payment method.");
                model.addAttribute("booking", bookingService.getBookingById(bookingId));
                return "booking-summary";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error processing transaction: " + e.getMessage());
            model.addAttribute("booking", bookingService.getBookingById(bookingId));
            return "booking-summary";
        }
    }

    @GetMapping("/{id}/success")
    public String showBookingSuccess(@PathVariable("id") Long bookingId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Booking booking = bookingService.getBookingById(bookingId);
        // Security check
        if (!booking.getUser().getUserId().equals(loggedInUser.getUserId())) {
            return "redirect:/";
        }

        Payment payment = paymentService.getPaymentByBooking(bookingId);
        model.addAttribute("booking", booking);
        model.addAttribute("payment", payment);
        return "booking-success";
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable("id") Long bookingId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Booking booking = bookingService.getBookingById(bookingId);
        // Security check
        if (!booking.getUser().getUserId().equals(loggedInUser.getUserId())) {
            return "redirect:/";
        }

        bookingService.cancelBooking(bookingId);
        return "redirect:/profile?cancelled=true";
    }
}
