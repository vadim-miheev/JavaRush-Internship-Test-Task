package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.MessageFormat;

public class PlayersJPACriteriaBuilder {

    private static Specification<Player> currentSpecification;

    public static Specification<Player> getSpecificationByHttpRequestParams(HttpServletRequest request) {

        currentSpecification = Specification.where(null);

        // Filter for name
        if (request.getParameter("name") != null) {
            addToCurrent((root, query, builder) -> builder.like(root.get("name"), contains(request.getParameter("name"))));
        }

        // Filter for title
        if (request.getParameter("title") != null) {
            addToCurrent((root, query, builder) -> builder.like(root.get("title"), contains(request.getParameter("title"))));
        }

        // Filters for birthday
        if (request.getParameter("after") != null) {
            addToCurrent((root, query, builder) -> builder.greaterThan(
                    root.get("birthday"),
                    new Date(Long.parseLong(request.getParameter("after")))
            ));
        }
        if (request.getParameter("before") != null) {
            addToCurrent((root, query, builder) -> builder.lessThan(
                    root.get("birthday"),
                    new Date(Long.parseLong(request.getParameter("before")))
            ));
        }

        // Filters for Experience
        if (request.getParameter("minExperience") != null) {
            addToCurrent((root, query, builder) -> builder.greaterThanOrEqualTo(
                    root.get("experience"),
                    Integer.parseInt(request.getParameter("minExperience"))
            ));
        }
        if (request.getParameter("maxExperience") != null) {
            addToCurrent((root, query, builder) -> builder.lessThanOrEqualTo(
                    root.get("experience"),
                    Integer.parseInt(request.getParameter("maxExperience"))
            ));
        }

        // Filters for Level
        if (request.getParameter("minLevel") != null) {
            addToCurrent((root, query, builder) -> builder.greaterThanOrEqualTo(
                    root.get("level"),
                    Integer.parseInt(request.getParameter("minLevel"))
            ));
        }
        if (request.getParameter("maxLevel") != null) {
            addToCurrent((root, query, builder) -> builder.lessThanOrEqualTo(
                    root.get("level"),
                    Integer.parseInt(request.getParameter("maxLevel"))
            ));
        }

        // Filters for Race
        if (request.getParameter("race") != null) {
            addToCurrent((root, query, builder) -> builder.equal(
                    root.get("race"),
                    Race.valueOf(request.getParameter("race"))
            ));
        }

        // Filters for Profession
        if (request.getParameter("profession") != null) {
            addToCurrent((root, query, builder) -> builder.equal(
                    root.get("profession"),
                    Profession.valueOf(request.getParameter("profession"))
            ));
        }

        // Filters by Ban
        if ("true".equals(request.getParameter("banned"))) {
            addToCurrent((root, query, builder) -> builder.isTrue(root.get("banned")));
        }

        // Filters by Ban
        if ("false".equals(request.getParameter("banned"))) {
            addToCurrent((root, query, builder) -> builder.isFalse(root.get("banned")));
        }

        return currentSpecification;
    }

    private static void addToCurrent(Specification<Player> other) {
         currentSpecification = currentSpecification == null ? Specification.where(other) : currentSpecification.and(other);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
