import {useEffect, useState} from "react";
import {BACKEND_URL} from "../components/Game/configuration";

export const useCreateNewGame = () => {
    const [game, setGame] = useState()

    useEffect(() => {
        const gameId = document.location.hash?.substring(1)
        let lastPlayerColor = sessionStorage.getItem('playerColor')
        const lastGameId = sessionStorage.getItem('playerColor')
        if (gameId !== lastGameId) {
            lastPlayerColor = null
        }

        if (gameId?.length > 0) {
            fetch(BACKEND_URL + "get-game-status", {
                method: 'POST', body: JSON.stringify({gameId, playerAsking: lastPlayerColor}), headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.json())
                .then(data => {
                    setGame(data);
                    sessionStorage.setItem('playerColor', data.yourPlayerColor)
                    sessionStorage.setItem('gameId', data.gameId)
                });

        } else {
            fetch(BACKEND_URL + "create-new-game", {
                method: 'POST', body: "{}", headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.json())
                .then(data => {
                    setGame(data);
                    document.location.hash = data.gameId;
                    sessionStorage.setItem('playerColor', data.yourPlayerColor)
                    sessionStorage.setItem('gameId', data.gameId)
                });
        }
    }, [])

    return {game, setGame};
}