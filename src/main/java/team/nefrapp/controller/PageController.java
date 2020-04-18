package team.nefrapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.Normalizer;

@Controller
public class PageController {
    @GetMapping({"/", "/dashboard"})
    public String dashboard() { return "dashboard"; }

    @GetMapping(value="/login")
    public String login() {
        return "login";
    }

    @GetMapping(value="/team")
    public String team() { return "team"; }

    @GetMapping(value="/error")
    public String error() { return "paginaErrore"; }

}