package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayersRepository extends CrudRepository<Player, Long> {
}