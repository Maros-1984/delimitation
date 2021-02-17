import {useEffect} from "react";
import {BACKEND_URL} from "../components/Game/configuration";

export const useWaitingForAnotherPlayer = (game, setGame) => {
    useEffect(() => {
        if (!game || game.possibleMoves.length > 0) return

        setTimeout(() =>
            fetch(BACKEND_URL + "get-game-status", {
                method: 'POST', body: JSON.stringify({
                    gameId: game.gameId,
                    playerAsking: sessionStorage.getItem('playerColor'),
                    incremental: true
                }), headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.json())
                .then(data => setGame(data)), 2000);
    }, [game, setGame])
}