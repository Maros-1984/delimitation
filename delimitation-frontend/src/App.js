import './App.css';
import {Fragment, useEffect} from "react";
import {backendUrl} from "./configuration";
import {AreaColor} from "./constants";

const App = () => {
    useEffect(() => {
        fetch(backendUrl + "create-new-game", {
            method: 'POST', body: "{}", headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => console.log(data));
    }, [])

    const gameState = {
        areas: [
            [AreaColor.EMPTY, AreaColor.RED, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.BLUE, AreaColor.BLUE, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
            [AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY, AreaColor.EMPTY],
        ],
        possibleMoves: [
            {
                areaX: 0,
                areaY: 0,
                bottom: true
            },
            {
                areaX: 0,
                areaY: 0,
                right: true
            },
        ]

    }

    const areaWidth = 90 / (gameState.areas[0].length) + '%';
    const borderWidth = 10 / (gameState.areas[0].length + 1) + '%';
    const areaStyle = {
        paddingBottom: areaWidth,
        width: areaWidth,
    };
    const verticalBorderStyle = {
        paddingBottom: areaWidth,
        width: borderWidth,
    }
    const horizontalBorderStyle = {
        paddingBottom: borderWidth,
        width: areaWidth,
    }
    const dotStyle = {
        paddingBottom: borderWidth,
        width: borderWidth,
    }

    const isPossibleRightMove = (rowIndex, cellIndex) => gameState.possibleMoves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.right);
    const isPossibleBottomMove = (rowIndex, cellIndex) => gameState.possibleMoves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.bottom);

    return (
        <div>
            &nbsp;
            <table>
                <tbody>
                {gameState.areas.map((row, rowIndex) => {
                    return (
                        <Fragment>
                            <tr key={'row-' + rowIndex}>
                                {row.map((cell, cellIndex) => {
                                    return (<Fragment>
                                            <td key={'cell-' + rowIndex + "-" + cellIndex} style={{
                                                ...areaStyle,
                                                backgroundColor: cell === AreaColor.RED ? 'red' : cell === AreaColor.BLUE ? 'blue' : 'white'
                                            }}/>
                                            {cellIndex !== gameState.areas[0].length - 1 &&
                                            <td key={'right-border-of-cell-' + rowIndex + "-" + cellIndex}
                                                style={verticalBorderStyle}
                                                className={isPossibleRightMove(rowIndex, cellIndex) ? 'possibleMove border' : 'border'}/>}
                                        </Fragment>
                                    )
                                })
                                }
                            </tr>
                            {rowIndex !== gameState.areas.length - 1 && (
                                <tr key={'bottom-borders-of-row-' + rowIndex}>
                                    {row.map((cell, cellIndex) => {
                                        return (<Fragment>
                                                <td key={'bottom-border-of-cell-' + rowIndex + "-" + cellIndex}
                                                    style={horizontalBorderStyle}
                                                    className={isPossibleBottomMove(rowIndex, cellIndex) ? 'possibleMove border' : 'border'}
                                                />
                                                {cellIndex !== gameState.areas[0].length - 1 &&
                                                <td key={'dot-between-borders-' + rowIndex + "-" + cellIndex}
                                                    style={dotStyle} className={'border'}/>}
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

export default App;
