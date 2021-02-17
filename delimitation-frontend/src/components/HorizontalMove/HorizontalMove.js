import {BORDER_PERCENTAGE_OF_AREA} from "../Game/configuration";

export const HorizontalMove = ({rowIndex, cellIndex, gameWidth, game, highlightedMove}) => {
    const areaWidth = (100 - BORDER_PERCENTAGE_OF_AREA) / gameWidth + '%';
    const borderWidth = BORDER_PERCENTAGE_OF_AREA / (gameWidth + 1) + '%';

    const horizontalBorderStyle = {
        paddingBottom: borderWidth,
        width: areaWidth,
    }

    const isPossibleBottomMove = (rowIndex, cellIndex) => game.possibleMoves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.bottom);
    const isHighlighted = highlightedMove?.areaX === cellIndex && highlightedMove?.areaY === rowIndex && highlightedMove?.bottom
    const madeMoveColor = game.moves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.bottom)?.color?.toLowerCase();

    return <td style={horizontalBorderStyle}
               className={madeMoveColor ? madeMoveColor : isPossibleBottomMove(rowIndex, cellIndex) ? isHighlighted ? 'highlightedBorder' : 'possibleMove border' : 'border'}
    />;
};