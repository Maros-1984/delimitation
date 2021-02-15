import {useEffect} from "react";
import {backendUrl} from "../components/Game/configuration";

export const useMakeMove = (moveToMake, setMoveToMake, game, setGame) => {
    useEffect(() => {
        if (!moveToMake) return

        fetch(backendUrl + "make-move", {
            method: 'POST',
            body: JSON.stringify({gameId: game.gameId, move: moveToMake, player: game.playerOnMove}),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                setMoveToMake(null)
                setGame(data);
            });
    }, [game?.gameId, game?.playerOnMove, moveToMake, setGame, setMoveToMake])
}