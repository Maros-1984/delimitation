import {BORDER_PERCENTAGE_OF_AREA} from "../Game/configuration";

export const VerticalMove = ({cellIndex, gameWidth, rowIndex, game, highlightedMove}) => {
    const areaWidth = (100 - BORDER_PERCENTAGE_OF_AREA) / gameWidth + '%';
    const borderWidth = BORDER_PERCENTAGE_OF_AREA / (gameWidth + 1) + '%';

    const verticalBorderStyle = {
        paddingBottom: areaWidth,
        width: borderWidth,
    }

    const isPossibleRightMove = (rowIndex, cellIndex) => game.possibleMoves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.right);
    const isHighlighted = highlightedMove?.areaX === cellIndex && highlightedMove?.areaY === rowIndex && highlightedMove?.right
    const madeMoveColor = game.moves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.right)?.color?.toLowerCase();

    return <>
        {cellIndex !== gameWidth - 1 &&
        <td style={verticalBorderStyle}
            className={madeMoveColor ? madeMoveColor : isPossibleRightMove(rowIndex, cellIndex) ? isHighlighted ? 'highlightedBorder' : 'possibleMove border' : 'border'}/>}
    </>;
};