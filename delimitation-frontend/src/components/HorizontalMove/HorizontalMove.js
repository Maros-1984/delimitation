export const HorizontalMove = ({rowIndex, cellIndex, gameWidth, game, highlightedMove}) => {
    const areaWidth = 90 / gameWidth + '%';
    const borderWidth = 10 / (gameWidth + 1) + '%';

    const horizontalBorderStyle = {
        paddingBottom: borderWidth,
        width: areaWidth,
    }

    const isPossibleBottomMove = (rowIndex, cellIndex) => game.possibleMoves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.bottom);
    const isHighlighted = highlightedMove?.areaX === cellIndex && highlightedMove?.areaY === rowIndex && highlightedMove?.bottom
    const isMade = game.moves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.bottom);

    return <td style={horizontalBorderStyle}
               className={isMade ? 'blue' : isPossibleBottomMove(rowIndex, cellIndex) ? isHighlighted ? 'highlightedBorder' : 'possibleMove border' : 'border'}
    />;
};