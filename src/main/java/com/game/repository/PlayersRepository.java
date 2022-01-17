package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PlayersRepository extends CrudRepository<Player, Long>, QueryByExampleExecutor<Player> {
}