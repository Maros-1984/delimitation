import './Game.css';
import {Fragment, useState} from "react";
import {useCreateNewGame} from "../../hooks/useCreateNewGame";
import {Area, HorizontalMove, InbetweenDot, Loader, VerticalMove} from "..";
import {useMakeMove} from "../../hooks/useMakeMove";
import {useWaitingForAnotherPlayer} from "../../hooks/useWaitingForAnotherPlayer";

const Game = () => {
    const [highlightedMove, setHighlightedMove] = useState()
    const [moveToMake, setMoveToMake] = useState()
    const {game, setGame} = useCreateNewGame()

    useMakeMove(moveToMake, setMoveToMake, game, setGame);
    useWaitingForAnotherPlayer(game, setGame)

    if (!game) {
        return <Loader/>
    }

    const gameWidth = game.areas[0].length;

    const handleOnClick = () => {
        if (game.possibleMoves.find(
            pm => pm.areaX === highlightedMove?.areaX
                && pm.areaY === highlightedMove?.areaY
                && ((pm.right && highlightedMove?.right) || (pm.bottom && highlightedMove?.bottom)))) {
            setMoveToMake(highlightedMove)
        }
    };

    return (
        <div onClick={handleOnClick}>
            &nbsp;
            <table>
                <tbody>
                {game.areas.map((row, rowIndex) => {
                    return (
                        <Fragment key={'row-' + rowIndex}>
                            <tr>
                                {row.map((cell, cellIndex) => {
                                    return (
                                        <Fragment key={'cell-' + rowIndex + "-" + cellIndex}>
                                            <Area gameWidth={gameWidth} color={cell} areaY={rowIndex} areaX={cellIndex}
                                                  setHighlightedMove={setHighlightedMove}/>
                                            <VerticalMove cellIndex={cellIndex} gameWidth={gameWidth}
                                                          rowIndex={rowIndex} game={game}
                                                          highlightedMove={highlightedMove}/>
                                        </Fragment>)
                                })}
                            </tr>
                            {rowIndex !== game.areas.length - 1 && (
                                <tr key={'bottom-borders-of-row-' + rowIndex}>
                                    {row.map((cell, cellIndex) => {
                                        return (<Fragment key={'bottom-border-of-cell-' + rowIndex + "-" + cellIndex}>
                                                <HorizontalMove rowIndex={rowIndex} cellIndex={cellIndex}
                                                                gameWidth={gameWidth} highlightedMove={highlightedMove}
                                                                game={game}/>
                                                <InbetweenDot cellIndex={cellIndex} gameWidth={gameWidth}/>
                                            </Fragment>
                                        )
                                    })
                                    }
                                </tr>)}
                        </Fragment>)
                })}
                </tbody>
            </table>
        </div>
    );
};

export default Game;
