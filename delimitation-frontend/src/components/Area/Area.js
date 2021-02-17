import {useWindowDimensions} from "../../hooks/useWindowDimensions";
import {Loader} from "../Loader/Loader";
import {BORDER_PERCENTAGE_OF_AREA} from "../Game/configuration";

export const Area = ({gameWidth, color, areaX, areaY, setHighlightedMove, moveToMake}) => {
    const {width} = useWindowDimensions()
    const areaWidth = (100 - BORDER_PERCENTAGE_OF_AREA) / 100 / gameWidth * width;

    const areaStyle = {
        paddingBottom: areaWidth + "px",
        width: areaWidth + "px",
    };

    const handleMouseMove = (e) => {
        let x = e.nativeEvent.offsetX;
        let y = e.nativeEvent.offsetY;

        if (x > y) {
            if (areaWidth - x > y) {
                // Highlight upper border.
                setHighlightedMove({areaX, areaY: areaY - 1, bottom: true})
            } else {
                // Highlight right border.
                setHighlightedMove({areaX, areaY, right: true})
            }
        } else {
            if (areaWidth - x > y) {
                // Highlight left border.
                setHighlightedMove({areaX: areaX - 1, areaY, right: true})
            } else {
                // Highlight bottom border.
                setHighlightedMove({areaX, areaY, bottom: true})
            }
        }
    };

    return (<td onMouseMove={handleMouseMove} className={color.toLowerCase() + "Area"} style={areaStyle}>
        {moveToMake && moveToMake.areaX === areaX && moveToMake.areaY === areaY && (<Loader/>)}
    </td>)
};