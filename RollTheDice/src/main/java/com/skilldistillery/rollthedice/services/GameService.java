package com.skilldistillery.rollthedice.services;

import java.util.List;

import com.skilldistillery.rollthedice.entities.Game;
import com.skilldistillery.rollthedice.entities.Genre;

public interface GameService {
	
	List<Game> index();
	Game show(int id);
	Game create(Game game, String username, List<Genre> genres);
	Game update(Game bame, int gid, String username);
	boolean delete(int id, String username);
	List<Game> searchKeyword(String keyword);

}
