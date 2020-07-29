package co.kr.snack.store.config.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 
 * <b></b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2020.03.10, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2020.03.10
 */
public class UrlMatcher implements RequestMatcher {
    private OrRequestMatcher matchers;
    private OrRequestMatcher processingMatchers;

    public UrlMatcher(List<String> pathsToSkip, List<String> pathsToProcessing) {
        List<RequestMatcher> skipList = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path))
                .collect(Collectors.toList());
        
        List<RequestMatcher> processingList = pathsToProcessing.stream().map(path -> new AntPathRequestMatcher(path))
                .collect(Collectors.toList());
        
        matchers = new OrRequestMatcher(skipList);
        processingMatchers = new OrRequestMatcher(processingList);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (matchers.matches(request)) {
            return false;
        }
        return processingMatchers.matches(request) ? true : false;
    }
}
