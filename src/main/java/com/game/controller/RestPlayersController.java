package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayersRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest")
public class RestPlayersController {

    private final PlayersRepository playersRepository;

    public RestPlayersController(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    @GetMapping("/players")
    public Iterable<Player> getPlayers(HttpServletRequest request) {
        int pageNumber = request.getParameter("pageNumber") == null ? 0 : Integer.parseInt(request.getParameter("pageNumber"));
        int pageSize = request.getParameter("pageSize") == null ? 3 : Integer.parseInt(request.getParameter("pageSize"));
        String order = request.getParameter("order") == null ? PlayerOrder.ID.getFieldName() : PlayerOrder.valueOf(request.getParameter("order")).getFieldName();

        return playersRepository.findAll(getPlayerExampleMatcher(request), PageRequest.of(pageNumber, pageSize, Sort.by(order))).getContent();
    }

    @GetMapping("/players/count")
    public Long getPlayersCount(HttpServletRequest request) {
        return playersRepository.count(getPlayerExampleMatcher(request));
    }

    private Example<Player> getPlayerExampleMatcher(HttpServletRequest request) {
        Player player = new Player();
        player.setName(request.getParameter("name"));
        player.setTitle(request.getParameter("title"));
        player.setBanned(Boolean.parseBoolean(request.getParameter("banned")));
        player.setRace(request.getParameter("race") == null ? null : Race.valueOf(request.getParameter("race")));
        player.setProfession(request.getParameter("profession") == null ? null : Profession.valueOf(request.getParameter("profession")));


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
                .withMatcher("title", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
                .withIgnoreNullValues();

        if (request.getParameter("banned") == null) {
            matcher = matcher.withIgnorePaths("banned");
        }

        return Example.of(player, matcher);
    }
}
