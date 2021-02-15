export const InbetweenDot = ({cellIndex, gameWidth}) => {
    const borderWidth = 10 / (gameWidth + 1) + '%';

    const dotStyle = {
        paddingBottom: borderWidth,
        width: borderWidth,
    }

    return <>
        {cellIndex !== gameWidth - 1 &&
        <td
            style={dotStyle} className={'border'}/>}
    </>;
};