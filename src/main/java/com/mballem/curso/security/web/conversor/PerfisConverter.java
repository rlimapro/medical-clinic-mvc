package com.mballem.curso.security.web.conversor;

import com.mballem.curso.security.domain.Perfil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PerfisConverter implements Converter<String[], List<Perfil>> {

    @Override
    public List<Perfil> convert(String[] strings) {
        List<Perfil> perfis = new ArrayList<>();
        for (String id : strings) {
            if(!id.equals("0")) {
                perfis.add(new Perfil(Long.parseLong(id)));
            }
        }
        return perfis;
    }
}
