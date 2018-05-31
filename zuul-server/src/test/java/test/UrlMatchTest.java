package test;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Comparator;
import java.util.Map;

public class UrlMatchTest {

    public static void main(String[] args) {
        PathMatcher pathMatcher = new AntPathMatcher();
        boolean test = pathMatcher.match("/test/", "/test");
        System.out.println(test);
    }
}
