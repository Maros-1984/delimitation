export const VerticalMove = ({cellIndex, gameWidth, rowIndex, possibleMoves, highlightedMove}) => {
    const areaWidth = 90 / gameWidth + '%';
    const borderWidth = 10 / (gameWidth + 1) + '%';

    const verticalBorderStyle = {
        paddingBottom: areaWidth,
        width: borderWidth,
    }

    const isPossibleRightMove = (rowIndex, cellIndex) => possibleMoves.find(pm => pm.areaX === cellIndex && pm.areaY === rowIndex && pm.right);
    const isHighlighted = highlightedMove?.areaX === cellIndex && highlightedMove?.areaY === rowIndex && highlightedMove?.right

    return <>
        {cellIndex !== gameWidth - 1 &&
        <td style={verticalBorderStyle}
            className={isPossibleRightMove(rowIndex, cellIndex) ? isHighlighted ? 'highlightedBorder' : 'possibleMove border' : 'border'}/>}
    </>;
};