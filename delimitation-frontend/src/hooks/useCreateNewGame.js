import {useEffect, useState} from "react";
import {backendUrl} from "../components/Game/configuration";

export const useCreateNewGame = () => {
    const [game, setGame] = useState()

    useEffect(() => {
        fetch(backendUrl + "create-new-game", {
            method: 'POST', body: "{}", headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => setGame(data));
    }, [])

    return {game, setGame};
}