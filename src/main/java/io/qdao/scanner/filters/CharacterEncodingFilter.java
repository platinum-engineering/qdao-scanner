package io.qdao.scanner.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {

    private final String characterEncoding;

    public CharacterEncodingFilter(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        request.setCharacterEncoding(characterEncoding);
        response.setCharacterEncoding(characterEncoding);

        chain.doFilter(request, response);

        request.setCharacterEncoding(characterEncoding);
        response.setCharacterEncoding(characterEncoding);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException { }
}
