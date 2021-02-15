import {useEffect, useState} from "react";
import {backendUrl} from "./configuration";

export const useCreateNewGame = () => {
    const [gameState, setGame] = useState()

    useEffect(() => {
        fetch(backendUrl + "create-new-game", {
            method: 'POST', body: "{}", headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => setGame(data));
    }, [])

    return gameState;
}