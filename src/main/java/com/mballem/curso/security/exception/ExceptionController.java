package com.mballem.curso.security.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException e) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 404);
        model.addObject("error", "Não foi possível realizar esta operação.");
        model.addObject("message", e.getMessage());
        return model;
    }
}
