export const HorizontalMove = ({rowIndex, cellIndex, gameWidth, game, highlightedMove}) => {
    const areaWidth = 90 / gameWidth + '%';
    const borderWidth = 10 / (gameWidth + 1) + '%';

    const horizontalBorderStyle = {
        paddingBottom: borderWidth,
        width: areaWidth,
    }

    const isPossibleBottomMove = (rowIndex, cellIndex) => game.possibleMoves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.bottom);
    const isHighlighted = highlightedMove?.areaX === cellIndex && highlightedMove?.areaY === rowIndex && highlightedMove?.bottom
    const madeMoveColor = game.moves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.bottom)?.color?.toLowerCase();
    console.log(madeMoveColor, game.moves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.bottom))

    return <td style={horizontalBorderStyle}
               className={madeMoveColor ? madeMoveColor : isPossibleBottomMove(rowIndex, cellIndex) ? isHighlighted ? 'highlightedBorder' : 'possibleMove border' : 'border'}
    />;
};