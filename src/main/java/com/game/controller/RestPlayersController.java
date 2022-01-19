package com.game.controller;

import com.game.entity.Player;
import com.game.repository.PlayersRepository;
import com.game.service.PlayersJPACriteriaBuilder;
import org.springframework.data.domain.*;
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

        return playersRepository.findAll(
                PlayersJPACriteriaBuilder.getSpecificationByHttpRequestParams(request),
                PageRequest.of(pageNumber, pageSize, Sort.by(order))
        ).getContent();
    }

    @GetMapping("/players/count")
    public Long getPlayersCount(HttpServletRequest request) {
        return playersRepository.count(PlayersJPACriteriaBuilder.getSpecificationByHttpRequestParams(request));
    }
}
