package com.game.controller;

import com.game.entity.Player;
import com.game.repository.PlayersRepository;
import com.game.service.PlayerValidator;
import com.game.service.PlayersJPACriteriaBuilder;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

    @GetMapping("/players/{id}")
    public Object getPlayerByID(@PathVariable Long id) {
        if (id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Player> player = playersRepository.findById(id);
        if (player.isPresent()) {
            return player.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player player) {
        player.updateLevel();
        DataBinder dataBinder = new DataBinder(player);
        dataBinder.addValidators(new PlayerValidator());
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return playersRepository.save(player);
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player newPlayer) {
        if (id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Player> currentPlayerOptional = playersRepository.findById(id);
        if (currentPlayerOptional.isPresent()) {
            Player currentPlayer = currentPlayerOptional.get();
            if (newPlayer.getName() != null) {
                currentPlayer.setName(newPlayer.getName());
            }
            if (newPlayer.getTitle() != null) {
                currentPlayer.setTitle(newPlayer.getTitle());
            }
            if (newPlayer.getProfession() != null) {
                currentPlayer.setProfession(newPlayer.getProfession());
            }
            if (newPlayer.getRace() != null) {
                currentPlayer.setRace(newPlayer.getRace());
            }
            if (newPlayer.getBirthday() != null) {
                currentPlayer.setBirthday(newPlayer.getBirthday());
            }
            if (newPlayer.getExperience() != null) {
                currentPlayer.setExperience(newPlayer.getExperience());
            }
            if (newPlayer.isBanned()) {
                currentPlayer.setBanned(true);
            }

            DataBinder dataBinder = new DataBinder(currentPlayer);
            dataBinder.addValidators(new PlayerValidator());
            dataBinder.validate();
            if (dataBinder.getBindingResult().hasErrors()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            return playersRepository.save(currentPlayer);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        if (id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (playersRepository.existsById(id)) {
            playersRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
