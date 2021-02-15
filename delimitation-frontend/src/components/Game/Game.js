import './Game.css';
import {Fragment} from "react";
import {useCreateNewGame} from "./backendCalls";
import {Area, HorizontalMove, InbetweenDot, Loader, VerticalMove} from "..";

const Game = () => {
    const gameState = useCreateNewGame()

    if (!gameState) {
        return <Loader/>
    }

    const gameWidth = gameState.areas[0].length;

    return (
        <div>
            &nbsp;
            <table>
                <tbody>
                {gameState.areas.map((row, rowIndex) => {
                    return (
                        <Fragment key={'row-' + rowIndex}>
                            <tr>
                                {row.map((cell, cellIndex) => {
                                    return (
                                        <Fragment key={'cell-' + rowIndex + "-" + cellIndex}>
                                            <Area gameWidth={gameWidth} color={cell}/>
                                            <VerticalMove cellIndex={cellIndex} gameWidth={gameWidth}
                                                          rowIndex={rowIndex} possibleMoves={gameState.possibleMoves}/>
                                        </Fragment>)
                                })}
                            </tr>
                            {rowIndex !== gameState.areas.length - 1 && (
                                <tr key={'bottom-borders-of-row-' + rowIndex}>
                                    {row.map((cell, cellIndex) => {
                                        return (<Fragment key={'bottom-border-of-cell-' + rowIndex + "-" + cellIndex}>
                                                <HorizontalMove rowIndex={rowIndex} cellIndex={cellIndex}
                                                                gameWidth={gameWidth}
                                                                possibleMoves={gameState.possibleMoves}/>
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
