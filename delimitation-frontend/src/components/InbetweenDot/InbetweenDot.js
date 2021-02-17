import {BORDER_PERCENTAGE_OF_AREA} from "../Game/configuration";

export const InbetweenDot = ({cellIndex, gameWidth}) => {
    const borderWidth = BORDER_PERCENTAGE_OF_AREA / (gameWidth + 1) + '%';

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